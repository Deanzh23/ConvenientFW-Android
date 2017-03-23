package com.dean.android.fw.convenient.googlemap.ui.view;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.dean.android.framework.convenient.location.ConvenientLocationUtils;
import com.dean.android.fw.convenient.googlemap.listener.GoogleMapHtmlListener;

/**
 * 谷歌地图HTML控件
 * <p>
 * Created by dean on 2017/3/23.
 */
public class GoogleMapHTMLView extends WebView {

    private final String HTML_FILE_PATH = "file:///android_asset/GoogleMap.html";
    private final String JAVASCRIPT_PREFIX = "javascript:";

    private Context context;

    private GoogleMapHtmlListener googleMapHtmlListener;

    private Handler handler = new Handler();

    public GoogleMapHTMLView(@NonNull Context context) {
        super(context);

        this.context = context;
        initWebView();
    }

    public GoogleMapHTMLView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        initWebView();
    }

    /**
     * 初始化加载显示Google map HTML 的 WebView
     */
    private void initWebView() {
        setWebViewConfig();
        setListener();

        // 设置HTML文件路径
        this.loadUrl(HTML_FILE_PATH);

        location();
    }

    /**
     * 设置WebView配置
     */
    private void setWebViewConfig() {
        WebSettings settings = this.getSettings();
        // 设置支持JavaScript
        settings.setJavaScriptEnabled(true);
        // 设置优先使用缓存（或不使用缓存：WebSettings.LOAD_NO_CACHE）
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        this.setWebChromeClient(new WebChromeClient() {

            /**
             * 监听页面加载进度
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (googleMapHtmlListener != null)
                    googleMapHtmlListener.onLoadProgressChanged(newProgress);
            }
        });
    }

    /**
     * 移动地图到指定经纬度
     *
     * @param lat
     * @param lng
     */
    public void moveTo(String lat, String lng) {
        executeJavaScript("moveTo(" + lat + "," + lng + ")");
    }

    /**
     * 设置缩放
     *
     * @param zoom
     */
    public void setZoom(int zoom) {
        executeJavaScript("setZoom(" + zoom + ")");
    }

    /**
     * 设置我的位置
     *
     * @param lat
     * @param lng
     * @param moveToMyLocation
     */
    public void setMyLocationMarker(String lat, String lng, boolean moveToMyLocation) {
        executeJavaScript("setMyLocationMarker(" + lat + "," + lng + "," + moveToMyLocation + ")");
    }

    /**
     * 执行JavaScript函数
     *
     * @param method
     */
    private void executeJavaScript(final String method) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    loadUrl(JAVASCRIPT_PREFIX + method);
                }
            }
        });
    }

    class JavaScriptInterface {
    }

    /**
     * 设置监听器
     *
     * @param googleMapHtmlListener
     */
    public void setGoogleMapHtmlListener(GoogleMapHtmlListener googleMapHtmlListener) {
        this.googleMapHtmlListener = googleMapHtmlListener;
    }

    /**
     * 初始化定位自己的位置
     */
    private void location() {
        ConvenientLocationUtils.requestLocationUpdatesOnce(context, LocationManager.NETWORK_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setMyLocationMarker(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), true);
                        setZoom(14);
                    }
                }, 3000);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        });
    }
}