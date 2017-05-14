package com.dean.android.fw.convenientui.gaodemap.ui;

import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.Marker;

/**
 * 自定义标注
 * <p>
 * Created by dean on 2017/4/20.
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter {

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
