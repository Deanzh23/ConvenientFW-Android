package com.dean.android.framework.convenientapplication.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.dean.android.famework.convenientapplication.R;
import com.dean.android.famework.convenientapplication.databinding.ActivityMainBinding;
import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.framework.convenientapplication.bean.UserInfoBean;

@ContentView(R.layout.activity_main)
public class MainActivity extends ConvenientActivity<ActivityMainBinding> {

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
        Toast.makeText(MainActivity.this, userInfoBean.getUserName(), Toast.LENGTH_SHORT).show();
    }

}