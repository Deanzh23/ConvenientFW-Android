package com.dean.android.fw.convenientui.gaodemap.listener;

import com.amap.api.location.AMapLocation;

/**
 * 高德定位UI层监听器
 * <p/>
 * Created by Dean on 16/8/26.
 */
public interface GaoDeLocationListener {

    void onSuccess(AMapLocation aMapLocation);

    void onError(int code, String message);

}
