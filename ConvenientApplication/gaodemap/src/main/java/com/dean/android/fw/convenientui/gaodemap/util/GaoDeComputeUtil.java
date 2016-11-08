package com.dean.android.fw.convenientui.gaodemap.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

/**
 * 高德计算工具类
 * <p/>
 * Created by Dean on 16/8/26.
 */
public class GaoDeComputeUtil {

    /**
     * 获取两个坐标之间的距离
     *
     * @param start
     * @param end
     * @return
     */
    public static double getDistance(LatLng start, LatLng end) {
        return AMapUtils.calculateLineDistance(start, end);
    }

    /**
     * AMapLocation 转 LatLng
     *
     * @param aMapLocation
     * @return
     */
    public static LatLng aMapLocation2Latlng(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            return latLng;
        }

        return null;
    }

}
