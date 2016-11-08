package com.dean.android.framework.convenient.activity;

import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dean.android.framework.convenient.application.ConvenientApplication;
import com.dean.android.framework.convenient.permission.annotations.listener.PermissionAnnotationListener;
import com.dean.android.framework.convenient.permission.annotations.util.PermissionAnnotationsUtil;
import com.dean.android.framework.convenient.permission.util.PermissionsUtil;
import com.dean.android.framework.convenient.view.util.BindingViewController;

import java.util.ArrayList;
import java.util.List;

/**
 * 超类Activity
 * <p>
 * 用于进行相关配置
 * <p>
 * Created by Dean on 2016/11/7.
 */
public class ConvenientActivity<T extends ViewDataBinding> extends AppCompatActivity {

    /**
     * 申请的权限数组
     */
    private String[] mPermissionArray;

    // 数据绑定器
    protected T viewDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //透明状态栏
        if (translucentStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        /** 将当前activity加入到“历史记录” **/
        ConvenientApplication.addHistoryActivity(this);

        /** 设置DataBinding相关配置 **/
        viewDataBinding = BindingViewController.inject(this, viewDataBinding);

        /** 解析并申请权限 **/
        PermissionAnnotationsUtil.inject(this, new PermissionAnnotationListener() {
            @Override
            public void success(String[] permissionArray) {
                mPermissionArray = permissionArray;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /** 如果没有被申请的权限，则返回 **/
        if (mPermissionArray == null || mPermissionArray.length <= 0 || permissions == null || permissions.length <= 0)
            return;

        List<String> permissionList = new ArrayList<>();
        for (String permission : mPermissionArray)
            permissionList.add(permission);

        for (String permission : mPermissionArray) {
            /** 如果申请权限被用户拒绝 **/
            if (permissionList.contains(permission) && !PermissionsUtil.requestPermissionsSuccess(grantResults))
                Toast.makeText(ConvenientActivity.this, permission + " 权限未申请成功", Toast.LENGTH_LONG).show();

        }
    }

    /**
     * 透明状态栏
     *
     * @return
     */
    public boolean translucentStatusBar() {
        return true;
    }
}
