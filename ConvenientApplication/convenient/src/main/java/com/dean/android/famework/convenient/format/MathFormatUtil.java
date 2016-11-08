package com.dean.android.famework.convenient.format;

import java.text.DecimalFormat;

/**
 * 数学格式化 工具类
 * <p/>
 * 1.byte to kb/M（自动判断，如果大于 1M 则返回单位为"M"，否则返回单位为"KB" ）
 * 2.byte to kb（没有小数）
 * 3.byte to M（保留1位小数）
 * Created by Dean on 16/5/12.
 */
public class MathFormatUtil {

    // 保留1位小数的format
    private static final DecimalFormat sDecimalFormat = new DecimalFormat("##0.0");

    /**
     * byte to kb（没有小数）
     *
     * @param byteNumber
     * @return
     */
    public static String byte2Kb(int byteNumber) {
        return ((int) (byteNumber / 1024.0)) + " KB";
    }

    /**
     * byte to M（保留1位小数）
     *
     * @param byteNumber
     * @return
     */
    public static String byte2Mega(int byteNumber) {
        return sDecimalFormat.format(byteNumber / Math.pow(1024.0, 2)) + " M";
    }

    /**
     * byte to kb/M（自动判断，如果大于 1M 则返回单位为"M"，否则返回单位为"KB" ）
     *
     * @param byteNumber
     * @return
     */
    public static String formatByte(int byteNumber) {
        return byteNumber / Math.pow(1024.0, 2) > 1 ? byte2Mega(byteNumber) : byte2Kb(byteNumber);
    }

}
