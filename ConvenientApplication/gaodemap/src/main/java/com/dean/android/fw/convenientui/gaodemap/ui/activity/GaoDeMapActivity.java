package com.dean.android.fw.convenientui.gaodemap.ui.activity;

import android.content.Context;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.SetUtil;
import com.dean.android.fw.convenientui.gaodemap.listener.GaoDeClickListener;
import com.dean.android.fw.convenientui.gaodemap.listener.GaoDeLocationListener;
import com.dean.android.fw.convenientui.gaodemap.listener.GaoDeSearchListener;
import com.dean.android.fw.convenientui.gaodemap.util.GaoDeMapLocationUtil;

import java.util.List;

/**
 * 高德地图Activity
 * <p/>
 * Created by Dean on 16/8/25.
 */
public abstract class GaoDeMapActivity extends ConvenientActivity implements LocationSource, PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener,
        AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener {

    private MapView map2dView;
    private AMap aMap;
    private AMapLocationClient aMapLocationClient;
    private GaoDeLocationListener gaoDeLocationListener;
    private GaoDeSearchListener gaoDeSearchListener;
    private GaoDeClickListener gaoDeClickListener;
    private OnLocationChangedListener onLocationChangedListener;
    private GeocodeSearch geocodeSearch;

    private List<PoiItem> searchPOIItems;

    private AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            /** 设置基础地图定位显示位置 **/
            if (map2dView != null && onLocationChangedListener != null && aMapLocation != null) {
                aMap = map2dView.getMap();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        map2dView = setMap2dView();
        gaoDeLocationListener = setGaoDeLocationListener();
        gaoDeSearchListener = setGaoDeSearchListener();
        gaoDeClickListener = setGaoDeClickListener();

        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);

        setConfig(savedInstanceState);
    }

    /**
     * 设置基础地图和定位配置
     *
     * @param savedInstanceState
     */
    private void setConfig(Bundle savedInstanceState) {
        // 配置基础地图
        if (map2dView != null)
            map2dView.onCreate(savedInstanceState);

        // 配置基础地图和定位绑定
        if (map2dView != null) {
            aMap = map2dView.getMap();

            aMap.setLocationSource(this);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnMapClickListener(this);
            aMap.setMyLocationEnabled(true);
            aMap.getUiSettings().setAllGesturesEnabled(true);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.getUiSettings().setScaleControlsEnabled(true);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        }

        // 配置定位监听器
        AMapLocationClientOption aMapLocationClientOption = GaoDeMapLocationUtil.initClientOption(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy, 3000);
        aMapLocationClient = GaoDeMapLocationUtil.initClient(getApplicationContext(), aMapLocationClientOption, aMapLocationListener);
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

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
    }

    /**
     * 发起查询
     *
     * @param context
     * @param strQuery 查询关键字
     * @param type     查询类型
     * @param city     城市（nul是全国）
     */
    protected void poiSearch(Context context, String strQuery, String type, String city) {
        PoiSearch.Query query = new PoiSearch.Query(strQuery, type, city);
        query.setPageSize(5);

        PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(this);

        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int code) {
        if (code == 1000) {
            // 清理之前的图标
            clearSearchPOIItems();

            if (poiResult != null && poiResult.getQuery() != null) {
                // 取得第一页的 PoiItem 数据，页数从数字0开始
                searchPOIItems = poiResult.getPois();
                showSearchedInfo();
            } else
                ToastUtil.showToast(GaoDeMapActivity.this, "没有搜索到该地址");
        } else
            ToastUtil.showToast(GaoDeMapActivity.this, "搜索失败");
    }

    /**
     * 显示出搜索结果
     */
    private void showSearchedInfo() {
        if (!SetUtil.isEmpty(searchPOIItems)) {
            // 将搜索结果显示在基础地图上
            if (aMap != null) {
                PoiOverlay poiOverlay = new PoiOverlay(aMap, searchPOIItems);
                poiOverlay.removeFromMap();
                poiOverlay.addToMap();
                poiOverlay.zoomToSpan();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int code) {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // 点击了搜索结果中的某个位置图标
        if (!SetUtil.isEmpty(searchPOIItems)) {
            for (PoiItem poiItem : searchPOIItems) {
                LatLng latLng = marker.getPosition();
                LatLonPoint searchLatLonPoint = poiItem.getLatLonPoint();

                if (latLng.latitude == searchLatLonPoint.getLatitude() && latLng.longitude == searchLatLonPoint.getLongitude())
                    if (gaoDeSearchListener != null)
                        gaoDeSearchListener.onSearchSelected(poiItem);
            }
        }
        return false;
    }

    private LatLng mapClickLatLng;

    @Override
    public void onMapClick(LatLng latLng) {
        if (gaoDeClickListener != null) {
            mapClickLatLng = latLng;

            RegeocodeQuery regeocodeQuery = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 100, GeocodeSearch.AMAP);
            geocodeSearch.getFromLocationAsyn(regeocodeQuery);
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress();

                gaoDeClickListener.onClicked(mapClickLatLng, addressName);
            }
        } else {
            gaoDeClickListener.onClicked(null, null);
            ToastUtil.showToast(this, i + "");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }

    /**
     * 清理地图上的图标
     */
    private void clearSearchPOIItems() {
        if (searchPOIItems != null)
            searchPOIItems.clear();

        if (aMap != null)
            aMap.clear();
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

    /**
     * 设置高德搜索结果选择监听器实例
     *
     * @return
     */
    abstract protected GaoDeSearchListener setGaoDeSearchListener();

    /**
     * 设置高德地图 地图点击监听器实例
     *
     * @return
     */
    abstract protected GaoDeClickListener setGaoDeClickListener();

    @Override
    protected void onResume() {
        super.onResume();

        if (map2dView != null)
            map2dView.onResume();

    }

    @Override
    protected void onPause() {
        if (map2dView != null)
            map2dView.onPause();

        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (map2dView != null)
            map2dView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        stopLocation();

        if (map2dView != null)
            map2dView.onDestroy();

        super.onDestroy();
    }

}
