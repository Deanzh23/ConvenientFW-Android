package com.dean.android.framework.convenient.terminal.ping.thread;

import android.app.Activity;
import android.util.Log;

import com.dean.android.framework.convenient.terminal.ping.listener.OnPingListener;

import java.io.IOException;

/**
 * Created by Dean on 2016/11/15.
 */
public class PingThread extends Thread {

    private static final String TAG = PingThread.class.getSimpleName();

    private String ip;

    private Activity activity;
    private OnPingListener onPingListener;

    public PingThread(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        try {
            Process process = Runtime.getRuntime().exec("ping -c 1 -w 5 " + ip);
            final int code = process.waitFor();
            Log.d(TAG, "code is " + code);

            if (activity != null && onPingListener != null)
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == 0)
                            onPingListener.onSuccess();
                        else
                            onPingListener.onError();
                    }
                });

            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "ping error!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "process.waitFor() error!");
        }
    }

    /**
     * 启动ping线程
     *
     * @param onPingListener
     */
    public void start(Activity activity, OnPingListener onPingListener) {
        this.activity = activity;
        this.onPingListener = onPingListener;

        super.start();
    }

}