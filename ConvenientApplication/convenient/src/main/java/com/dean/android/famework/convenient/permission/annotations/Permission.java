package com.dean.android.famework.convenient.permission.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解 申请相关权限
 * <p>
 * 1.SD卡写入权限
 * <p>
 * Created by Dean on 16/5/12.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    String[] value();

}
