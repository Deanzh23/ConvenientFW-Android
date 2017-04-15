package com.dean.android.framework.convenientapplication;

import android.content.Intent;

import com.dean.android.framework.convenient.activity.ConvenientMainActivity;
import com.dean.android.framework.convenient.version.VersionUpdate;
import com.dean.android.framework.convenient.view.ContentView;

/**
 * Created by Dean on 2016/11/8.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends ConvenientMainActivity {

    @Override
    protected void showUpdateDownload(VersionUpdate versionUpdate) {
    }

    @Override
    protected void closeMainToHomeActivity() {
        startActivity(new Intent(this, CalendarActivity.class));
        this.finish();
    }
}