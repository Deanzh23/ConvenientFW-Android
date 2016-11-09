package com.dean.android.framework.convenientapplication.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.dean.android.framework.convenient.fragment.ConvenientFragment;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenientapplication.R;
import com.dean.android.framework.convenientapplication.databinding.FragmentDatabindingBinding;

/**
 * Created by Dean on 2016/11/9.
 */
@ContentView(R.layout.fragment_databinding)
public class DataBindingFragment extends ConvenientFragment<FragmentDatabindingBinding> {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewDataBinding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DataBindingFragment.this.getActivity(), "Fragment clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
