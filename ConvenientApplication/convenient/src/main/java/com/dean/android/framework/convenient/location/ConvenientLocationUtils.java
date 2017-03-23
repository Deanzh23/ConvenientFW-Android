package com.dean.android.framework.convenient.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * 定位工具类
 * <p>
 * Created by dean on 2017/3/21.
 */
public class ConvenientLocationUtils {

    /**
     * @param context
     * @param provider
     * @param minTime
     * @param minDistance
     * @param locationListener
     */
    public static void requestLocationUpdates(Context context, String provider, final int minTime, final int minDistance, final LocationListener locationListener) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            LocationListener listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (locationListener != null) {
                        locationListener.onLocationChanged(location);

                        if (minTime == -1 && minDistance == -1)
                            locationManager.removeUpdates(this);
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    if (locationListener != null)
                        locationListener.onStatusChanged(provider, status, extras);
                }

                @Override
                public void onProviderEnabled(String provider) {
                    if (locationListener != null)
                        locationListener.onProviderEnabled(provider);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    if (locationListener != null)
                        locationListener.onProviderDisabled(provider);
                }
            };

            locationManager.requestLocationUpdates(provider, minTime, minDistance, listener);
        }
    }

    /**
     * 请求一次定位信息
     *
     * @param context
     * @param provider
     * @param locationListener
     */
    public static void requestLocationUpdatesOnce(Context context, String provider, LocationListener locationListener) {
        requestLocationUpdates(context, provider, -1, -1, locationListener);
    }

}
