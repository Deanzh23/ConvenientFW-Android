package com.dean.android.framework.convenientapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.view.ContentView;
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
        userInfoBean.setAvatarUrl("http://a.hiphotos.baidu.com/zhidao/pic/item/d1160924ab18972b0c6e3752e0cd7b899f510ad5.jpg");

        viewDataBinding.setUserInfo(userInfoBean);
        viewDataBinding.commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DataBindingActivity.this, userInfoBean.getUserName(), Toast.LENGTH_SHORT).show();
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout, new DataBindingFragment()).commit();
    }

}