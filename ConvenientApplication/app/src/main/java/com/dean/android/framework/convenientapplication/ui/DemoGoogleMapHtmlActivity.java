package com.dean.android.framework.convenientapplication.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenientapplication.R;
import com.dean.android.framework.convenientapplication.databinding.ActivityDemoGoogleMapHtmlBinding;
import com.dean.android.fw.convenient.googlemap.model.LocationModel;
import com.dean.android.fw.convenient.googlemap.model.RouteModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Google Map for HTML demo Activity
 * <p>
 * Created by dean on 2017/3/23.
 */
@Permission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
@ContentView(R.layout.activity_demo_google_map_html)
public class DemoGoogleMapHtmlActivity extends ConvenientActivity<ActivityDemoGoogleMapHtmlBinding> {

    private Handler handler = new Handler();

    private List<RouteModel> routeModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        routeModels = new ArrayList<>();

        RouteModel routeModel1 = new RouteModel();
        routeModel1.setLatFrom("34.699667");
        routeModel1.setLatTo("34.730175");
        routeModel1.setLngFrom("135.499389");
        routeModel1.setLngTo("135.502958");
        routeModels.add(routeModel1);

        RouteModel routeModel2 = new RouteModel();
        routeModel2.setLatFrom("34.730175");
        routeModel2.setLatTo("35.167467");
        routeModel2.setLngFrom("135.502958");
        routeModel2.setLngTo("136.884539");
        routeModels.add(routeModel2);

        RouteModel routeModel3 = new RouteModel();
        routeModel3.setLatFrom("35.167467");
        routeModel3.setLatTo("35.139722");
        routeModel3.setLngFrom("136.884539");
        routeModel3.setLngTo("136.904058");
        routeModels.add(routeModel3);

        RouteModel routeModel4 = new RouteModel();
        routeModel4.setLatFrom("35.139722");
        routeModel4.setLatTo("35.122572");
        routeModel4.setLngFrom("136.904058");
        routeModel4.setLngTo("136.915369");
        routeModels.add(routeModel4);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(DemoGoogleMapHtmlActivity.this, "1");
                viewDataBinding.googleMapView.setRouteModels(routeModels);

                RouteModel routeModel = new RouteModel();
                routeModel.setLatFrom("34.699667");
                routeModel.setLatTo("35.122572");
                routeModel.setLngFrom("135.499389");
                routeModel.setLngTo("136.915369");

                List<LocationModel> locationModels = new ArrayList<>();
                LocationModel locationModel1 = new LocationModel("34.730175", "135.502958");
                locationModels.add(locationModel1);
                LocationModel locationModel2 = new LocationModel("35.167467", "136.884539");
                locationModels.add(locationModel2);

                viewDataBinding.googleMapView.navigationRoute(routeModel, locationModels);
            }
        }, 3000);
    }

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
