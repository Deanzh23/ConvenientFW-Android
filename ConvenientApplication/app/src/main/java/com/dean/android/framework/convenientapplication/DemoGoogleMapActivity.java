package com.dean.android.framework.convenientapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.location.ConvenientLocationUtils;
import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by dean on 2017/3/21.
 */
@Permission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
@ContentView(R.layout.activity_demo_google_map)
public class DemoGoogleMapActivity extends ConvenientActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, LocationListener
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private MapFragment googleMapFragment;
//    private MapView googleMapView;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        googleMapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.mapLayout, googleMapFragment).commit();
        googleMapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(DemoGoogleMapActivity.class.getSimpleName(), "[onMapReady]--->");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);

//        LatLng sydney = new LatLng(-33.867, 151.206);
//
//
//
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        googleMap.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));

        ConvenientLocationUtils.requestLocationUpdates(this, LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        ToastUtil.showToast(this, "返回我的当前位置", Toast.LENGTH_SHORT, ToastUtil.LOCATION_MIDDLE);

//        ConvenientLocationUtils.requestLocationUpdates(this, LocationManager.NETWORK_PROVIDER, 0, 0, this);

        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        ToastUtil.showToast(this, "[onLocationChanged]--->", Toast.LENGTH_SHORT, ToastUtil.LOCATION_MIDDLE);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        ToastUtil.showToast(this, "[onStatusChanged]--->", Toast.LENGTH_SHORT, ToastUtil.LOCATION_MIDDLE);
    }

    @Override
    public void onProviderEnabled(String provider) {
        ToastUtil.showToast(this, "[onProviderEnabled]--->", Toast.LENGTH_SHORT, ToastUtil.LOCATION_MIDDLE);
    }

    @Override
    public void onProviderDisabled(String provider) {
        ToastUtil.showToast(this, "[onProviderDisabled]--->", Toast.LENGTH_SHORT, ToastUtil.LOCATION_MIDDLE);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(DemoGoogleMapActivity.class.getSimpleName(), "[onConnected]--->");
        ToastUtil.showToast(this, "连接成功", Toast.LENGTH_SHORT, ToastUtil.LOCATION_MIDDLE);

//        LocationRequest locationRequest = new LocationRequest();
//        locationRequest.setExpirationTime(0);
//        locationRequest.setExpirationDuration(0);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(DemoGoogleMapActivity.class.getSimpleName(), "[onConnectionSuspended]--->");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(DemoGoogleMapActivity.class.getSimpleName(), "[onConnectionFailed]--->");
        ToastUtil.showToast(this, "连接失败！error code is " + connectionResult.getErrorCode() + "\n" //
                        + "error message is " + connectionResult.getErrorMessage(), //
                Toast.LENGTH_SHORT, ToastUtil.LOCATION_MIDDLE);
    }
}
