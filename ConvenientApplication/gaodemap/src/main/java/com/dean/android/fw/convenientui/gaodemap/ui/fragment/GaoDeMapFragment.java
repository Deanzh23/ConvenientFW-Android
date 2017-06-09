package com.dean.android.fw.convenientui.gaodemap.ui.fragment;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.screen.ScreenUtils;
import com.dean.android.framework.convenient.util.SetUtil;
import com.dean.android.fw.convenientui.gaodemap.listener.GaoDeLocationListener;
import com.dean.android.fw.convenientui.gaodemap.listener.OnInfoWindowClickListener;
import com.dean.android.fw.convenientui.gaodemap.listener.OnMarkerClickListener;
import com.dean.android.fw.convenientui.gaodemap.object.GaoDeMapMarkerModel;
import com.dean.android.fw.convenientui.gaodemap.ui.infowinadapter.InfoWinAdapter;
import com.dean.android.fw.convenientui.gaodemap.util.GaoDeMapLocationUtil;

import java.util.List;

/**
 * 高德地图Fragment
 * <p>
 * Created by Dean on 16/8/26.
 */
public abstract class GaoDeMapFragment<T extends ViewDataBinding> extends ConvenientFragment<T> implements LocationSource, AMap.OnMarkerClickListener {

    private Context context;

    private MapView map2dView;

    private AMapLocationClient aMapLocationClient;

    private InfoWinAdapter infoWindowAdapter;

    private GaoDeLocationListener gaoDeLocationListener;
    private LocationSource.OnLocationChangedListener onLocationChangedListener;
    private OnMarkerClickListener onMarkerClickListener;

    private AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            /** 设置基础地图定位显示位置 **/
            if (map2dView != null && onLocationChangedListener != null && aMapLocation != null) {
                AMap aMap = map2dView.getMap();

                // 显示系统小蓝点
                onLocationChangedListener.onLocationChanged(aMapLocation);
                float bearing = aMap.getCameraPosition().bearing;
                // 设置小蓝点旋转角度
                aMap.setMyLocationRotateAngle(bearing);
            }

            /** 返回给UI层的业务使用 **/
            if (gaoDeLocationListener != null) {
                if (GaoDeMapLocationUtil.isLocationSuccess(aMapLocation))
                    gaoDeLocationListener.onSuccess(aMapLocation);
                else
                    gaoDeLocationListener.onError(aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        map2dView = setMap2dView();
        if (map2dView != null)
            map2dView.onCreate(savedInstanceState);

        gaoDeLocationListener = setGaoDeLocationListener();
        setConfig();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (map2dView != null)
            map2dView.onResume();
    }

    /**
     * 设置基础地图和定位配置
     */
    private void setConfig() {
        // 配置基础地图和定位绑定
        if (map2dView != null) {
            AMap aMap = map2dView.getMap();

            setZoomControlsEnabled(true);
            setCompassEnabled(true);
            setScaleControlsEnabled(true);

            aMap.setLocationSource(this);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        }

        // 配置定位监听器
        if (gaoDeLocationListener != null) {
            AMapLocationClientOption aMapLocationClientOption = GaoDeMapLocationUtil.initClientOption(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy,
                    3000);
            aMapLocationClient = GaoDeMapLocationUtil.initClient(getActivity().getApplicationContext(), aMapLocationClientOption, aMapLocationListener);
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        if (aMapLocationClient != null)
            aMapLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (aMapLocationClient != null && aMapLocationClient.isStarted())
            aMapLocationClient.stopLocation();
    }

    private List<? extends GaoDeMapMarkerModel> mapMarkerModels;

    /**
     * 设置标记
     *
     * @param latLngList
     */
    public void setMarkers(List<LatLng> latLngList, List<String> titleList, List<String> snippets, Integer iconId, List<? extends GaoDeMapMarkerModel> objects) {
        mapMarkerModels = objects;

        if (map2dView != null)
            map2dView.getMap().clear();

        if (SetUtil.isEmpty(latLngList))
            return;

        int size = latLngList.size();
        for (int i = 0; i < size; i++) {
            LatLng latLng = latLngList.get(i);
            String title = null;
            try {
                title = titleList.get(i);
            } catch (Exception e) {
            }
            String snippet = null;
            try {
                snippet = snippets.get(i);
            } catch (Exception e) {
            }

            MarkerOptions markerOptions = new MarkerOptions();
            // 经纬度
            markerOptions.position(latLng);
            // 标题
            if (!TextUtils.isEmpty(snippet))
                markerOptions.snippet(snippet);
            if (!TextUtils.isEmpty(title))
                markerOptions.title(title);
            // 设置自定义Marker图标
            if (iconId != null)
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), iconId)));
            // 可否拖动
            markerOptions.draggable(false);

