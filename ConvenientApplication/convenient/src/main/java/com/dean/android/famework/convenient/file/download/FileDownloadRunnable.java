package com.dean.android.famework.convenient.file.download;

import android.app.Activity;

import com.dean.android.famework.convenient.file.download.listener.FileDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件下载线程
 * <p>
 * Created by Dean on 2016/10/18.
 */
public class FileDownloadRunnable implements Runnable {

    protected Activity activity;
    protected String strUrl;
    protected String localPath;
    protected String name;

    protected FileDownloadListener fileDownloadListener;

    public FileDownloadRunnable(Activity activity, String strUrl, String localPath, String name, FileDownloadListener fileDownloadListener) {
        this.activity = activity;
        this.strUrl = strUrl;
        this.localPath = localPath;
        this.name = name;
        this.fileDownloadListener = fileDownloadListener;
    }

    /**
     * 配置连接
     *
     * @param httpURLConnection
     */
    public void configConnection(HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null)
            return;

        /** 连接时长 1分钟 **/
        httpURLConnection.setConnectTimeout(1000 * 60);
        /** 其它配置信息 **/
        httpURLConnection.setRequestProperty("Accept-Language", "zh-CH");
        httpURLConnection.setRequestProperty("Charset", "UTF-8");
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
    }

    private void throwErrorToUiThread(final String error) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fileDownloadListener.error(error);
            }
        });
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(strUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            /** 配置 HttpURLConnection **/
            configConnection(httpURLConnection);
            /** 获取服务器中图片的输入流 **/
            inputStream = httpURLConnection.getInputStream();

            int fileSize = httpURLConnection.getContentLength();

            /** 输入流转输出流 写入文件 **/
            writeToFile(inputStream, fileSize);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fileDownloadListener.success();
                }
            });
        } catch (final MalformedURLException e) {
            throwErrorToUiThread(e.getMessage());
        } catch (IOException e) {
            throwErrorToUiThread(e.getMessage());
        } finally {
            /** 关闭连接 **/
            try {
                if (inputStream != null)
                    inputStream.close();
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写入到指定路径文件
     *
     * @param inputStream
     * @param fileSize
     * @throws IOException
     */
    private void writeToFile(InputStream inputStream, final int fileSize) throws IOException {
        byte[] date = new byte[1024];
        int len;
        final int[] lenCount = {0};

        File dir = new File(localPath);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, name);
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fileOutputStream;
        fileOutputStream = new FileOutputStream(file);

        while ((len = inputStream.read(date)) != -1) {
            fileOutputStream.write(date, 0, len);

            final int finalLen = len;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fileDownloadListener.progress(lenCount[0] += finalLen, fileSize);
                }
            });
        }

        try {
            if (fileOutputStream != null)
                fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
