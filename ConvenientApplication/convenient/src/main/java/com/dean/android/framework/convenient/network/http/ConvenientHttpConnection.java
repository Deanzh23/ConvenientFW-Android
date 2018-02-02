package com.dean.android.framework.convenient.network.http;

import com.dean.android.framework.convenient.network.http.listener.OnHttpConnectionListener;

import java.io.File;
import java.util.Map;

/**
 * Convenient http connection
 * <p>
 * Created by Dean on 16/8/8.
 */
public class ConvenientHttpConnection extends DefaultHttpConnection {

    /**
     * 发送（默认配置的） HTTP Get 请求
     *
     * @param basicURL
     * @param params
     * @param onHttpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> headerParams, Object params, OnHttpConnectionListener onHttpConnectionListener) {
        super.sendHttpGet(basicURL, headerParams, params, "utf-8", 5000, false, onHttpConnectionListener);
    }

    /**
     * 发送（默认配置的） HTTPS Get 请求
     *
     * @param basicURL
     * @param params
     * @param onHttpConnectionListener
     */
    public void sendHttpsGet(String basicURL, Map<String, String> headerParams, Object params, OnHttpConnectionListener onHttpConnectionListener) {
        super.sendHttpsGet(basicURL, headerParams, params, "utf-8", 5000, false, onHttpConnectionListener);
    }

    /**
     * 发送（默认配置的） HTTP Post 请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param onHttpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> headerParams, Object params, Map<String, String> bodyParams,
                             OnHttpConnectionListener onHttpConnectionListener) {
        super.sendHttpPost(basicURL, headerParams, params, bodyParams, "utf-8", 5000, false, onHttpConnectionListener);
    }

    /**
     * 发送（默认配置的） HTTPS Post 请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param onHttpConnectionListener
     */
    public void sendHttpsPost(String basicURL, Map<String, String> headerParams, Object params, Map<String, String> bodyParams,
                              OnHttpConnectionListener onHttpConnectionListener) {
        super.sendHttpsPost(basicURL, headerParams, params, bodyParams, "utf-8", 5000, false, onHttpConnectionListener);
    }

    /**
     * 发送（默认配置的） HTTP Post 请求
     * <p>
     * body参数是复杂的形式
     *
     * @param basicURL
     * @param headerParams
     * @param params
     * @param bodyParams
     * @param onHttpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> headerParams, Object params, String bodyParams, OnHttpConnectionListener onHttpConnectionListener) {
        super.sendHttpPost(basicURL, headerParams, params, bodyParams, "utf-8", 5000, false, onHttpConnectionListener);
    }

    /**
     * 文件上传
     *
     * @param basicURL
     * @param urlParams
     * @param file
     * @param onHttpConnectionListener
     */
    public void sendFile(String basicURL, Object urlParams, File file, OnHttpConnectionListener onHttpConnectionListener) {
        super.sendFile(basicURL, urlParams, file, onHttpConnectionListener);
    }

}
