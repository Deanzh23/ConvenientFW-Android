package com.dean.android.framework.convenient.format;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.dean.android.framework.convenient.util.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间格式化工具类
 * <p>
 * Created by dean on 2017/4/24.
 */
public class DateTimeFormatUtils {

    private static final SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy-M-d HH:mm");
    private static final SimpleDateFormat YYYY_MM_DD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 设置格式化日期时间
     *
     * @param timeView
     * @param time
     */
    @BindingAdapter({"time"})
    public static void setTime(TextView timeView, long time) {
        if (timeView != null)
            timeView.setText(DATE_FORMAT_1.format(new Date(time)));
    }

    @BindingAdapter({"yyyyMMddTime"})
    public static void setTime2yyyyMMdd(TextView textView, String strTime) {
        if (textView == null || TextUtils.isEmpty(strTime))
            return;

        try {
            Date date = YYYY_MM_DD_FORMAT.parse(strTime);
            String time = YYYY_MM_DD_FORMAT.format(date);

            textView.setText(time);
        } catch (Exception e) {
            textView.setText("");
        }
    }

}
