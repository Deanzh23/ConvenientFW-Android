package com.dean.android.framework.convenient.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 架构超类Adapter
 * <p>
 * Created by Dean on 2016/11/9.
 */
public abstract class ConvenientAdapter<T extends ViewDataBinding> extends BaseAdapter {

    private T viewDataBinding;
    private int itemLayoutId;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null || viewDataBinding == null) {
            itemLayoutId = setItemLayoutId();
            viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), itemLayoutId, viewGroup, false);

            view = viewDataBinding.getRoot();
            view.setTag(viewDataBinding);
        } else
            viewDataBinding = (T) view.getTag();

        setItemView(viewDataBinding, i);

        return view;
    }

    /**
     * 设置item项布局id
     *
     * @return
     */
    public abstract int setItemLayoutId();

    /**
     * 设置业务的具体Item数据和行为
     *
     * @param viewDataBinding
     */
    public abstract void setItemView(T viewDataBinding, int index);

}
