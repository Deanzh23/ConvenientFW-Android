package com.dean.android.framework.convenient.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入layout 的注解
 * <p>
 * 替代setContentView设置layout
 * <p>
 * Created by Dean on 15/10/10.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {

    int value();
}
