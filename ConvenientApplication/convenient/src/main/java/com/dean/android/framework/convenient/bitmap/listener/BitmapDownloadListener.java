package com.dean.android.framework.convenient.bitmap.listener;

/**
 * 图片下载监听器
 * <p/>
 * Created by Dean on 16/5/11.
 */
public interface BitmapDownloadListener {

    /**
     * 开始下载
     */
    void start();

    /**
     * 下载成功
     */
    void success();

    /**
     * 下载失败
     */
    void error(String error);

    /**
     * 下载进度（单位：byte）
     *
     * @param current 当前下载量
     * @param total   总大小
     */
    void progress(int current, int total);

}
