package com.dean.android.famework.convenient.permission;

import android.os.Build;

/**
 * Android SDK version 工具类
 * <p>
 * 当前针对6.0的权限问题
 * <p>
 * Created by Dean on 16/5/11.
 */
public class SdkVersionUtil {

    public static boolean isVersionAfterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
