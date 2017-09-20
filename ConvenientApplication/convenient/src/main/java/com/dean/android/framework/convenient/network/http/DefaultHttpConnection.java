package com.dean.android.framework.convenient.network.http;

import android.util.Log;

import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;
import com.dean.android.framework.convenient.util.SetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Http请求超类
 * <p>
 * Created by Dean on 16/8/9.
 */
public class DefaultHttpConnection {

    /**
     * 发送默认配置的HttpGet请求
     *
     * @param basicURL
     * @param params
     * @param httpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> headerParams, Object params, HttpConnectionListener httpConnectionListener) {
        sendHttpGet(basicURL, headerParams, params, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送HttpGet请求
     *
     * @param basicURL
     * @param headerParams
     * @param urlParams
     * @param encoding
     * @param timeOut
     * @param isUseCache
     * @param httpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> headerParams, Object urlParams, String encoding, int timeOut, boolean isUseCache,
                            HttpConnectionListener httpConnectionListener) {
        String urlParam = getHttpURL(basicURL, urlParams);

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlParam);

            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "GET", encoding, timeOut, isUseCache, headerParams);

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

                if (httpConnectionListener != null)
                    httpConnectionListener.success(responseBuilder.length() == 0 ? null : responseBuilder.toString());
            } else {
                if (httpConnectionListener != null)
                    httpConnectionListener.error(responseCode);
            }
        } catch (MalformedURLException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } catch (IOException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } finally {
            if (httpConnectionListener != null)
                httpConnectionListener.end();

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
            }
        }
    }

    /**
     * 发送默认配置的HttpPost请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param httpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> headerParams, Object params, Map<String, String> bodyParams,
                             HttpConnectionListener httpConnectionListener) {
        sendHttpPost(basicURL, headerParams, params, bodyParams, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送HttpPost请求
     *
     * @param basicURL
     * @param urlParams
     * @param bodyParams
     * @param encoding
     * @param timeOut
     * @param isUseCache
     * @param httpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> headerParams, Object urlParams, Map<String, String> bodyParams, String encoding,
                             int timeOut, boolean isUseCache, HttpConnectionListener httpConnectionListener) {
        String urlParam = getHttpURL(basicURL, urlParams);

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlParam);

            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "POST", encoding, timeOut, isUseCache, headerParams);

            connection.connect();

            /** body **/
            if (!SetUtil.isEmpty(bodyParams)) {
                JSONObject bodyJSONObject = new JSONObject();

                for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    bodyJSONObject.put(URLEncoder.encode(key, "utf-8"), URLEncoder.encode(value, "utf-8"));
                }

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(bodyJSONObject.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }

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

                if (httpConnectionListener != null)
                    httpConnectionListener.success(responseBuilder.length() == 0 ? null : responseBuilder.toString());
            } else {
                if (httpConnectionListener != null)
                    httpConnectionListener.error(responseCode);
            }
        } catch (MalformedURLException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } catch (IOException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } catch (JSONException e) {
            if (httpConnectionListener != null)
                httpConnectionListener.error(-1);
        } finally {
            if (httpConnectionListener != null)
                httpConnectionListener.end();

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
            }
        }
    }

    /**
     * 设置连接规则
     *
     * @param connection
     * @param encoding
     * @param timeOut
     * @param isUseCache
     */
    private void setHttpURLConnectionConfig(HttpURLConnection connection, String requestMethod, String encoding, int timeOut, boolean isUseCache,
                                            Map<String, String> headerParams) throws ProtocolException {
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("encoding", encoding);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setConnectTimeout(timeOut);
        connection.setUseCaches(isUseCache);

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


}
