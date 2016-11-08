package com.dean.android.fw.convenientui.gaodemap.listener;

import com.amap.api.maps2d.model.LatLng;

/**
 * Created by Dean on 2016/9/28.
 */

public interface GaoDeClickListener {

    void onClicked(LatLng latLng, String addressName);
}
