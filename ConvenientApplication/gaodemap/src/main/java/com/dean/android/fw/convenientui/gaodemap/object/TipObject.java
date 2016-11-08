package com.dean.android.fw.convenientui.gaodemap.object;

import com.amap.api.services.core.LatLonPoint;

import java.io.Serializable;

/**
 * Created by Dean on 2016/9/28.
 */
public class TipObject implements Serializable {

    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public TipObject(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatLonPoint(LatLonPoint latLonPoint) {
        if (latLonPoint != null) {
            latitude = latLonPoint.getLatitude();
            longitude = latLonPoint.getLongitude();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
