package com.dean.android.framework.convenientapplication;

import android.Manifest;
import android.content.Intent;

import com.dean.android.framework.convenient.activity.ConvenientMainActivity;
import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.version.VersionUpdate;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenientapplication.ui.DataBindingActivity;

/**
 * Created by Dean on 2016/11/8.
 */
@Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
@ContentView(R.layout.activity_main)
public class MainActivity extends ConvenientMainActivity {

    @Override
    protected void showUpdateDownload(VersionUpdate versionUpdate) {
    }

    @Override
    protected void closeMainToHomeActivity() {
        startActivity(new Intent(this, DataBindingActivity.class));
        this.finish();
    }
}