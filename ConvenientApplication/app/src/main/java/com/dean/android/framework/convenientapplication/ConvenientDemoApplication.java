package com.dean.android.framework.convenientapplication;

import com.dean.android.framework.convenient.application.ConvenientApplication;

/**
 * Created by Dean on 2016/11/8.
 */
public class ConvenientDemoApplication extends ConvenientApplication {

    @Override
    protected void initConfigAndData() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }

//        DatabaseUtil.init();
    }

    @Override
    protected String checkVersionUrl() {
        return null;
    }
}
