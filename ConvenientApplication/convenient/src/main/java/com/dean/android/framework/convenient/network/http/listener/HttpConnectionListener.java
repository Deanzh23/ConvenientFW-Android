package com.dean.android.framework.convenient.network.http.listener;

/**
 * 网络请求状态监听器
 * <p>
 * Created by Dean on 16/8/9.
 */
public abstract class HttpConnectionListener {

    /**
     * 请求成功（返回状态码200）
     * <p>
     * 由框架调用此方法
     *
     * @param response
     */
    public void onRequestSuccess(String response) {
        onSuccess(response);
        onEnd();
    }

    /**
     * 请求错误（返回状态码非200的其它）
     * <p>
     * 由框架调用此方法
     *
     * @param responseCode
     */
    public void onRequestError(int responseCode) {
        onError(responseCode);
        onEnd();
    }

    /**
     * token失效（返回状态码9004）
     * <p>
     * 由框架调用此方法
     */
    public void onRequestTokenFailure() {
        onEnd();
        onTokenFailure();
    }

    /**
     * 请求成功（返回状态码200）
     * <p>
     * 由客户端实现
     *
     * @param response
     */
    public abstract void onSuccess(String response);

    /**
     * 请求失败（返回状态码非200的其它）
     * <p>
     * 由客户端实现
     *
     * @param responseCode
     */
    public abstract void onError(int responseCode);

    /**
     * token失效（返回状态码非9004）
     * <p>
     * 由客户端实现
     */
    public abstract void onTokenFailure();

    /**
     * 请求完成
     * <p>
     * 由客户端实现
     */
    public abstract void onEnd();

}
