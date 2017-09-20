package com.dean.android.framework.convenient.calendar.util;

import android.util.Log;

import com.dean.android.framework.convenient.calendar.model.CalendarDayModel;

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
    public static List<CalendarDayModel> getCalendarItemsByDate(Date date, SimpleDateFormat simpleDateFormat) {
        List<CalendarDayModel> monthCalendarItems = new ArrayList<>();

        int week = getFirstDayForMonthOnWeek(date);
        int dayCount = getDayCountOfMonth(date);

        for (int i = 1; i <= dayCount; i++) {
            // 本月第一天是周几（占位）
            if (week != 7) {
                List<CalendarDayModel> tempCalendarDayModels = new ArrayList<>();

                for (int j = 1; j < week; j++) {
                    Date tempDate = getDateFrontAndBackByDate(date, simpleDateFormat, j);
                    CalendarDayModel nominalCalendarDayModel = new CalendarDayModel();
                    nominalCalendarDayModel.setDate(tempDate);
                    nominalCalendarDayModel.setDay(tempDate.getDay());
                    nominalCalendarDayModel.setNominalDate(true);

                    tempCalendarDayModels.add(nominalCalendarDayModel);
                }

                if (tempCalendarDayModels.size() > 0) {
                    int size = tempCalendarDayModels.size();

                    for (int j = size - 1; j >= 0; j--)
                        monthCalendarItems.add(tempCalendarDayModels.get(j));
                }
            }

            CalendarDayModel calendarDayModel = new CalendarDayModel();
            calendarDayModel.setDate(getDateFrontAndBackByDate(date, simpleDateFormat, i - 1));
            calendarDayModel.setDay(i);

            monthCalendarItems.add(calendarDayModel);
        }

        return monthCalendarItems;
    }

    /**
     * 获取指定日期前一个月的CalendarItem集合
     *
     * @param date
     * @return
     */
    public static List<CalendarDayModel> getCalendarItemByDate(Date date, SimpleDateFormat simpleDateFormat, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);

        return getCalendarItemsByDate(calendar.getTime(), simpleDateFormat);
    }

    /**
     * 获取指定日期 前／后 n天 的日期
     *
     * @param date
     * @param simpleDateFormat
     * @param difference
     * @return
     */
    public static Date getDateFrontAndBackByDate(Date date, SimpleDateFormat simpleDateFormat, int difference) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_MONTH, difference);

        return calendar.getTime();
    }

}
