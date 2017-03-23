package com.dean.android.framework.convenientapplication.ui;

import android.Manifest;
import android.os.Handler;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenientapplication.R;
import com.dean.android.framework.convenientapplication.databinding.ActivityDemoGoogleMapHtmlBinding;

/**
 * Google Map for HTML demo Activity
 * <p>
 * Created by dean on 2017/3/23.
 */
@Permission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
@ContentView(R.layout.activity_demo_google_map_html)
public class DemoGoogleMapHtmlActivity extends ConvenientActivity<ActivityDemoGoogleMapHtmlBinding> {

    private Handler handler = new Handler();

    @Override
    protected void onStart() {
        super.onStart();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                viewDataBinding.googleMapView.moveTo("40", "123.38");
            }
        }, 5000);
    }
}
