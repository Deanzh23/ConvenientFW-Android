package com.dean.android.framework.convenient.fragment;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dean.android.framework.convenient.view.util.BindingViewController;

/**
 * 架构超类Fragment
 * <p>
 * Created by Dean on 2016/11/9.
 */
public class ConvenientFragment<T extends ViewDataBinding> extends Fragment {

    protected T viewDataBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = BindingViewController.inject(this, viewDataBinding);
        return viewDataBinding.getRoot();
    }
}