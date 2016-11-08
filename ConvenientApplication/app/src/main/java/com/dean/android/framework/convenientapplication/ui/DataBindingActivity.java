package com.dean.android.framework.convenientapplication.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.framework.convenientapplication.R;
import com.dean.android.framework.convenientapplication.bean.UserInfoBean;
import com.dean.android.framework.convenientapplication.databinding.ActivityDatabindingBinding;

@ContentView(R.layout.activity_databinding)
public class DataBindingActivity extends ConvenientActivity<ActivityDatabindingBinding> {

    private UserInfoBean userInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfoBean = new UserInfoBean();
        userInfoBean.setUserName("数据绑定UserName");

        viewDataBinding.setUserInfo(userInfoBean);
    }

    @OnClick(R.id.commitBtn)
    private void commitClicked() {
        Toast.makeText(DataBindingActivity.this, userInfoBean.getUserName(), Toast.LENGTH_SHORT).show();
    }

}