            // 加载到地图上
            map2dView.getMap().addMarker(markerOptions);
        }

        map2dView.getMap().setOnMarkerClickListener(this);
    }

    /**
     * 设置最佳视角
     *
     * @param latLngList
     */
    public void setBestAngle(List<LatLng> latLngList) {
        if (SetUtil.isEmpty(latLngList))
            return;

        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        for (LatLng latLng : latLngList)
            latLngBounds.include(latLng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), ScreenUtils.dp2px(context, 36));

        if (map2dView != null)
            map2dView.getMap().animateCamera(cameraUpdate);
    }

    /**
     * 设置我的定位按钮是否可见
     *
     * @param enabled
     */
    public void setMyLocationButtonEnabled(boolean enabled) {
        UiSettings uiSettings = getMapUiSettings();
        if (uiSettings != null)
            uiSettings.setMyLocationButtonEnabled(enabled);
    }

    /**
     * UI中的缩放按钮是否可见
     *
     * @param enabled
     */
    public void setZoomControlsEnabled(boolean enabled) {
        UiSettings uiSettings = getMapUiSettings();
        if (uiSettings != null)
            uiSettings.setZoomControlsEnabled(enabled);
    }

    /**
     * UI中的指南针是否可见
     *
     * @param enabled
     */
    public void setCompassEnabled(boolean enabled) {
        UiSettings uiSettings = getMapUiSettings();
        if (uiSettings != null)
            uiSettings.setCompassEnabled(enabled);
    }

    /**
     * UI中的比例尺是否可见
     *
     * @param enabled
     */
    public void setScaleControlsEnabled(boolean enabled) {
        UiSettings uiSettings = getMapUiSettings();
        if (uiSettings != null)
            uiSettings.setScaleControlsEnabled(enabled);
    }

    /**
     * 获取高德地图的UiSettings对象
     *
     * @return
     */
    private UiSettings getMapUiSettings() {
        if (map2dView == null)
            return null;

        return map2dView.getMap().getUiSettings();
    }

    /**
     * Marker点击事件
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null) {
            if (marker.isInfoWindowShown())
                marker.hideInfoWindow();
            else
                marker.showInfoWindow();

            if (onMarkerClickListener != null) {
                if (!SetUtil.isEmpty(mapMarkerModels)) {
                    LatLng markerLatLng = marker.getPosition();

                    for (GaoDeMapMarkerModel mapMarkerModel : mapMarkerModels) {
                        LatLng latLng = mapMarkerModel.getLatLng();

                        try {
                            if (latLng != null && markerLatLng.latitude == latLng.latitude && markerLatLng.longitude == latLng.longitude) {
                                onMarkerClickListener.onMarkerClicked(mapMarkerModel);
                                break;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * 设置高德地图MapView实例
     *
     * @return
     */
    abstract protected MapView setMap2dView();

    /**
     * 设置高德定位监听器实例
     *
     * @return
     */
    abstract protected GaoDeLocationListener setGaoDeLocationListener();

    public void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        this.onMarkerClickListener = onMarkerClickListener;
    }

    public void setOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener) {
        infoWindowAdapter = new InfoWinAdapter(context, onInfoWindowClickListener);
        map2dView.getMap().setInfoWindowAdapter(infoWindowAdapter);
    }

    @Override
    public void onPause() {
        if (map2dView != null)
            map2dView.onPause();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        stopLocation();

        if (map2dView != null)
            map2dView.onDestroy();

        super.onDestroy();
    }
}
