package com.dean.android.famework.convenient.util;

import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 * <p/>
 * Created by Dean on 16/7/6.
 */
public class SetUtil {

    /**
     * 判断集合是否为空（null and 空集合）
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.size() <= 0)
            return true;
        else
            return false;
    }

    /**
     * 判断集合是否为空（null and 空集合）
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        if (map == null || map.size() <= 0)
            return true;
        else
            return false;
    }

}
