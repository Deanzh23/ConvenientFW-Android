package com.dean.android.famework.convenient.permission.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.dean.android.famework.convenient.permission.SdkVersionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限 工具类
 * <p>
 * 主要针对 [android 6.0)以后新的权限特性
 * <p>
 * 1.SD卡写入权限
 * <p>
 * Created by Dean on 16/5/11.
 */
public class PermissionsUtil {

    /**
     * 权限 Action
     */
    public static final int ACTION_DEFAULT_PERMISSION_CHECK = 9999;

    /**
     * 权限检查
     *
     * @param activity
     * @return 有/没有（申请） 权限
     */
    public static boolean checkPermission(Activity activity, String permission) {
        boolean hasPermission;
        /** 权限检查 **/
        if (!SdkVersionUtil.isVersionAfterM())
            hasPermission = true;
        else
            hasPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        return hasPermission;
    }

    /**
     * 申请权限
     *
     * @param activity
     * @param permissionArray 需要的权限集合
     */
    public static void requestPermission(Activity activity, String[] permissionArray) {
        // 如果没有需要的权限，则略过
        if (permissionArray == null || permissionArray.length <= 0)
            return;

        List<String> needRequestPermissionList = new ArrayList<>();

        // 权限检查
        if (SdkVersionUtil.isVersionAfterM()) {
            // 遍历全部需要的权限，将 需要，但没有申请 的权限加入到"申请权限集合"中
            for (String permission : permissionArray) {
                boolean hasPermission = checkPermission(activity, permission);

                if (!hasPermission)
                    needRequestPermissionList.add(permission);
            }

            // 进行权限申请
            if (needRequestPermissionList.size() > 0) {
                String[] requestPermissionArray = needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]);

                ActivityCompat.requestPermissions(activity, requestPermissionArray, ACTION_DEFAULT_PERMISSION_CHECK);
            }
        }
    }

    /**
     * 是否成功获取了申请的权限
     * <p>
     * 在 onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) 中使用
     * <p>
     *
     * @param grantResults
     * @return
     */
    public static boolean requestPermissionsSuccess(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

}
