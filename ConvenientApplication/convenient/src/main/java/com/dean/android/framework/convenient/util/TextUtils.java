package com.dean.android.framework.convenient.util;

import android.support.annotation.Nullable;

/**
 * 字符串工具类
 * <p>
 * Created by dean on 2017/5/25.
 */
public class TextUtils {

    /**
     * 是否为空
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(@Nullable CharSequence text) {
        return android.text.TextUtils.isEmpty(text) || "null".equals(text);
    }

}
