package com.dean.android.framework.convenient.format;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间格式化工具类
 * <p>
 * Created by dean on 2017/4/24.
 */
public class DateTimeFormatUtils {

    private static final SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy-M-d HH:mm");

    /**
     * 设置格式化日期时间
     *
     * @param timeView
     * @param time
     */
    @BindingAdapter({"time"})
    public static void setTime(TextView timeView, long time) {
        SimpleDateFormat simpleDateFormat;

        if (timeView != null)
            timeView.setText(DATE_FORMAT_1.format(new Date(time)));
    }

}
