package com.dean.android.famework.convenientapplication.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.dean.android.famework.convenient.activity.ConvenientActivity;
import com.dean.android.famework.convenient.view.ContentView;
import com.dean.android.famework.convenient.view.OnClick;
import com.dean.android.famework.convenientapplication.R;
import com.dean.android.famework.convenientapplication.bean.UserInfoBean;
import com.dean.android.famework.convenientapplication.databinding.ActivityMainBinding;

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