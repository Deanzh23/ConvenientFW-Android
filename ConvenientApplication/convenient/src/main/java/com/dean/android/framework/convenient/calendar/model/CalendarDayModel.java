package com.dean.android.framework.convenient.calendar.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dean on 2017/7/24.
 */
public class CalendarDayModel implements Serializable {

    private Date date;
    /**
     * 日期
     */
    private int day;
    /**
     * 虚日期（上一个月或者下一个月显示在此月份日历中的日期）
     */
    private boolean nominalDate = false;
    /**
     * 划掉的日期
     */
    private boolean elideDate = false;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isNominalDate() {
        return nominalDate;
    }

    public void setNominalDate(boolean nominalDate) {
        this.nominalDate = nominalDate;
    }

    public boolean isElideDate() {
        return elideDate;
    }

    public void setElideDate(boolean elideDate) {
        this.elideDate = elideDate;
    }
}
