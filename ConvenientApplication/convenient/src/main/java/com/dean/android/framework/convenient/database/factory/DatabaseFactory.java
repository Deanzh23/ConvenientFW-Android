package com.dean.android.framework.convenient.database.factory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.dean.android.framework.convenient.database.FrameworkDatabaseHelper;
import com.dean.android.framework.convenient.database.Selector;
import com.dean.android.framework.convenient.database.annotation.Column;
import com.dean.android.framework.convenient.database.annotation.PrimaryKey;
import com.dean.android.framework.convenient.database.listener.DatabaseVersionUpdateListener;
import com.dean.android.framework.convenient.database.type.TableUtil;
import com.dean.android.framework.convenient.object.ObjectUtil;
import com.dean.android.framework.convenient.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工厂类
 * <p/>
 * 1.维护数据库名称
 * 2.维护数据表
 * 3.维护数据库版本
 * <p/>
 * Created by Dean on 16/5/24.
 */
public class DatabaseFactory {

    // 数据库版本（默认是1）
    private static int sDatabaseVersionCode = 1;
    // 数据库名
    private static String sDatabaseName;
    // 数据库操作Helper
    private static FrameworkDatabaseHelper sFrameworkDatabaseHelper;

    /**
     * 设置 数据库配置参数
     *
     * @param context
     * @param databaseName
     * @param versionCode
     * @param databaseVersionUpdateListener
     */
    public void setDatabaseConfig(Context context, String databaseName, int versionCode, DatabaseVersionUpdateListener databaseVersionUpdateListener) {
        sDatabaseVersionCode = versionCode;
        sDatabaseName = databaseName;

        /** 创建数据库操作Helper类 **/
        createDatabaseHelper(context);

        /** 检查数据库版本更新 **/
        checkVersionUpdate(versionCode, databaseVersionUpdateListener);
    }

    /**
     * 检查数据库版本更新
     *
     * @param versionCode
     * @param databaseVersionUpdateListener
     */
    private void checkVersionUpdate(int versionCode, DatabaseVersionUpdateListener databaseVersionUpdateListener) {
        int oldVersionCode = getOldVersionCode();

        if (sDatabaseVersionCode > oldVersionCode)
            if (databaseVersionUpdateListener != null)
                databaseVersionUpdateListener.update(oldVersionCode, versionCode);
    }

    /**
     * 获取当前数据库版本号
     *
     * @return
     */
    private int getOldVersionCode() {
        int testVersionCode = 0;
        return testVersionCode;
    }

    /**
     * 创建数据库操作Helper类
     *
     * @param context
     */
    private void createDatabaseHelper(Context context) {
        sFrameworkDatabaseHelper = new FrameworkDatabaseHelper(context, sDatabaseName, null, sDatabaseVersionCode);
    }

    /**
     * 创建表
     *
     * @param ormClass 映射表结构
     * @return 是否创建成功
     */
    public void createTableIfNotExists(Class ormClass) {
        StringBuilder createTableSQLBuilder = new StringBuilder();
        createTableSQLBuilder.append("CREATE TABLE IF NOT EXISTS");

        // 表名
        createTableSQLBuilder.append(" " + TableUtil.getTableName(ormClass) + " (");

        Field[] fields = getFields(ormClass);

        int length = fields.length;
        if (length == 0)
            return;

        // 字段
        for (Field field : fields) {
            if (field.getAnnotation(PrimaryKey.class) != null || field.getAnnotation(Column.class) != null) {
                boolean isPrimaryKey = field.getAnnotation(PrimaryKey.class) != null;

                createTableSQLBuilder.append(" " + field.getName() + " "
                        + TableUtil.getTableType(field.getType()) + (isPrimaryKey ? (" " + TableUtil.COLUMN_NOT_NULL) : " ") + " ,");
            }
        }

        // 主键
        for (Field field : fields) {
            PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);

            if (primaryKeyAnnotation == null)
                continue;

            if (!createTableSQLBuilder.toString().contains("PRIMARY KEY "))
                createTableSQLBuilder.append(" PRIMARY KEY ( " + field.getName());
            else
                createTableSQLBuilder.append(" , " + field.getName());
        }

        createTableSQLBuilder.append(" ) ");

        createTableSQLBuilder.append(" )");
        Log.d(DatabaseFactory.class.getName(), createTableSQLBuilder.toString());

