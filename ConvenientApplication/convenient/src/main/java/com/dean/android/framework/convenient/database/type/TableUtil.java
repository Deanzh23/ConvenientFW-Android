package com.dean.android.framework.convenient.database.type;

import android.database.Cursor;
import android.util.Log;

import com.dean.android.framework.convenient.database.annotation.Table;
import com.dean.android.framework.convenient.database.util.DatabaseUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dean on 16/5/24.
 */
public class TableUtil {

    public static final String COLUMN_NOT_NULL = "NOT NULL";

    /**
     * 获取表名
     *
     * @param ormClass
     * @return
     */
    public static String getTableName(Class<? extends Object> ormClass) {
        Table table = ormClass.getAnnotation(Table.class);
        return table == null ? ormClass.getSimpleName() : table.value();
    }

    /**
     * 获取指定属性的值
     *
     * @param ormObject
     * @param field
     * @return
     */
    public static Object getColumnValue(Object ormObject, Field field) {
        Class<? extends Object> ormClass = ormObject.getClass();
        String columnName = field.getName();
        String getMethodName;
        Method getMethod;

        boolean isBooleanType = "boolean".equals(field.getType().getSimpleName());
        try {
            if (!isBooleanType)
                getMethodName = "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
            else
                getMethodName = columnName.substring(0, 1) + columnName.substring(1);

            getMethod = ormClass.getDeclaredMethod(getMethodName);
            Object result = getMethod.invoke(ormObject);
            Log.d(TableUtil.class.getSimpleName(), "[insert]--->column is " + columnName + " : value is " + result);

            return getTableTypeValue(result, field.getType());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取数据库表使用类型的值
     *
     * @param object
     * @param typeClass
     * @return
     */
    public static Object getTableTypeValue(Object object, Class typeClass) {
        String typeName = typeClass.getSimpleName().toLowerCase();

        if ("boolean".equals(typeName))
            return (boolean) object ? 1 : 0;
        else if ("string".equals(typeName))
            return object == null ? null : "'" + object + "'";
        else if ("date".equals(typeName)) {
            Date date = (Date) object;
            return date == null ? 0 : ((Date) object).getTime();
        } else
            return object;
    }

    /**
     * 获取数据库表使用类型的值
     *
     * @param object
     * @return
     */
    public static Object getTableTypeValue(Object object) {
        if (object == null)
            return null;
        else
            return getTableTypeValue(object, object.getClass());
    }

    /**
     * 根据查询结果生成相应对象
     *
     * @param ormClass
     * @param cursor
     * @return
     */
    public static <T> List<T> generateOrmObject(Class ormClass, Cursor cursor) {
        /** 没有查询到记录 **/
        if (cursor == null || cursor.getCount() <= 0)
            return null;

        List<T> objects = new ArrayList<>();

        String[] columnNames = cursor.getColumnNames();

        while (cursor.moveToNext()) {
            /** 生成实例对象 **/
            Object object = DatabaseUtil.instanceFromClass(ormClass);

            /** 遍历表结构，为实例对象注入对应的表字段值 **/
            for (String columnName : columnNames) {
                int columnIndex = cursor.getColumnIndex(columnName);

                try {
                    Field field = ormClass.getDeclaredField(columnName);
                    String setMethodName;
                    Method setMethod;

                    if (columnName.startsWith("is"))
                        setMethodName = columnName.replace("is", "set");
                    else if (columnName.startsWith("has"))
                        setMethodName = columnName.replace("has", "set");
                    else
                        setMethodName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);

                    setMethod = ormClass.getDeclaredMethod(setMethodName, field.getType());

                    String typeName = field.getType().getSimpleName().toLowerCase();

                    if ("boolean".equals(typeName))
                        setMethod.invoke(object, cursor.getInt(columnIndex) == 1);
                    else if ("int".equals(typeName))
                        setMethod.invoke(object, cursor.getInt(columnIndex));
                    else if ("float".equals(typeName))
                        setMethod.invoke(object, cursor.getFloat(columnIndex));
                    else if ("double".equals(typeName))
                        setMethod.invoke(object, cursor.getDouble(columnIndex));
                    else if ("string".equals(typeName))
                        setMethod.invoke(object, cursor.getString(columnIndex));
                    else if ("long".equals(typeName))
                        setMethod.invoke(object, cursor.getLong(columnIndex));
                    else if ("date".equals(typeName))
                        setMethod.invoke(object, new Date(cursor.getLong(columnIndex)));

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            objects.add((T) object);
        }

        return objects;
    }

    /**
     * 获取数据库表字段使用的类型声明
     *
     * @param typeClass
     * @return
     */
    public static String getTableType(Class<? extends Object> typeClass) {
        String typeName = typeClass.getSimpleName().toLowerCase();
        String type = null;

        if ("string".equals(typeName))
            type = "TEXT";
        else if ("int".equals(typeName))
            type = "INT";
        else if ("float".equals(typeName))
            type = "FLOAT";
        else if ("double".equals(typeName))
            type = "DOUBLE";
        else if ("boolean".equals(typeName))
            type = "BOOLEAN";
        else if ("date".equals(typeName))
            type = "LONG";

        return type;
    }

}
