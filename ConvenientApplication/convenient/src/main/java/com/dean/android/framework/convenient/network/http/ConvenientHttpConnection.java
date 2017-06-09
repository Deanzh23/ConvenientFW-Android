package com.dean.android.framework.convenient.network.http;

import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;

import java.util.Map;

/**
 * Convenient http connection
 * <p>
 * Created by Dean on 16/8/8.
 */
public class ConvenientHttpConnection extends DefaultHttpConnection {

    /**
     * 发送（默认配置的） Http Get 请求
     *
     * @param basicURL
     * @param params
     * @param httpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> headerParams, Object params, HttpConnectionListener httpConnectionListener) {
        super.sendHttpGet(basicURL, headerParams, params, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送（默认配置的） Http Post 请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param httpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> headerParams, Object params, Map<String, String> bodyParams,
                             HttpConnectionListener httpConnectionListener) {
        super.sendHttpPost(basicURL, headerParams, params, bodyParams, "utf-8", 5000, false, httpConnectionListener);
    }

}