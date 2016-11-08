package com.dean.android.framework.convenient.view.util;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.dean.android.framework.convenient.activity.ConvenientActivity;
import com.dean.android.framework.convenient.view.ContentView;
import com.dean.android.framework.convenient.view.OnClick;
import com.dean.android.framework.convenient.view.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 初始化Activity和Fragment的控制器
 * <p>
 * Created by Dean on 2016/11/7.
 */
public class BindingViewController {

    /**
     * Activity相关注入
     *
     * @param activity
     * @param viewDataBinding
     * @param <T>
     * @return
     */
    public static <T extends ViewDataBinding> T inject(ConvenientActivity activity, T viewDataBinding) {
        viewDataBinding = injectContentView(activity, viewDataBinding);
        injectWidget(activity, viewDataBinding);
        injectOnClick(activity, viewDataBinding);

        return viewDataBinding;
    }

    /**
     * Activity注入ContentView
     *
     * @param activity
     * @param viewDataBinding
     * @param <T>
     * @return
     */
    private static <T extends ViewDataBinding> T injectContentView(ConvenientActivity activity, T viewDataBinding) {
        Class<? extends Activity> activityClass = activity.getClass();
        ContentView contentViewAnnotation = activityClass.getAnnotation(ContentView.class);

        if (contentViewAnnotation != null) {
            int resourcesId = contentViewAnnotation.value();

            viewDataBinding = DataBindingUtil.setContentView(activity, resourcesId);
        }

        return viewDataBinding;
    }

    /**
     * Activity注入实例化控件
     *
     * @param activity
     * @param viewDataBinding
     * @param <T>
     */
    private static <T extends ViewDataBinding> void injectWidget(ConvenientActivity activity, T viewDataBinding) {
        Class<? extends Activity> activityClass = activity.getClass();
        Field[] fields = activityClass.getDeclaredFields();

        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                ViewInject viewInjectAnnotation = field.getAnnotation(ViewInject.class);

                if (viewInjectAnnotation == null)
                    continue;

                String fieldName = field.getName();
                int viewId = activity.getResources().getIdentifier(fieldName, "id", activity.getPackageName());

                try {
                    Object view = viewDataBinding.getRoot().findViewById(viewId);
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    /**
     * Activity注入OnClick事件
     *
     * @param activity
     * @param viewDataBinding
     * @param <T>
     */
    private static <T extends ViewDataBinding> void injectOnClick(final ConvenientActivity activity, T viewDataBinding) {
        Class<? extends Activity> activityClass = activity.getClass();
        Method[] methods = activityClass.getDeclaredMethods();

        if (methods != null && methods.length > 0) {
            for (final Method method : methods) {
                OnClick onClickAnnotation = method.getAnnotation(OnClick.class);

                if (onClickAnnotation == null)
                    continue;

                method.setAccessible(true);
                int viewId = onClickAnnotation.value();

                View view = viewDataBinding.getRoot().findViewById(viewId);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            method.invoke(activity);
                        } catch (IllegalAccessException e) {
                        } catch (InvocationTargetException e) {
                        }
                    }
                });
            }
        }
    }

}
