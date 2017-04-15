package com.dean.android.fw.convenient.ui.view.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 可以展开的日历控件
 * <p>
 * Created by dean on 2017/4/12.
 */
public class CalendarView extends FrameLayout {

    private Context context;

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
    }


}
