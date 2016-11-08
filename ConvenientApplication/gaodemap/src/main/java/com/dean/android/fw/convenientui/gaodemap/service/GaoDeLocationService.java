package com.dean.android.fw.convenientui.gaodemap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.dean.android.fw.convenientui.gaodemap.util.GaoDeMapLocationUtil;

/**
 * 高德定位Service
 * <p>
 * Created by Dean on 16/8/25.
 */
public abstract class GaoDeLocationService extends Service {

    private AMapLocationClient aMapLocationClient;
    private AMapLocationListener aMapLocationListener;

    @Override
    public void onCreate() {
        super.onCreate();

        aMapLocationListener = setAMapLocationListener();

        setConfig();
        startLocation();
    }

    /**
     * 设置基础地图和定位配置
     */
    private void setConfig() {
        // 配置定位监听器
        AMapLocationClientOption aMapLocationClientOption = GaoDeMapLocationUtil.initClientOption(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy,
                3000);
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
    public void onDestroy() {
        stopLocation();

        super.onDestroy();
    }

    /**
     * 设置高德定位监听器实例
     *
     * @return
     */
    abstract protected AMapLocationListener setAMapLocationListener();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
