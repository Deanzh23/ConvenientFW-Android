package com.dean.android.framework.convenient.permission.annotations.util;

import android.app.Activity;

import com.dean.android.framework.convenient.permission.annotations.Permission;
import com.dean.android.framework.convenient.permission.annotations.listener.PermissionAnnotationListener;
import com.dean.android.framework.convenient.permission.util.PermissionsUtil;

/**
 * 权限注解 工具类
 * <p>
 * Created by Dean on 16/5/12.
 */
public class PermissionAnnotationsUtil {

    /**
     * 注入 权限注解
     * <p>
     * 执行位置：Activity的onCreate中
     *
     * @param activity
     * @param permissionAnnotationListener 配合FrameworkActivity
     */
    public static void inject(Activity activity, PermissionAnnotationListener permissionAnnotationListener) {
        /** 从相关activity的Class中获取Permission注解 **/
        Class<? extends Activity> activityClass = activity.getClass();
        Permission permission = activityClass.getAnnotation(Permission.class);
        /** 如果不存在权限注解，则返回 **/
        if (permission == null)
            return;

        /** 获取注解中的内容 **/
        String[] permissionArray = permission.value();
        /** 如果只存在注解而没有实际权限内容，则返回 **/
        if (permissionArray == null || permissionArray.length <= 0)
            return;

        /** 将解析出的权限内容返回给activity **/
        permissionAnnotationListener.success(permissionArray);

        /** 开始申请权限 **/
        PermissionsUtil.requestPermission(activity, permissionArray);
    }

}
