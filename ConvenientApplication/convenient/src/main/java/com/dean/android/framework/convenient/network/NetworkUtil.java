package com.dean.android.framework.convenient.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dean.android.framework.convenient.terminal.ping.DefaultPing;
import com.dean.android.framework.convenient.terminal.ping.listener.OnPingListener;

/**
 * 网络相关 工具类
 * <p>
 * 需要 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 权限
 * <p>
 * Created by Dean on 16/7/18.
 */
public class NetworkUtil {

    // 移动网络
    public static final int NETWORK_STATE_MOBILE = 0;
    // WIFI
    public static final int NETWORK_STATE_WIFI = 1;

    /**
     * 是否可以有效的访问公网（此方法隐藏式执行在一个Thread中）
     *
     * @param activity
     * @param ip
     * @param onPingListener
     */
    public static void isConnectionedToPublicNetwork(Activity activity, String ip, OnPingListener onPingListener) {
        DefaultPing defaultPing = new DefaultPing(ip);
        defaultPing.ping(activity, onPingListener);
    }

    /**
     * 获取当前网络状态
     *
     * @return 返回null 表示没有接入到任何网络
     */
    public static Integer getNetworkState(Context context) {
        Integer state = null;

        if (isWifiConnected(context))
            state = NETWORK_STATE_WIFI;
        else if (isMobileConnected(context))
            state = NETWORK_STATE_MOBILE;

        return state;
    }

    /**
     * 当前是否是WIFI连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        return isConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 当前是否是移动网络连接
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        return isConnected(context, ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * 获取当前，指定类型的网络是否连接
     *
     * @param context
     * @param networkType
     * @return
     */
    public static boolean isConnected(Context context, int networkType) {
        if (context == null)
            return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType);

        return networkInfo != null;
    }

}
