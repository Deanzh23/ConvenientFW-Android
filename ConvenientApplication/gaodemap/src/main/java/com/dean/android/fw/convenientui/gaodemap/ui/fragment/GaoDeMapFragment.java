package com.dean.android.fw.convenientui.gaodemap.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.dean.android.fw.convenientui.gaodemap.listener.GaoDeLocationListener;
import com.dean.android.fw.convenientui.gaodemap.util.GaoDeMapLocationUtil;
import cn.com.dean.android.fw.convenientframework.fragment.ConvenientFragment;

/**
 * 高德地图Fragment
 * <p>
 * Created by Dean on 16/8/26.
 */
public abstract class GaoDeMapFragment extends ConvenientFragment implements LocationSource {

    private MapView map2dView;
    private AMapLocationClient aMapLocationClient;
    private GaoDeLocationListener gaoDeLocationListener;
    private LocationSource.OnLocationChangedListener onLocationChangedListener;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        map2dView = setMap2dView();
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
