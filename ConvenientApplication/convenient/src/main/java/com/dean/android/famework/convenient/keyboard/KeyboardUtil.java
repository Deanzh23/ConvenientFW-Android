package com.dean.android.famework.convenient.keyboard;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘相关操作工具类
 * <p>
 * 1.收起软键盘
 * <p>
 * Created by Dean on 16/5/24.
 */
public class KeyboardUtil {

    /**
     * 收起软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
    }

}
