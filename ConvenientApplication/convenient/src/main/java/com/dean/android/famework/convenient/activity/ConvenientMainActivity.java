package com.dean.android.famework.convenient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.dean.android.famework.convenient.application.ConvenientApplication;
import com.dean.android.famework.convenient.version.VersionUpdate;

/**
 * 架构的首页Activity
 * <p>
 * 主要配合FrameworkApplication初始化app来控制用户是否可以开始操作app了
 * <p>
 * Created by Dean on 16/5/24.
 */
public abstract class ConvenientMainActivity extends ConvenientActivity {

    // 版本更新dialog
    private AlertDialog versionUpdateDialog;

    /**
     * 是否app初始化配置和数据完成以后自动跳转，如需翻到最后一页点击进入的效果，请在onCreate(Bundle,boolean)方法中设置为false（默认true）
     */
    private boolean mIsAutoJump = true;
    private Handler mHandler = new Handler();

    /**
     * app初始化完成广播监听器
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mIsAutoJump)
                aboutCheckVersionUpdate();
        }
    };

    /**
     * 显示版本更新dialog
     *
     * @param versionUpdate
     */
    private void showVersionUpdateDialog(final VersionUpdate versionUpdate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = versionUpdate.getUpdateMessage();
        String title = "您有一个版本更新" + (versionUpdate.isMobileNetwork() ? "（您正在使用移动网络）" : "");
        if (TextUtils.isEmpty(message))
            builder.setMessage(title);
        else {
            builder.setTitle(title);
            builder.setMessage(message);
        }
        builder.setNegativeButton("取消", (dialog, which) -> {
            versionUpdateDialog.dismiss();

            // 强制更新
            if (versionUpdate.isForceUpdate()) {
                System.exit(0);
            }
            // 不强制更新
            else {
                closeMainToHomeActivity();
            }
        });
        builder.setNeutralButton("更新", (dialog, which) -> {
            versionUpdateDialog.dismiss();
            showUpdateDownload(versionUpdate);
        });
        builder.setCancelable(false);

        versionUpdateDialog = builder.create();
        versionUpdateDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /** 检查app初始化配置和数据是否已经完成（延迟1s执行） **/
        mHandler.postDelayed(() -> checkAppInitProgress(), 1000);
    }

    /**
     * app初始化进度
     */
    private void checkAppInitProgress() {
        /** 如果app已经初始化完成，则直接进入下一界面 **/
        if (ConvenientApplication.isAppInitFinish) {
            if (mIsAutoJump)
                aboutCheckVersionUpdate();
        }
        /** app没有初始化完成，注册广播监听器，监听初始化完成广播 **/
        else
            registerAppInitFinishBroadcastReceiver();
    }

    /**
     * 版本更新相关
     */
    private void aboutCheckVersionUpdate() {
        VersionUpdate versionUpdate = ConvenientApplication.getVersionUpdate();
        // 如果没有新版本，则直接进行后续工作
        if (!versionUpdate.hasNewVersion())
            closeMainToHomeActivity();
        else
            showVersionUpdateDialog(versionUpdate);
    }

    /**
     * 注册监听app初始化配置和数据完成广播
     */
    private void registerAppInitFinishBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConvenientApplication.ACTION_APP_INIT_FINISH);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {
            Log.d(ConvenientMainActivity.class.getSimpleName(), "没有注册app初始化广播，不需要解绑");
        }
        super.onDestroy();
    }

    /**
     * 设置app初始化配置和数据完成以后自动跳转（如需翻到最后一页点击进入的效果，请设置为false（默认true））
     *
     * @param isAutoJump
     */
    public void setAutoJump(boolean isAutoJump) {
        this.mIsAutoJump = isAutoJump;
    }

    /**
     * 显示下载更新
     */
    protected abstract void showUpdateDownload(VersionUpdate versionUpdate);

    protected abstract void closeMainToHomeActivity();

}
