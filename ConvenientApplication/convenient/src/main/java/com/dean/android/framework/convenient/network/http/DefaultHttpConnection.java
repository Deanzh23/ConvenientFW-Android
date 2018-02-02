package com.dean.android.framework.convenient.network.http;

import android.util.Log;

import com.dean.android.framework.convenient.network.http.listener.OnHttpConnectionListener;
import com.dean.android.framework.convenient.util.SetUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Http请求超类
 * <p>
 * Created by Dean on 16/8/9.
 */
public class DefaultHttpConnection {

    /**
     * 应答"成功"
     */
    public static final String RESPONSE_SUCCESS = "200";
    /**
     * 应答"token失效"
     */
    public static final String RESPONSE_TOKEN_LOSE_EFFICACY = "9004";

    /**
     * 绕过HTTPS证书认证
     */
    private class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i(DefaultHttpConnection.class.getSimpleName(), "Approving certificate for " + hostname);
            return true;
        }
    }

    /**
     * 发送默认配置的HttpGet请求
     *
     * @param basicURL
     * @param params
     * @param onHttpConnectionListener
     */
    protected void sendHttpGet(String basicURL, Map<String, String> headerParams, Object params, OnHttpConnectionListener onHttpConnectionListener) {
        sendHttpGet(basicURL, headerParams, params, "utf-8", 5000, false, onHttpConnectionListener);
    }

    /**
     * 发送默认配置的HttpsGet请求
     *
     * @param basicURL
     * @param params
     * @param onHttpConnectionListener
     */
    protected void sendHttpsGet(String basicURL, Map<String, String> headerParams, Object params, OnHttpConnectionListener onHttpConnectionListener) {
        sendHttpsGet(basicURL, headerParams, params, "utf-8", 5000, false, onHttpConnectionListener);
    }

    /**
     * 发送HttpGet请求
     *
     * @param basicURL
     * @param headerParams
     * @param urlParams
     * @param encoding
     * @param connectTimeout
     * @param useCaches
     * @param onHttpConnectionListener
     */
    protected void sendHttpGet(String basicURL, Map<String, String> headerParams, Object urlParams, String encoding, int connectTimeout, boolean useCaches,
                               OnHttpConnectionListener onHttpConnectionListener) {
        sendGet(basicURL, headerParams, urlParams, encoding, connectTimeout, useCaches, onHttpConnectionListener);
    }

    /**
     * 发送HttpsGet请求
     *
     * @param basicURL
     * @param headerParams
     * @param urlParams
     * @param encoding
     * @param connectTimeout
     * @param useCaches
     * @param onHttpConnectionListener
     */
    protected void sendHttpsGet(String basicURL, Map<String, String> headerParams, Object urlParams, String encoding, int connectTimeout, boolean useCaches,
                                OnHttpConnectionListener onHttpConnectionListener) {
        try {
            setBypassHttpsAuthentication();
            sendGet(basicURL, headerParams, urlParams, encoding, connectTimeout, useCaches, onHttpConnectionListener);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            if (onHttpConnectionListener != null)
                onHttpConnectionListener.onRequestError(-1);
        }
    }

    /**
     * 发送GET请求
     *
     * @param basicURL
     * @param headerParams
     * @param urlParams
     * @param encoding
     * @param connectTimeout
     * @param useCaches
     * @param onHttpConnectionListener
     */
    private void sendGet(String basicURL, Map<String, String> headerParams, Object urlParams, String encoding, int connectTimeout,
                         boolean useCaches, OnHttpConnectionListener onHttpConnectionListener) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            String urlParam = getHttpURL(basicURL, urlParams);
            URL url = new URL(urlParam);
            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "GET", encoding, connectTimeout, useCaches, headerParams);

            connection.connect();

            /** 响应 **/
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    responseBuilder.append(line);

                Log.d(ConvenientHttpConnection.class.getSimpleName(), "response is " + (responseBuilder.length() == 0 ? null : responseBuilder.toString()));

                if (onHttpConnectionListener != null) {
                    // 验证token
                    JSONObject response = new JSONObject(responseBuilder.toString());
                    String code = response.getString("code");
                    // 返回"token失效"应答
                    if (RESPONSE_TOKEN_LOSE_EFFICACY.equals(code)) {
                        onHttpConnectionListener.onRequestTokenFailure();
                    }
                    // 正常返回应答
                    else
                        onHttpConnectionListener.onRequestSuccess(responseBuilder.length() == 0 ? null : responseBuilder.toString());
                }
            } else {
                if (onHttpConnectionListener != null)
                    onHttpConnectionListener.onRequestError(responseCode);
            }
        } catch (IOException | JSONException e) {
            if (onHttpConnectionListener != null)
                onHttpConnectionListener.onRequestError(-1);
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (inputStream != null)
                    inputStream.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送默认配置的HttpPost请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param onHttpConnectionListener
     */
    protected void sendHttpPost(String basicURL, Map<String, String> headerParams, Object params, Map<String, String> bodyParams,
                                OnHttpConnectionListener onHttpConnectionListener) {
        sendHttpPost(basicURL, headerParams, params, bodyParams, "utf-8", 5000, false, onHttpConnectionListener);
    }

    /**
     * 发送HttpPost请求
     *
     * @param basicURL
     * @param headerParams
     * @param urlParams
     * @param bodyParams
     * @param encoding
     * @param connectTimeout
     * @param useCaches
     * @param onHttpConnectionListener
     */
    protected void sendHttpPost(String basicURL, Map<String, String> headerParams, Object urlParams, Object bodyParams, String encoding, int connectTimeout,
                                boolean useCaches, OnHttpConnectionListener onHttpConnectionListener) {
        sendPost(basicURL, headerParams, urlParams, bodyParams, encoding, connectTimeout, useCaches, onHttpConnectionListener);
    }

    /**
     * 发送HttpsPost请求
     *
     * @param basicURL
     * @param headerParams
     * @param urlParams
     * @param bodyParams
     * @param encoding
     * @param connectTimeout
     * @param useCaches
     * @param onHttpConnectionListener
     */
    protected void sendHttpsPost(String basicURL, Map<String, String> headerParams, Object urlParams, Object bodyParams, String encoding, int connectTimeout,
                                 boolean useCaches, OnHttpConnectionListener onHttpConnectionListener) {
        try {
            setBypassHttpsAuthentication();
            sendPost(basicURL, headerParams, urlParams, bodyParams, encoding, connectTimeout, useCaches, onHttpConnectionListener);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            if (onHttpConnectionListener != null)
                onHttpConnectionListener.onRequestError(-1);
        }
    }

    /**
     * 发送Post请求
     *
     * @param basicURL
     * @param headerParams
     * @param urlParams
     * @param bodyParams
     * @param encoding
     * @param connectTimeout
     * @param useCaches
     * @param onHttpConnectionListener
     */
    private void sendPost(String basicURL, Map<String, String> headerParams, Object urlParams, Object bodyParams, String encoding, int connectTimeout,
                          boolean useCaches, OnHttpConnectionListener onHttpConnectionListener) {
        String urlParam = getHttpURL(basicURL, urlParams);

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlParam);

            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "POST", encoding, connectTimeout, useCaches, headerParams);

            connection.connect();

            /** body **/
            JSONObject bodyJSONObject = null;
            String bodyString = null;

            if (bodyParams != null) {
                if (bodyParams instanceof Map) {
                    Map<String, String> bodyMap = (Map<String, String>) bodyParams;

                    if (SetUtil.isEmpty(bodyMap))
                        return;

                    bodyJSONObject = new JSONObject();

                    for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        bodyJSONObject.put(URLEncoder.encode(key, "utf-8"), URLEncoder.encode(value, "utf-8"));
                    }
                } else if (bodyParams instanceof String) {
                    bodyString = (String) bodyParams;
                }
            }

            OutputStream outputStream = connection.getOutputStream();
            if (bodyJSONObject != null)
                outputStream.write(bodyJSONObject.toString().getBytes());
            else if (bodyString != null)
                outputStream.write(bodyString.getBytes());
            outputStream.flush();
            outputStream.close();

            /** 响应 **/
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    responseBuilder.append(line);

                Log.d(ConvenientHttpConnection.class.getSimpleName(), "response is " + (responseBuilder.length() == 0 ? null : responseBuilder.toString()));

                if (onHttpConnectionListener != null) {
                    // 验证token
                    JSONObject response = new JSONObject(responseBuilder.toString());
                    String code = response.getString("code");
                    // 返回"token失效"应答
                    if (RESPONSE_TOKEN_LOSE_EFFICACY.equals(code)) {
                        onHttpConnectionListener.onRequestTokenFailure();
                    }
                    // 正常返回应答
                    else
                        onHttpConnectionListener.onRequestSuccess(responseBuilder.length() == 0 ? null : responseBuilder.toString());
                }
            } else {
                if (onHttpConnectionListener != null)
                    onHttpConnectionListener.onRequestError(responseCode);
            }
        } catch (JSONException | IOException e) {
            if (onHttpConnectionListener != null)
                onHttpConnectionListener.onRequestError(-1);
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (inputStream != null)
                    inputStream.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();

        String url = getHttpURL(basicURL, urlParams);
        params.addBodyParameter("file", file, "image/png");
        httpUtils.configSoTimeout(10000);
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 验证token
                try {
                    JSONObject response = new JSONObject(responseInfo.result);
                    String code = response.getString("code");
                    // 返回"token失效"应答
                    if (RESPONSE_TOKEN_LOSE_EFFICACY.equals(code)) {
                        onHttpConnectionListener.onRequestTokenFailure();
                    }
                    // 正常返回应答
                    else
                        onHttpConnectionListener.onRequestSuccess(responseInfo.result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (onHttpConnectionListener != null)
                        onHttpConnectionListener.onRequestError(-1);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (onHttpConnectionListener != null)
                    onHttpConnectionListener.onRequestError(e.getExceptionCode());
            }
        });
    }

    /**
     * 设置连接规则
     *
     * @param connection
     * @param encoding
     * @param connectTimeout
     * @param useCaches
     */
    private void setHttpURLConnectionConfig(HttpURLConnection connection, String requestMethod, String encoding, int connectTimeout, boolean useCaches,
                                            Map<String, String> headerParams) throws ProtocolException {
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("encoding", encoding);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setConnectTimeout(connectTimeout);
        connection.setUseCaches(useCaches);

        // 设置用户header参数
        if (!SetUtil.isEmpty(headerParams)) {
            for (Map.Entry<String, String> entry : headerParams.entrySet())
                connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        boolean isGetMethod = (requestMethod != null && requestMethod.toLowerCase().equals("get"));
        connection.setDoOutput(!isGetMethod);
        connection.setDoInput(true);
    }

    /**
     * 获取配置好的URL
     *
     * @param basicURL
     * @param urlParams
     * @return
     */
    private String getHttpURL(String basicURL, Object urlParams) {
        StringBuilder builder = new StringBuilder(basicURL);

        if (urlParams instanceof Map) {
            Map<String, Object> tempURLParams = (Map<String, Object>) urlParams;

            int i = 0;
            if (!SetUtil.isEmpty(tempURLParams))

                for (Map.Entry<String, Object> entry : tempURLParams.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof String) {
                        builder.append(i == 0 ? "?" : "&").append(URLEncoder.encode(key)).append("=").append(URLEncoder.encode((String) value));
                    } else {
                        builder.append(i == 0 ? "?" : "&").append(URLEncoder.encode(key)).append("=").append(value);
                    }

                    i++;
                }
        } else if (urlParams instanceof List) {
            for (Object value : (List<Object>) urlParams) {
                if (value instanceof String) {
                    builder.append("/").append(URLEncoder.encode((String) value));
                } else {
                    builder.append("/").append(value);
                }
            }
        }

        return builder.toString();
    }

    /**
     * 设置HTTPS请求绕过证书验证
     *
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public void setBypassHttpsAuthentication() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

}
