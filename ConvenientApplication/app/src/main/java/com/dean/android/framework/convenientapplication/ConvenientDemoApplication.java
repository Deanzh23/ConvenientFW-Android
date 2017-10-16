package com.dean.android.framework.convenientapplication;

import android.os.Environment;

import com.dean.android.framework.convenient.application.ConvenientApplication;

/**
 * Created by Dean on 2016/11/8.
 */
public class ConvenientDemoApplication extends ConvenientApplication {

    @Override
    protected void initConfigAndData() {
    }

    @Override
    protected String setVersionUpdateDownloadLocalPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/version/update/";
    }

    @Override
    protected String checkVersionUrl() {
        return null;
    }
}
