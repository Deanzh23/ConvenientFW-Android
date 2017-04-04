package com.dean.android.framework.convenient.calendar.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日期工具类
 * <p>
 * Created by dean on 2017/4/2.
 */
public class CalendarUtils {

    private static final String TAG = CalendarUtils.class.getSimpleName();

    /**
     * 获取指定日期所在月的第一天是周几
     *
     * @param date
     * @return
     */
    public static int getFirstDayForMonthOnWeek(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d(TAG, "[getFirstDayForMonthOnWeek]--->当月第一天是 周" + week);

        return week;
    }

    /**
     * 获取当前年月字符串
     *
     * @param date
     * @param month
     * @param format
     * @return
     */
    public static String getYearMonthByDate(Date date, int month, SimpleDateFormat format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);

        String yearMonth = format.format(calendar.getTime());

        return yearMonth;
    }

    /**
     * 获取指定日期的前／后 n个月的日期
     *
     * @param date
     * @param monthCount
     * @return
     */
    public static Date moveMonth(Date date, int monthCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthCount);

        return calendar.getTime();
    }

    /**
     * 获取指定日期所在月份的天数
     *
     * @param date
     * @return
     */
    public static int getDayCountOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CANADA);
        calendar.setTime(date);
        int dayCount = calendar.getActualMaximum(Calendar.DATE);
        return dayCount;
    }

    /**
     * 获取指定日期所在月的CalendarItem集合
     *
     * @param date
     * @return
     */
    public static List<Object> getCalendarItemsByDate(Date date) {
        List<Object> monthCalendarItems = new ArrayList<>();
        String[] monthPlanArray = null;
//        if (personShiftVO != null && !TextUtils.isEmpty(personShiftVO.getMonthPlan()))
//            monthPlanArray = personShiftVO.getMonthPlan().split(",");

        int week = getFirstDayForMonthOnWeek(date);

        int dayCount = getDayCountOfMonth(date);
        for (int i = 1, j = 1; i <= dayCount; i++, j++) {
            // 本月第一天是周几（占位）
            if (j < week) {
                monthCalendarItems.add(null);
                i--;
                continue;
            }

//            CalendarItem calendarItem = new CalendarItem(String.valueOf(i));

//            if (monthPlanArray != null) {
//                String monthPlanValue = monthPlanArray[i - 1];
//                if (monthPlanArray != null && !"0".equals(monthPlanValue))
//                    calendarItem.setMark(monthPlanValue);
//            }

            monthCalendarItems.add(i);
        }

        return monthCalendarItems;
    }

    /**
     * 获取指定日期前一个月的CalendarItem集合
     *
     * @param date
     * @return
     */
    public static List<Object> getCalendarItemByDate(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);

        return getCalendarItemsByDate(calendar.getTime());
    }
}
