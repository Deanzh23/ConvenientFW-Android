package com.dean.android.framework.convenient.util;

import android.util.Log;

import com.dean.android.framework.convenient.database.util.DatabaseUtil;

/**
 * 对象工具类
 * <p>
 * Created by dean on 2017/5/16.
 */
public class ObjectUtils {

    /**
     * 通过反射实例化一个指定类型的对象
     *
     * @param tClass 对象类型Class
     * @return 实例化对象
     */
    public static Object instanceFromClass(Class tClass) {
        Object object = null;
        try {
            object = tClass.newInstance();
            Log.d(DatabaseUtil.class.getSimpleName(), "[getInstanceFromClass]--->实例化 " + tClass.getSimpleName() + " 成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(DatabaseUtil.class.getSimpleName(), "[getInstanceFromClass]--->实例化 " + tClass.getSimpleName() + " 失败！");
        }

        return object;
    }

}
