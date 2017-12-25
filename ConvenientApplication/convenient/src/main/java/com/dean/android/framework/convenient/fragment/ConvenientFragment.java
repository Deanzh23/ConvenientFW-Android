package com.dean.android.framework.convenient.fragment;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dean.android.framework.convenient.keyboard.KeyboardUtil;
import com.dean.android.framework.convenient.view.util.BindingViewController;
import com.umeng.analytics.MobclickAgent;

/**
 * 架构超类Fragment
 * <p>
 * Created by Dean on 2016/11/9.
 */
public class ConvenientFragment<T extends ViewDataBinding> extends Fragment {

    protected Activity activity;
    protected T viewDataBinding;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /** 设置DataBinding相关配置 **/
        viewDataBinding = BindingViewController.inject(this, viewDataBinding);

        return viewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 收起软键盘
        if (activity != null)
            KeyboardUtil.hideSoftKeyboard(activity);

        // 友盟Session统计
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();

        // 友盟Session统计
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}