package com.dean.android.framework.convenient.terminal.ping;

import android.app.Activity;

import com.dean.android.framework.convenient.terminal.ping.listener.OnPingListener;
import com.dean.android.framework.convenient.terminal.ping.thread.PingThread;

/**
 * ping命令访问一个url
 * <p>
 * Created by Dean on 2016/11/15.
 */
public class DefaultPing {

    private String ip;

    public DefaultPing(String ip) {
        this.ip = ip;
    }

    /**
     * code is 0 ：成功
     * 其他：失败
     */
    public void ping(Activity activity, OnPingListener onPingListener) {
        PingThread pingThread = new PingThread(ip);
        pingThread.start(activity, onPingListener);
    }

}
