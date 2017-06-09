package com.dean.android.fw.convenientui.gaodemap.object;

import android.databinding.BaseObservable;

import com.amap.api.maps2d.model.LatLng;

/**
 * Created by dean on 2017/5/25.
 */
public class GaoDeMapMarkerModel extends BaseObservable {

    protected LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
