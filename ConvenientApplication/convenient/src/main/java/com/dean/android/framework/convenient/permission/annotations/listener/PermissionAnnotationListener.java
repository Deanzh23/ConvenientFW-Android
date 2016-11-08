package com.dean.android.framework.convenient.permission.annotations.listener;

/**
 * 权限注解监听器
 * <p>
 * Created by Dean on 16/5/12.
 */
public interface PermissionAnnotationListener {

    /**
     * 获取注解中填充了哪些权限
     *
     * @param permissionArray 权限数组
     */
    void success(String[] permissionArray);

}
