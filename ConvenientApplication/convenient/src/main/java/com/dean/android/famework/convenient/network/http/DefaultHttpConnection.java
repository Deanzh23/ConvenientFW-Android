package com.dean.android.famework.convenient.network.http;

import com.dean.android.famework.convenient.network.http.listener.HttpConnectionListener;

import java.util.Map;

/**
 * Created by Dean on 16/8/9.
 */
public class DefaultHttpConnection extends HttpConnection {

    /**
     * 发送（默认配置的） Http Get 请求
     *
     * @param basicURL
     * @param params
     * @param httpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> params, HttpConnectionListener httpConnectionListener) {
        super.sendHttpGet(basicURL, params, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送（默认配置的） Http Post 请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param httpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> params, Map<String, String> bodyParams, HttpConnectionListener httpConnectionListener) {
        super.sendHttpPost(basicURL, params, bodyParams, "utf-8", 5000, false, httpConnectionListener);
    }

}
