package com.dean.android.fw.convenient.googlemap.model;

import java.io.Serializable;

/**
 * Created by dean on 2017/4/10.
 */
public class RouteModel implements Serializable {

    private String latFrom;
    private String latTo;

    private String lngFrom;
    private String lngTo;

    private int type;

    public String getLatFrom() {
        return latFrom;
    }

    public void setLatFrom(String latFrom) {
        this.latFrom = latFrom;
    }

    public String getLatTo() {
        return latTo;
    }

    public void setLatTo(String latTo) {
        this.latTo = latTo;
    }

    public String getLngFrom() {
        return lngFrom;
    }

    public void setLngFrom(String lngFrom) {
        this.lngFrom = lngFrom;
    }

    public String getLngTo() {
        return lngTo;
    }

    public void setLngTo(String lngTo) {
        this.lngTo = lngTo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
