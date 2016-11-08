package com.dean.android.framework.convenient.event;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dean.android.framework.convenient.event.listener.TouchListener;

/**
 * 单点触控 and 连点屏蔽
 * <p>
 * Created by Dean on 15/10/12.
 */
public class EventUtil {

    private static final int INIT_STATUS = -1;

    private static int touchViewId = INIT_STATUS;
    private static double clickViewTime = INIT_STATUS;

    public static void clear() {
        touchViewId = INIT_STATUS;
        clickViewTime = INIT_STATUS;
    }

    /**
     * 将点击事件添加到“点击”工具类
     *
     * @param view
     * @param touchInterface
     */
    public static void addClickView(View view, final TouchListener touchInterface) {
        try {
            view.setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            onTouchDown(view);
                        default:
                            return false;
                    }
                }
            });
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (touchViewId == view.getId())
                        onClicked(view, touchInterface);
                }
            });
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    /**
     * “按下”触碰事件处理
     *
     * @param view
     * @return
     */
    private static boolean onTouchDown(View view) {
        Log.i(EventUtil.class.getName(), "onTouchDown()方法执行！");
        if (touchViewId != INIT_STATUS && touchViewId != view.getId())
            return true;
        else
            touchViewId = view.getId();
        return false;
    }

    /**
     * 执行并初始化实际要执行的“事件”
     *
     * @param view
     * @param touchInterface
     */
    private static void onClicked(View view, TouchListener touchInterface) {
        Log.i(EventUtil.class.getName(), "onClick()方法执行！");
        touchInterface.onClicked(view);
        touchViewId = INIT_STATUS;
    }

}
