package com.dean.android.fw.convenientui.gaodemap.ui.activity;

import android.os.Bundle;

import com.amap.api.maps2d.MapView;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.dean.android.fw.convenientui.gaodemap.listener.GaoDeClickListener;
import com.dean.android.fw.convenientui.gaodemap.listener.GaoDeInputQueryListener;
import com.dean.android.fw.convenientui.gaodemap.listener.GaoDeLocationListener;
import com.dean.android.fw.convenientui.gaodemap.listener.GaoDeSearchListener;
import com.dean.android.fw.convenientui.gaodemap.object.TipObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.dean.android.fw.convenientframework.util.SetUtil;

/**
 * 搜索结果 Activity
 * <p>
 * Created by Dean on 2016/9/28.
 */
public abstract class GaoDeInputQueryActivity extends GaoDeMapActivity implements Inputtips.InputtipsListener {

    private Inputtips inputtips;
    private InputtipsQuery inputtipsQuery;

    private GaoDeInputQueryListener gaoDeInputQueryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gaoDeInputQueryListener = setGaoDeInputQueryListener();
    }

    public void query(String content, String city) {
        inputtipsQuery = new InputtipsQuery(content, city);
        inputtipsQuery.setCityLimit(true);

        inputtips = new Inputtips(this, inputtipsQuery);
        inputtips.setInputtipsListener(this);

        inputtips.requestInputtipsAsyn();
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        ArrayList<TipObject> tips = new ArrayList<>();

        if (i == 1000) {
            if (!SetUtil.isEmpty(list)) {
                for (Tip tip : list) {
                    TipObject tipObject = new TipObject(tip.getName(), tip.getAddress());
                    tipObject.setLatLonPoint(tip.getPoint());

                    tips.add(tipObject);
                }
            }

            gaoDeInputQueryListener.onInputQuery(tips);
        } else
            gaoDeInputQueryListener.onError();
    }

    protected abstract GaoDeInputQueryListener setGaoDeInputQueryListener();

    @Override
    protected MapView setMap2dView() {
        return null;
    }

    @Override
    protected GaoDeLocationListener setGaoDeLocationListener() {
        return null;
    }

    @Override
    protected GaoDeSearchListener setGaoDeSearchListener() {
        return null;
    }

    @Override
    protected GaoDeClickListener setGaoDeClickListener() {
        return null;
    }

}
