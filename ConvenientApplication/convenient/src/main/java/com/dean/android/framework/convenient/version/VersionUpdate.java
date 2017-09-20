package com.dean.android.framework.convenient.version;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dean.android.framework.convenient.network.NetworkUtil;
import com.dean.android.framework.convenient.network.http.DefaultHttpConnection;
import com.dean.android.framework.convenient.network.http.listener.HttpConnectionListener;
import com.dean.android.framework.convenient.version.listener.OnCheckVersionListener;
import com.dean.android.framework.convenient.version.util.VersionUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 版本更新
 * <p>
 * Created by Dean on 2016/10/18.
 */
public class VersionUpdate {

    private Context context;

    // 检查更新的url
    private String checkUpdateURL;
    // 本地版本号
    private int localVersionCode;
    // 服务器版本号
    private int serverVersionCode;
    // apk下载路径
    private String apkDownloadURL;
    // 是否强制更新
    private boolean isForceUpdate;
    // 更新内容信息
    private String updateMessage;
    // 是否处于移动网络
    private boolean isMobileNetwork;

    public VersionUpdate(Context context) {
        this.context = context;

        localVersionCode = VersionUtil.getVersionCode(this.context);
        isMobileNetwork = !NetworkUtil.isWifiConnected(this.context);
    }

    public void setCheckUpdateURL(String checkUpdateURL) {
        this.checkUpdateURL = checkUpdateURL;
    }

    public String getApkDownloadURL() {
        return apkDownloadURL;
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public boolean isMobileNetwork() {
        return isMobileNetwork;
    }

    /**
     * 是否有新版本
     *
     * @return
     */
    public boolean hasNewVersion() {
        return serverVersionCode > localVersionCode;
    }

    /**
     * 检查版本更新
     *
     * @return
     */
    public void checkUpdate(final OnCheckVersionListener onCheckVersionListener) {
        // 没有网络
        if (NetworkUtil.getNetworkState(context) == null || TextUtils.isEmpty(checkUpdateURL)) {
            onCheckVersionListener.onCheck(false, null);
        } else {
            DefaultHttpConnection defaultHttpConnection = new DefaultHttpConnection();
            defaultHttpConnection.sendHttpGet(checkUpdateURL, null, null, new HttpConnectionListener() {
                @Override
                public void success(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        serverVersionCode = json.getInt("versionCode");

                        if (serverVersionCode > localVersionCode) {
                            apkDownloadURL = json.getString("downloadApk");
                            isForceUpdate = json.getBoolean("forceUpdate");
                            updateMessage = json.getString("updateMessage");

                            onCheckVersionListener.onCheck(true, json);
                        } else
                            onCheckVersionListener.onCheck(false, null);
                    } catch (JSONException e) {
                        Log.e(VersionUpdate.class.getSimpleName(), e.getMessage());
                        onCheckVersionListener.onCheck(false, null);
                    }
                }

                @Override
                public void error(int responseCode) {
                    onCheckVersionListener.onCheck(false, null);
                }

                @Override
                public void end() {
                }
            });
        }
    }

}