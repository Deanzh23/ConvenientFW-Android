package com.dean.android.framework.convenient.json;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * JSON 工具类
 * <p>
 * 1.JsonObject 与 Object 相互转换
 * <p>
 * Created by Dean on 15/10/26.
 */
public class JSONUtil {

    /**
     * JSON 转 Object
     *
     * @param from
     * @param to
     */
    public static void json2Object(JSONObject from, Object to) {
        Class<? extends Object> toClz = to.getClass();
        Field[] fields = toClz.getDeclaredFields();

        if (fields == null || fields.length == 0)
            return;

        int injectCount = 0;
        for (Field field : fields) {
            ValueInject valueInject = field.getAnnotation(ValueInject.class);
            if (valueInject != null)
                injectCount++;
        }

        if (injectCount == 0)
            json2Object_v1(from, to);
        else
            json2Object_v2(from, to);
    }

    /**
     * object 转 json
     *
     * @param object
     * @return
     */
    public static JSONObject object2Json(Object object) {
        JSONObject json = new JSONObject();
        Class<? extends Object> ormClass = object.getClass();

        Field[] fields = ormClass.getDeclaredFields();

        if (fields.length <= 0)
            return null;

        int injectCount = 0;
        for (Field field : fields) {
            ValueInject valueInject = field.getAnnotation(ValueInject.class);

            if (valueInject != null)
                injectCount++;
        }

        return injectCount == 0 ? object2Json_v1(object) : object2Json_v2(object);
    }

    /**
     * object 2 json v1
     *
     * @param object
     * @return
     */
    private static JSONObject object2Json_v1(Object object) {
        JSONObject json = new JSONObject();


        Class<? extends Object> ormClass = object.getClass();
        Field[] fields = ormClass.getDeclaredFields();

        for (Field field : fields) {

            try {
                String fieldName = field.getName();
                String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                Method getMethod = ormClass.getDeclaredMethod(methodName);
                Object result = getMethod.invoke(object);

                json.put(fieldName, result == null ? JSONObject.NULL : result);
            } catch (Exception e) {
                continue;
            }
        }

        return json;
    }

    /**
     * object 2 json v2
     *
     * @param object
     * @return
     */
    private static JSONObject object2Json_v2(Object object) {
        JSONObject json = new JSONObject();

        Class<? extends Object> ormClass = object.getClass();
        Field[] fields = ormClass.getDeclaredFields();

        for (Field field : fields) {
            ValueInject valueInject = field.getAnnotation(ValueInject.class);

            if (valueInject == null)
                continue;

            try {
                // Json字段名
                String jsonFieldName = valueInject.value();
                // Object字段名
                String objectFieldName = field.getName();

                String methodName = "get" + objectFieldName.substring(0, 1).toUpperCase() + objectFieldName.substring(1);
                Method getMethod = ormClass.getDeclaredMethod(methodName);
                Object result = getMethod.invoke(object);

                json.put(jsonFieldName, result == null ? JSONObject.NULL : result);
            } catch (Exception e) {
                continue;
            }
        }

        return json;
    }


    /**
     * json 2 object version1
     *
     * @param from
     * @param to
     */
    private static void json2Object_v1(JSONObject from, Object to) {
        Class<? extends Object> toClz = to.getClass();
        Field[] fields = toClz.getDeclaredFields();

        if (fields == null || fields.length == 0)
            return;

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();

            Field toField;
            try {
                toField = toClz.getDeclaredField(fieldName);

                if (fields[i].getType() == toField.getType()) {
                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method setMethod = toClz.getDeclaredMethod(setMethodName, fields[i].getType());

                    String typeName = fields[i].getType().getSimpleName();

                    // 字符串
                    if (String.class.getSimpleName().equals(typeName)) {
                        String strValue = from.getString(fieldName);
                        setMethod.invoke(to, "null".equals(strValue) ? null : strValue);
                    }
                    // float
                    else if (float.class.getSimpleName().equals(typeName)) {
                        setMethod.invoke(to, (float) from.getDouble(fieldName));
                    }
                    // int
                    else if (int.class.getSimpleName().equals(typeName)) {
                        setMethod.invoke(to, from.getInt(fieldName));
                    }
                    // boolean
                    else if (boolean.class.getSimpleName().equals(typeName)) {
                        setMethod.invoke(to, from.getBoolean(fieldName));
                    }
                    // long
                    else if (long.class.getSimpleName().equals(typeName)) {
                        setMethod.invoke(to, from.getLong(fieldName));
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * json 2 object version2
     *
     * @param from
     * @param to
     */
    private static void json2Object_v2(JSONObject from, Object to) {
        Class<? extends Object> toClz = to.getClass();
        Field[] fields = toClz.getDeclaredFields();

        if (fields == null || fields.length == 0)
            return;

        for (Field field : fields) {
            ValueInject valueInject = field.getAnnotation(ValueInject.class);

            if (valueInject == null)
                continue;

            try {
                // Json的字段名
                String jsonFieldName = valueInject.value();
                // Object的字段名
                String objectFieldName = field.getName();

                /** 开始处理 **/

                String setMethodName = "set" + objectFieldName.substring(0, 1).toUpperCase() + objectFieldName.substring(1);
                Method setMethod = toClz.getDeclaredMethod(setMethodName, field.getType());
                String typeName = field.getType().getSimpleName();

                // 字符串
                if (String.class.getSimpleName().equals(typeName)) {
                    String strValue = from.getString(jsonFieldName);
                    setMethod.invoke(to, "null".equals(strValue) ? null : strValue);
                }
                // float
                else if (float.class.getSimpleName().equals(typeName)) {
                    setMethod.invoke(to, (float) from.getDouble(jsonFieldName));
                }
                // int
                else if (int.class.getSimpleName().equals(typeName)) {
                    setMethod.invoke(to, from.getInt(jsonFieldName));
                }
                // boolean
                else if (boolean.class.getSimpleName().equals(typeName)) {
                    setMethod.invoke(to, from.getBoolean(jsonFieldName));
                }
                // long
                else if (long.class.getSimpleName().equals(typeName)) {
                    setMethod.invoke(to, from.getLong(jsonFieldName));
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

}
