package com.dean.android.fw.convenientui.gaodemap.util;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 地图工具类
 * <p/>
 * Created by Dean on 16/8/25.
 */
public class GaoDeMapLocationUtil {

    private static final String TAG = GaoDeMapLocationUtil.class.getSimpleName();

    /**
     * 初始化地图控制器
     *
     * @param context
     * @param aMapLocationClientOption
     * @param aMapLocationListener
     * @return
     */
    public static AMapLocationClient initClient(Context context, AMapLocationClientOption aMapLocationClientOption, AMapLocationListener aMapLocationListener) {
        AMapLocationClient aMapLocationClient = new AMapLocationClient(context);

        aMapLocationClient.setLocationOption(aMapLocationClientOption);

        if (aMapLocationListener != null)
            aMapLocationClient.setLocationListener(aMapLocationListener);

        return aMapLocationClient;
    }

    /**
     * 初始化地图控制器配置参数
     *
     * @param locationMode
     * @param intervalTime 有值则为连续定位的间隔毫秒数，反之则为单次定位
     */
    public static AMapLocationClientOption initClientOption(AMapLocationClientOption.AMapLocationMode locationMode, Integer intervalTime) {

        AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(locationMode);
        if (intervalTime != null)
            aMapLocationClientOption.setInterval(intervalTime);
        else
            aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClientOption.setNeedAddress(true);
        aMapLocationClientOption.setLocationCacheEnable(true);

        return aMapLocationClientOption;
    }

    /**
     * 判断定位是否成功
     *
     * @param aMapLocation
     * @return
     */
    public static boolean isLocationSuccess(AMapLocation aMapLocation) {
        return aMapLocation != null && aMapLocation.getErrorCode() == 0;
    }

}
