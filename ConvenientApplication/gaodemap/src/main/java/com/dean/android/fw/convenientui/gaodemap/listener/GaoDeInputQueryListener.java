package com.dean.android.fw.convenientui.gaodemap.listener;

import com.dean.android.fw.convenientui.gaodemap.object.TipObject;

import java.util.List;

/**
 * Created by Dean on 2016/9/28.
 */

public interface GaoDeInputQueryListener {

    void onInputQuery(List<TipObject> tipObjects);

    void onError();
}
