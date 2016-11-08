package com.dean.android.framework.convenient.network.http;

import android.util.Log;

import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;
import com.dean.android.framework.convenient.util.SetUtil;

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
import java.util.Map;

/**
 * Created by Dean on 16/8/8.
 */
public class HttpConnection {

    /**
     * 发送默认配置的HttpGet请求
     *
     * @param basicURL
     * @param params
     * @param httpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> params, HttpConnectionListener httpConnectionListener) {
        sendHttpGet(basicURL, params, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送HttpGet请求
     *
     * @param basicURL
     * @param params
     * @param encoding
     * @param timeOut
     * @param isUseCache
     * @param httpConnectionListener
     */
    public void sendHttpGet(String basicURL, Map<String, String> params, String encoding, int timeOut, boolean isUseCache,
                            HttpConnectionListener httpConnectionListener) {

        StringBuilder builder = new StringBuilder(basicURL);

        int i = 0;
        if (!SetUtil.isEmpty(params))

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                builder.append(i == 0 ? "?" : "&").append(URLEncoder.encode(key)).append("=").append(URLEncoder.encode(value));

                i++;
            }

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(builder.toString());

            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "GET", encoding, timeOut, isUseCache);

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

                Log.d(HttpConnection.class.getSimpleName(), "response is " + (responseBuilder.length() == 0 ? null : responseBuilder.toString()));

                if (httpConnectionListener != null)
                    httpConnectionListener.success(responseBuilder.length() == 0 ? null : responseBuilder.toString());
            } else {
                if (httpConnectionListener != null)
                    httpConnectionListener.error(responseCode);
            }
        } catch (MalformedURLException e) {
            httpConnectionListener.error(-1);
        } catch (IOException e) {
            httpConnectionListener.error(-1);
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
    public void sendHttpPost(String basicURL, Map<String, String> params, Map<String, String> bodyParams, HttpConnectionListener httpConnectionListener) {
        sendHttpPost(basicURL, params, bodyParams, "utf-8", 5000, false, httpConnectionListener);
    }

    /**
     * 发送HttpPost请求
     *
     * @param basicURL
     * @param params
     * @param bodyParams
     * @param encoding
     * @param timeOut
     * @param isUseCache
     * @param httpConnectionListener
     */
    public void sendHttpPost(String basicURL, Map<String, String> params, Map<String, String> bodyParams, String encoding, int timeOut, boolean isUseCache,
                             HttpConnectionListener httpConnectionListener) {

        StringBuilder builder = new StringBuilder(basicURL);

        int i = 0;
        if (!SetUtil.isEmpty(params))

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                builder.append(i == 0 ? "?" : "&").append(URLEncoder.encode(key)).append("=").append(URLEncoder.encode(value));

                i++;
            }

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(builder.toString());

            connection = (HttpURLConnection) url.openConnection();
            setHttpURLConnectionConfig(connection, "POST", encoding, timeOut, isUseCache);

            connection.connect();

            /** body **/
            if (!SetUtil.isEmpty(bodyParams)) {
                String strBody = null;

                for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    strBody = URLEncoder.encode(key, "utf-8") + "=" + URLEncoder.encode(value, "utf-8");
                }

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(strBody.getBytes());
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

                Log.d(HttpConnection.class.getSimpleName(), "response is " + (responseBuilder.length() == 0 ? null : responseBuilder.toString()));

                if (httpConnectionListener != null)
                    httpConnectionListener.success(responseBuilder.length() == 0 ? null : responseBuilder.toString());
            } else {
                if (httpConnectionListener != null)
                    httpConnectionListener.error(responseCode);
            }
        } catch (MalformedURLException e) {
            httpConnectionListener.error(-1);
        } catch (IOException e) {
            httpConnectionListener.error(-1);
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
    private void setHttpURLConnectionConfig(HttpURLConnection connection, String requestMethod, String encoding, int timeOut, boolean isUseCache)
            throws ProtocolException {

        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("encoding", encoding);
        connection.setConnectTimeout(timeOut);
        connection.setUseCaches(isUseCache);

        boolean isGetMethod = (requestMethod != null && requestMethod.toLowerCase().equals("get"));
        connection.setDoOutput(!isGetMethod);
        connection.setDoInput(true);
    }

}
