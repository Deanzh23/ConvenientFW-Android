package com.dean.android.framework.convenient.version.listener;

import org.json.JSONObject;

/**
 * Created by Dean on 2016/10/18.
 */
public interface OnCheckVersionListener {

    void onCheck(boolean hasVersionUpdate, JSONObject updateMessage);

}