        try {
            sFrameworkDatabaseHelper.getWritableDatabase().execSQL(createTableSQLBuilder.toString());
        } catch (Exception e) {
            Log.e(DatabaseFactory.class.getName(), "[createTableIfNotExists]--->创建表 " + ormClass.getSimpleName() + " 失败！");
        }
    }

    /**
     * 查找单个记录（指定主键的，主键包含在实例对象中）
     *
     * @param ormObject 实例对象
     * @param <T>       类型
     * @return
     */
    public <T> T select(T ormObject, boolean isChangeOriginalObject) {
        Class<? extends Object> ormClass = ormObject.getClass();
        int condition = 0;

        StringBuilder selectSQLBuilder = new StringBuilder();
        selectSQLBuilder.append("SELECT * FROM");

        // 表名
        selectSQLBuilder.append(" " + TableUtil.getTableName(ormClass) + " WHERE");

        Field[] fields = getFields(ormClass);

        if (fields.length == 0)
            return null;

        // 主键
        for (Field field : fields) {
            PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);

            if (primaryKeyAnnotation == null)
                continue;

            if (condition++ == 0)
                selectSQLBuilder.append(" " + field.getName() + " = " + TableUtil.getColumnValue(ormObject, field));
            else
                selectSQLBuilder.append(" AND " + field.getName() + " = " + TableUtil.getColumnValue(ormObject, field));
        }

        // 查询
        Log.d(DatabaseFactory.class.getName(), selectSQLBuilder.toString());
        Cursor cursor = sFrameworkDatabaseHelper.getReadableDatabase().rawQuery(selectSQLBuilder.toString(), null);

        // 构造实例对象
        List<T> dataList = TableUtil.generateOrmObject(ormObject.getClass(), cursor);
        cursor.close();

        // 改变原有实例对象
        if (isChangeOriginalObject) {
            if (dataList != null && dataList.size() > 0)
                try {
                    ObjectUtil.objectCopy(ormObject, dataList.get(0), null);
                    return ormObject;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            else
                return null;
        }

        // 不改变原有实例对象
        return ormObject;
    }

    /**
     * 查询全部记录
     *
     * @param ormClass 指定的表
     * @param <T>      类型
     * @return
     */
    public <T> List<T> selectAll(Class ormClass, Selector selector) {
        StringBuilder selectSQLBuilder = new StringBuilder();
        selectSQLBuilder.append("SELECT * FROM");

        // 表名
        selectSQLBuilder.append(" " + TableUtil.getTableName(ormClass));

        if (selector != null && selector.getSQL() != null) {
            selectSQLBuilder.append(" WHERE ");
            selectSQLBuilder.append(selector.getSQL());
        }

        Cursor cursor;

        try {
            // 查询
            Log.d(DatabaseFactory.class.getName(), selectSQLBuilder.toString());
            cursor = sFrameworkDatabaseHelper.getReadableDatabase().rawQuery(selectSQLBuilder.toString(), null);
        } catch (Exception e) {
            // 修改表结构
            editTableStructure(ObjectUtils.instanceFromClass(ormClass));
            // 重新查询
            Log.d(DatabaseFactory.class.getName(), selectSQLBuilder.toString());
            cursor = sFrameworkDatabaseHelper.getReadableDatabase().rawQuery(selectSQLBuilder.toString(), null);
        }

        // 构造实例对象
        List<T> dataList = TableUtil.generateOrmObject(ormClass, cursor);
        cursor.close();


        return dataList;
    }

    /**
     * 插入一条记录
     *
     * @param ormObject
     */
    public void insert(Object ormObject) {
        Class<? extends Object> ormClass = ormObject.getClass();
        List<String> columnList = new ArrayList<>();
        List<Object> columnValueList = new ArrayList<>();

        Field[] fields = getFields(ormClass);

        if (fields.length == 0)
            return;

        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations == null || annotations.length <= 0)
                continue;

            Annotation annotation = annotations[0];

            if (annotation instanceof PrimaryKey || annotation instanceof Column) {
                columnList.add(field.getName());
                columnValueList.add(TableUtil.getColumnValue(ormObject, field));
            }
        }

        StringBuilder insertSQLBuilder = new StringBuilder();
        // 表名
        insertSQLBuilder.append("INSERT INTO " + TableUtil.getTableName(ormClass) + " (");

        int columnCount = columnList.size();
        // 字段
        for (int i = 0; i < columnCount; i++) {
            String column = columnList.get(i);
            insertSQLBuilder.append(" " + column);

            if (i != columnCount - 1)
                insertSQLBuilder.append(" ,");
        }

        insertSQLBuilder.append(" ) VALUES ( ");

        // 字段赋值
        for (int i = 0; i < columnCount; i++) {
            Object value = columnValueList.get(i);
            insertSQLBuilder.append(value);

            if (i != columnCount - 1)
                insertSQLBuilder.append(" ,");
        }

        insertSQLBuilder.append(" )");

        Log.d(DatabaseFactory.class.getName(), insertSQLBuilder.toString());
        try {
            sFrameworkDatabaseHelper.getWritableDatabase().execSQL(insertSQLBuilder.toString());
        } catch (SQLiteConstraintException e) {
            Log.e(DatabaseFactory.class.getSimpleName(), "[insert]--->" + e.getMessage());

            // 修改表结构
            editTableStructure(ormObject);
            // 重新插入
            insert(ormObject);
        }
    }

    /**
     * 更新单个
     *
     * @param ormObject
     */
    public void update(Object ormObject) {
        // 删除原有记录
        delete(ormObject);
        // 插入新记录
        insert(ormObject);
    }

    /**
     * 删除单个
     *
     * @param ormObject
     */
    public void delete(Object ormObject) {
        Class<? extends Object> ormClass = ormObject.getClass();
        int condition = 0;

        StringBuilder deleteSQLBuilder = new StringBuilder();
        deleteSQLBuilder.append("DELETE FROM");

        // 表名
        deleteSQLBuilder.append(" " + TableUtil.getTableName(ormClass) + " WHERE");

        Field[] fields = getFields(ormClass);

        if (fields.length == 0)
            return;

        // 字段
        for (Field field : fields) {
            PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);

            if (primaryKeyAnnotation == null)
                continue;

            if (condition++ == 0)
                deleteSQLBuilder.append(" " + field.getName() + " = " + TableUtil.getColumnValue(ormObject, field));
            else
                deleteSQLBuilder.append(" AND " + field.getName() + " = " + TableUtil.getColumnValue(ormObject, field));
        }

        // 删除
        Log.d(DatabaseFactory.class.getName(), deleteSQLBuilder.toString());
        sFrameworkDatabaseHelper.getWritableDatabase().execSQL(deleteSQLBuilder.toString());
    }

    /**
     * 删除全部
     *
     * @param ormClass
     */
    public void deleteAll(Class ormClass, Selector selector) {
        StringBuilder deleteSQLBuilder = new StringBuilder();
        deleteSQLBuilder.append("DELETE FROM");

        // 表名
        deleteSQLBuilder.append(" " + TableUtil.getTableName(ormClass));

        if (selector != null && selector.getSQL() != null) {
            deleteSQLBuilder.append(" WHERE ");
            deleteSQLBuilder.append(selector.getSQL());
        }

        // 删除
        Log.d(DatabaseFactory.class.getName(), deleteSQLBuilder.toString());
        sFrameworkDatabaseHelper.getWritableDatabase().execSQL(deleteSQLBuilder.toString());
    }

    /**
     * 删除表
     *
     * @param ormClass
     */
    public void dropTable(Class ormClass) {
        StringBuilder dropSQLBuilder = new StringBuilder();
        dropSQLBuilder.append("DROP FROM");
        // 表名
        dropSQLBuilder.append(" " + TableUtil.getTableName(ormClass));

        // 删除
        Log.d(DatabaseFactory.class.getName(), dropSQLBuilder.toString());
        sFrameworkDatabaseHelper.getWritableDatabase().execSQL(dropSQLBuilder.toString());
    }

    /**
     * 获取子类和父类的属性
     *
     * @param ormClass
     * @return
     */
    private Field[] getFields(Class ormClass) {
        Field[] childFields = ormClass.getDeclaredFields();
        Field[] superFields = ormClass.getSuperclass().getDeclaredFields();

        Field[] fields = new Field[childFields.length + superFields.length];

        int index = 0;
        for (Field field : childFields)
            fields[index++] = field;
        for (Field field : superFields)
            fields[index++] = field;

        return fields;
    }

    /**
     * 修改表结构（当表中字段比Bean中定义当少时，添加表字段）
     *
     * @param ormObject
     */
    private static void editTableStructure(Object ormObject) {
        Class tClass = ormObject.getClass();
        Field[] fields = tClass.getFields();

        if (fields == null && fields.length <= 0)
            return;

        for (Field field : fields) {
            // 如果包含数据库注解
            if (containsDBAnnotation(field))
                // 如果不存在于数据库中
                if (!isExistField(TableUtil.getTableName(tClass), field.getName())) {
                    String sql = "ALTER TABLE " + TableUtil.getTableName(tClass) + " ADD COLUMN " + field.getName() + " " + TableUtil.getTableType(field.getType());
                    Log.d(DatabaseFactory.class.getSimpleName(), "[editTableStructure]-->" + sql);

                    sFrameworkDatabaseHelper.getWritableDatabase().execSQL(sql);
                }
        }
    }

    /**
     * 指定表中的指定字段是否存在
     *
     * @param strTableName
     * @param strFieldName
     * @return
     */
    private static boolean isExistField(String strTableName, String strFieldName) {
        StringBuilder builder = new StringBuilder();
        builder.append("name = '").append(strTableName).append("' AND sql LIKE '%").append(strFieldName).append("%'");
        Cursor cursor = null;
        try {
            cursor = sFrameworkDatabaseHelper.getWritableDatabase().query("sqlite_master", null, builder.toString(), null, null,
                    null, null);

            return cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    /**
     * 包含数据库注解
     *
     * @param field
     * @return
     */
    private static boolean containsDBAnnotation(Field field) {
        Annotation[] annotations = field.getAnnotations();

        if (annotations == null || annotations.length <= 0)
            return false;

        if (field.getAnnotation(PrimaryKey.class) != null || field.getAnnotation(Column.class) != null)
            return true;

        return false;
    }

}
