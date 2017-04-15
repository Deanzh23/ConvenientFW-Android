package com.dean.android.framework.convenient.screen;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * 屏幕工具类
 * <p>
 * Created by dean on 2017/4/3.
 */
public class ScreenUtils {

    /**
     * 获取View在屏幕中的位置
     *
     * @param view
     * @return
     */
    public static int[] getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int x = location[0];
        int y = location[1];
        Log.d(ScreenUtils.class.getSimpleName(), "x:" + x + "  y:" + y);
        Log.d(ScreenUtils.class.getSimpleName(),
                "角left：" + view.getLeft() + "  角right：" + view.getRight() + "  角top：" + view.getTop() + "  角bottom：" + view.getBottom());

        return location;
    }

    /**
     * 获取屏幕参数
     *
     * @param context
     * @return
     */
    public static Display getScreenDisplay(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        return windowManager.getDefaultDisplay();
    }

    /**
     * 获取指定dp相应的px值
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
