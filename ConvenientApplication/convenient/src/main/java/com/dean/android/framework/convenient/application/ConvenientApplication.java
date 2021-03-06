package com.dean.android.framework.convenient.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dean.android.framework.convenient.file.download.listener.FileDownloadListener;
import com.dean.android.framework.convenient.file.util.FileUitl;
import com.dean.android.framework.convenient.format.MathFormatUtils;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.android.framework.convenient.util.SetUtil;
import com.dean.android.framework.convenient.version.VersionUpdate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 架构Application
 * <p/>
 * Created by Dean on 16/5/24.
 */
public abstract class ConvenientApplication extends Application {

    public static final String CONVENIENT_TAG = "Convenient";

    // app初始化配置和数据完成的Action
    public static final String ACTION_APP_INIT_FINISH = ConvenientApplication.class.getSimpleName() + ".ACTION_APP_INIT_FINISH";

    // app是否初始化完成（默认未完成）
    public static boolean isAppInitFinish = false;
    // 启动过的Activity
    private static Map<String, Object> mHistoryActivityMap = new HashMap<>();
    // 版本更新类
    private static VersionUpdate versionUpdate;
    private static String versionUpdateDownloadLocalPath;

    private static AlertDialog apkDownloadProgressDialog;

    @Override
    public void onCreate() {
        super.onCreate();

        // 版本升级
        versionUpdate();

        // 解决URI问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    /**
     * 初始化
     */
    private void versionUpdate() {
        versionUpdate = new VersionUpdate(ConvenientApplication.this.getApplicationContext());
        versionUpdate.setCheckUpdateURL(checkVersionUrl());

        new Thread(() -> {
            // 抽象方法，开发人员根据业务自己实现初始化加载的配置和数据
            initConfigAndData();

            versionUpdate.checkUpdate((hasVersionUpdate, updateInfo) -> {
                if (!hasVersionUpdate || updateInfo == null)
                    // 如果没有新版本更新，设置app初始化完成
                    appInitFinish();
                else
                    // 显示版本更新提示框
                    versionUpdateDownloadLocalPath = setVersionUpdateDownloadLocalPath();

                appInitFinish();
            });
        }).start();
    }

    /**
     * 开始下载新版本apk
     *
     * @param context
     * @param handler
     * @param apkDownloadUrl
     */
    public static void startDownloadNewVersionAPK(final Context context, final Handler handler, String apkDownloadUrl, final FileDownloadListener fileDownloadListener,
                                                  final boolean useDefaultDownloadDialog) {
        final String fileName = "update.apk";

        if (useDefaultDownloadDialog) {
            if (apkDownloadProgressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("升级文件下载中...");
                builder.setCancelable(false);

                apkDownloadProgressDialog = builder.create();
            }

            apkDownloadProgressDialog.setMessage("");
            apkDownloadProgressDialog.show();
        }

        FileUitl.download(handler, apkDownloadUrl, versionUpdateDownloadLocalPath, fileName, new FileDownloadListener() {
            @Override
            public void start() {
                ToastUtil.showToast(context, "开始下载升级文件");
            }

            @Override
            public void success() {
                ToastUtil.showToast(context, "下载成功");
                installLocalAPK(context, versionUpdateDownloadLocalPath + "/" + fileName);

                System.exit(0);
            }

            @Override
            public void error(String error) {
                AlertDialog.Builder errorBuilder = new AlertDialog.Builder(context);
                errorBuilder.setMessage("升级文件下载错误");
                errorBuilder.setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });

                errorBuilder.setCancelable(false);
                errorBuilder.create().show();
            }

            @Override
            public void progress(int current, int total) {
                Log.d(CONVENIENT_TAG, "下载进度: " + current + " / " + total);

                if (useDefaultDownloadDialog && apkDownloadProgressDialog != null)
                    apkDownloadProgressDialog.setMessage(MathFormatUtils.formatByte(current) + " / " + MathFormatUtils.formatByte(total));

                if (fileDownloadListener != null)
                    fileDownloadListener.progress(current, total);
            }
        });
    }

    /**
     * 安装本地apk
     *
     * @param context
     * @param filePath
     */
    public static void installLocalAPK(Context context, String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 设置app初始化完成
     */
    private void appInitFinish() {
        // 设置app已完成初始化标记
        isAppInitFinish = true;
        // 发送app初始化完成广播
        sendBroadcast(new Intent(ACTION_APP_INIT_FINISH));
    }

    /**
     * 向“历史Activity”中添加activity
     *
     * @param object
     */
    public static void addHistoryActivity(Object object) {
        mHistoryActivityMap.put(object.getClass().getSimpleName(), object);
    }

    /**
     * 关闭历史activity
     *
     * @param excludeName
     */
    public static void killHistoryActivity(String excludeName) {
        if (mHistoryActivityMap.size() > 0) {
            for (Map.Entry<String, Object> entry : mHistoryActivityMap.entrySet()) {
                String name = entry.getKey();
                Object activity = entry.getValue();

                if (excludeName != null) {
                    if (excludeName.equals(name))
                        continue;
                    else
                        finishActivity(activity);
                } else
                    finishActivity(activity);
            }
        }
    }

    /**
     * 关闭历史activity
     *
     * @param excludeNames
     */
    public static void killSpecifiedActivity(List<String> excludeNames) {
        if (mHistoryActivityMap.size() > 0) {
            for (Map.Entry<String, Object> entry : mHistoryActivityMap.entrySet()) {
                String name = entry.getKey();
                Object activity = entry.getValue();

                if (!SetUtil.isEmpty(excludeNames) && excludeNames.contains(name))
                    finishActivity(activity);
            }
        }
    }

    private static void finishActivity(Object object) {
        Log.d(ConvenientApplication.class.getSimpleName(), "[finishActivity]--> finish activity name is " + object.getClass().getSimpleName());

        if (object instanceof Activity)
            ((Activity) object).finish();
    }

    public static VersionUpdate getVersionUpdate() {
        return versionUpdate;
    }

    /**
     * app初始化抽象方法(* 开发人员不要创建新线程去写自己的业务)
     * <p/>
     * 开发人员根据业务自己实现初始化加载的配置和数据
     */
    protected abstract void initConfigAndData();

    /**
     * apk升级文件下载到本地后的存放路径
     *
     * @return
     */
    protected abstract String setVersionUpdateDownloadLocalPath();

    /**
     * 配置apk检查更新的url
     *
     * @return
     */
    protected abstract String checkVersionUrl();
}
