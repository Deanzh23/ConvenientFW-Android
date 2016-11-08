package com.dean.android.famework.convenient.view;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入widget 的注解(xml中的id与控件声明名称 相同)
 * <p>
 * 替代findViewById获取指定widgets
 * <p>
 * Created by Dean on 16/6/3.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
}
