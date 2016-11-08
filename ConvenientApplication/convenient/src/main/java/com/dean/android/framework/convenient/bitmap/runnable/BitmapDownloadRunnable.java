package com.dean.android.framework.convenient.bitmap.runnable;

import android.app.Activity;

import com.dean.android.framework.convenient.bitmap.listener.BitmapDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Bitmap 下载 线程
 * <p/>
 * Created by Dean on 16/5/11.
 */
public class BitmapDownloadRunnable implements Runnable {

    private Activity mActivity;
    private String mURL;
    private String mLocalPath;
    private String mName;
    private BitmapDownloadListener mBitmapDownloadListener;

    public BitmapDownloadRunnable(Activity activity, String url, String localPath, String name, BitmapDownloadListener bitmapDownloadListener) {
        this.mActivity = activity;
        this.mURL = url;
        this.mLocalPath = localPath;
        this.mName = name;
        this.mBitmapDownloadListener = bitmapDownloadListener;
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

    @Override
    public void run() {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(mURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            /** 配置 HttpURLConnection **/
            configConnection(httpURLConnection);
            /** 获取服务器中图片的输入流 **/
            inputStream = httpURLConnection.getInputStream();

            int fileSize = httpURLConnection.getContentLength();

            /** 输入流转输出流 写入文件 **/
            writeToFile(inputStream, fileSize);

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBitmapDownloadListener.success();
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

    private void throwErrorToUiThread(final String error) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBitmapDownloadListener.error(error);
            }
        });
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

        File dir = new File(mLocalPath);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, mName);
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fileOutputStream;
        fileOutputStream = new FileOutputStream(file);

        while ((len = inputStream.read(date)) != -1) {
            fileOutputStream.write(date, 0, len);

            final int finalLen = len;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBitmapDownloadListener.progress(lenCount[0] += finalLen, fileSize);
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
