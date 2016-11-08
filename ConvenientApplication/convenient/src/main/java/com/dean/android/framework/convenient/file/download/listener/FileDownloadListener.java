package com.dean.android.framework.convenient.file.download.listener;

/**
 * 文件下载监听器
 * <p>
 * Created by Dean on 2016/10/18.
 */
public interface FileDownloadListener {

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
