package com.dean.android.famework.convenient.toast;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast消息工具类
 * <p>
 * 新toast弹出会顶掉上一个正在显示的toast
 * <p>
 * Created by Dean on 16/3/4.
 */
public class ToastUtil {

    private static Toast toast;

    /**
     * 相较于屏幕的显示位置
     * 0:默认正常的
     * 1:屏幕中央
     * 2:屏幕中央偏上
     */
    public static final int LOCATION_DEFAULT = 0;
    public static final int LOCATION_MIDDLE = 1;
    public static final int LOCATION_TOP = 2;

    /**
     * 显示Toast消息
     *
     * @param context
     * @param message
     * @param time
     * @param location
     */
    public static void showToast(Context context, String message, int time, int location) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, message, time);
        switch (location) {
            case 1:
                toast.setGravity(Gravity.CENTER, 0, 0);
                break;
            case 2:
                Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
                toast.setGravity(Gravity.TOP, 0, (int) (display.getHeight() / 4.0));
                break;
            default:
                break;
        }
        toast.show();
    }

    /**
     * 显示Toast消息
     * <p>
     * 不指定显示位置，默认显示在底部
     *
     * @param context
     * @param message
     * @param time
     */
    public static void showToast(Context context, String message, int time) {
        showToast(context, message, time, LOCATION_DEFAULT);
    }

    /**
     * 显示Toast消息
     * <p>
     * 不指定显示位置，默认显示在底部
     * <p>
     * 不指定显示时长，默认短时间
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT, LOCATION_DEFAULT);
    }

}
