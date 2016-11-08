package com.dean.android.framework.convenient.file.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 * <p>
 * Created by Dean on 2016/10/18.
 */
public class FileUitl {

    /**
     * 检查路径，如果不存在，则创建
     *
     * @param path
     * @param name
     * @throws IOException
     */
    public static void checkFile(String path, String name) throws IOException {
        initPath(path);

        File pathFile = new File(path);
        if (!pathFile.exists())
            pathFile.mkdirs();


        File file = new File(path + (isSeparatorForLastChat(path) ? "" : File.separator) + name);
        if (!file.exists())
            file.createNewFile();
    }

    /**
     * 检查路径，如果不存在，则创建
     *
     * @param path
     */
    public static void checkFile(String path) throws IOException {
        initPath(path);

        File file = new File(path);
        if (!file.exists())
            file.createNewFile();
    }

    /**
     * 如果path == null，则返回sd卡根目录路径
     *
     * @param path
     * @return
     */
    private static String initPath(String path) {
        if (TextUtils.isEmpty(path))
            path = Environment.getExternalStorageDirectory().getPath();

        return path;
    }

    /**
     * 最后一位是分隔符吗
     *
     * @param path
     * @return
     */
    private static boolean isSeparatorForLastChat(String path) {
        initPath(path);

        int length = path.length();
        return File.separatorChar == path.charAt(length);
    }

}
