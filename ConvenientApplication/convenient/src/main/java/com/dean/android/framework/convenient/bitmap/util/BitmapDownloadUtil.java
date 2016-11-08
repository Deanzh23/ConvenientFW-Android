package com.dean.android.framework.convenient.bitmap.util;

import android.app.Activity;
import android.text.TextUtils;

import com.dean.android.framework.convenient.bitmap.listener.BitmapDownloadListener;
import com.dean.android.framework.convenient.bitmap.runnable.BitmapDownloadRunnable;

import java.util.HashSet;
import java.util.Set;

/**
 * 图片下载工具类
 * <p>
 * Created by Dean on 16/5/11.
 */
public class BitmapDownloadUtil {

    /**
     * 转载正在下载的URL（目的：避免重复下载）
     */
    private static Set<String> mDownloadUrlSet = new HashSet<>();

    /**
     * 图片下载并保存在本地指定位置（指定文件名）
     * <p>
     * 需要 <uses-permission android:name="android.permission.INTERNET"/> 权限
     * <p>
     *
     * @param activity
     * @param url                    下载地址
     * @param localPath              本地存储路径
     * @param pictureName            图片文件名
     * @param bitmapDownloadListener 下载状态监听器
     */
    public static void download(Activity activity, String url, String localPath, String pictureName, final BitmapDownloadListener bitmapDownloadListener) {
        /** URL 可用性检查 **/
        if (TextUtils.isEmpty(url)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bitmapDownloadListener.error("download address is \"\" or NULL");
                }
            });
            return;
        }
        /** URL重复性检查 **/
        if (mDownloadUrlSet.contains(url)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bitmapDownloadListener.error("this picture is downloading...");
                }
            });
            return;
        }

        /** 开始下载 **/
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bitmapDownloadListener.start();
            }
        });
        new Thread(new BitmapDownloadRunnable(activity, url, localPath, pictureName, bitmapDownloadListener)).start();
    }

}
