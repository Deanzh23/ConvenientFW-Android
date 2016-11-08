package com.dean.android.framework.convenient.database.util;

import android.content.Context;
import android.util.Log;

import com.dean.android.framework.convenient.database.Selector;
import com.dean.android.framework.convenient.database.factory.DatabaseFactory;
import com.dean.android.framework.convenient.database.listener.DatabaseVersionUpdateListener;

import java.util.List;

/**
 * 数据库相关工具类
 * <p/>
 * * 1.维护数据库名称
 * 2.维护数据表
 * 3.维护数据库版本
 * <p/>
 * Created by Dean on 16/5/24.
 */
public class DatabaseUtil {

    private static DatabaseFactory mDatabaseFactory = new DatabaseFactory();

    /**
     * 设置 数据库配置参数
     *
     * @param context
     * @param databaseName
     * @param versionCode
     * @param databaseVersionUpdateListener
     */
    public static void init(Context context, String databaseName, int versionCode, DatabaseVersionUpdateListener databaseVersionUpdateListener) {
        mDatabaseFactory.setDatabaseConfig(context, databaseName, versionCode, databaseVersionUpdateListener);
    }

    /**
     * 保存 or 更新 数据
     *
     * @param ormObject
     */
    public static <T> void saveOrUpdate(final T ormObject) {
        /** 创建表 **/
        mDatabaseFactory.createTableIfNotExists(ormObject.getClass());

        /** 保存或更新操作 **/
        Object dbOrmObject = mDatabaseFactory.select(ormObject, false);
        if (dbOrmObject == null)
            mDatabaseFactory.insert(ormObject);
        else
            mDatabaseFactory.update(ormObject);
    }

    /**
     * 查询数据（如果没有具体查询条件，则主键属性必须有值）
     *
     * @param ormObject
     * @param <T>
     */
    public static <T> void find(final T ormObject) {
        /** 创建表 **/
        mDatabaseFactory.createTableIfNotExists(ormObject.getClass());
        // 查询并通过接口返回查询结果
        mDatabaseFactory.select(ormObject, true);
    }

    /**
     * 按指定条件查询单个数据
     *
     * @param ormClass
     * @param selector
     * @param <T>
     * @return
     */
    public static <T> T find(Class<T> ormClass, Selector selector) {
        /** 创建表 **/
        mDatabaseFactory.createTableIfNotExists(ormClass);
        // 查询并返回结果
        List<T> list = mDatabaseFactory.selectAll(ormClass, selector);

        return list == null ? null : list.get(0);
    }

    /**
     * 查询全部数据
     *
     * @param ormClass
     * @param selector
     * @param <T>
     */
    public static <T> List<T> findAll(final Class<T> ormClass, final Selector selector) {
        /** 创建表 **/
        mDatabaseFactory.createTableIfNotExists(ormClass);
        // 查询并返回结果
        return mDatabaseFactory.selectAll(ormClass, selector);
    }

    /**
     * 删除一个
     *
     * @param object
     */
    public static void delete(Object object) {
        /** 创建表 **/
        mDatabaseFactory.createTableIfNotExists(object.getClass());

        mDatabaseFactory.delete(object);
    }

    /**
     * 删除全部
     *
     * @param ormClass
     * @param selector
     */
    public static void deleteAll(final Class ormClass, final Selector selector) {
        /** 创建表 **/
        mDatabaseFactory.createTableIfNotExists(ormClass);

        mDatabaseFactory.deleteAll(ormClass, selector);
    }

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
