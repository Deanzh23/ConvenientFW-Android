package com.dean.android.famework.convenient.object;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 对象 工具类
 * <p>
 * Object copy
 * <p>
 * Created by Dean on 15/10/26.
 */
public class ObjectUtil {

    /**
     * Bean拷贝
     *
     * @param toBean   要拷贝到的Bean
     * @param fromBean 被拷贝的Bean
     * @param nameList 过滤的属性名List
     * @throws Exception
     */
    public static void objectCopy(Object toBean, Object fromBean, List<String> nameList) throws Exception {
        Class<? extends Object> sourceClz = fromBean.getClass();
        Class<? extends Object> targetClz = toBean.getClass();

        Field[] fields = sourceClz.getDeclaredFields();

        if (fields.length == 0)
            fields = sourceClz.getSuperclass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            if (nameList != null && nameList.contains(fieldName))
                continue;
            Field targetField;
            try {
                targetField = targetClz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                targetField = targetClz.getSuperclass().getDeclaredField(fieldName);
            }
            if (fields[i].getType() == targetField.getType()) {
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getMethod;
                Method setMethod;
                try {
                    try {
                        getMethod = sourceClz.getDeclaredMethod(getMethodName);
                    } catch (NoSuchMethodException e) {
                        getMethod = sourceClz.getSuperclass().getDeclaredMethod(getMethodName);
                    }
                    try {
                        setMethod = targetClz.getDeclaredMethod(setMethodName, fields[i].getType());
                    } catch (NoSuchMethodException e) {
                        setMethod = targetClz.getSuperclass().getDeclaredMethod(setMethodName, fields[i].getType());
                    }
                    Object result = getMethod.invoke(fromBean);
                    setMethod.invoke(toBean, result);
                } catch (SecurityException e) {
                    e.printStackTrace();

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();

                } catch (IllegalAccessException e) {
                    e.printStackTrace();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                throw new Exception("同名属性类型不匹配！");
            }
        }
    }

}
