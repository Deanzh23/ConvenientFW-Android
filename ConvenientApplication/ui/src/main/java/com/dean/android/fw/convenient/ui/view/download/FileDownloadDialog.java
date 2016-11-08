package com.dean.android.fw.convenient.ui.view.download;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dean.android.fw.convenient.ui.R;
import com.dean.android.fw.convenient.ui.view.loading.circle.RotationProgress;

import java.io.IOException;

import cn.com.dean.android.fw.convenientframework.file.download.FileDownloadRunnable;
import cn.com.dean.android.fw.convenientframework.file.download.listener.FileDownloadListener;
import cn.com.dean.android.fw.convenientframework.file.util.FileUitl;
import cn.com.dean.android.fw.convenientframework.format.MathFormatUtil;
import cn.com.dean.android.fw.convenientframework.toast.ToastUtil;

/**
 * 文件下载Dialog
 * <p>
 * Created by Dean on 2016/10/18.
 */
public class FileDownloadDialog {

    private Context context;

    private AlertDialog dialog;
    private RotationProgress rotationProgress;
    private TextView currentView, totalView;

    private String title;
    private String message;

    public FileDownloadDialog(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;

        initView();
    }

    /**
     * 初始化设置dialog view
     */
    private void initView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View rootView = LayoutInflater.from(context).inflate(R.layout.view_file_download, null);
        rotationProgress = (RotationProgress) rootView.findViewById(R.id.rotationProgress);
        currentView = (TextView) rootView.findViewById(R.id.currentView);
        totalView = (TextView) rootView.findViewById(R.id.totalView);

        builder.setView(rootView);
        builder.setCancelable(false);

        dialog = builder.create();
    }

    /**
     * 下载指定文件
     *
     * @param activity
     * @param strUrl
     * @param localPath
     * @param name
     * @param fileDownloadListener
     */
    public void download(Activity activity, String strUrl, String localPath, String name, final FileDownloadListener fileDownloadListener) {
        try {
            FileUitl.checkFile(localPath, name);
            new Thread(new FileDownloadRunnable(activity, strUrl, localPath, name, new FileDownloadListener() {
                @Override
                public void start() {
                    fileDownloadListener.start();
                }

                @Override
                public void success() {
                    dismiss();
                    fileDownloadListener.success();
                }

                @Override
                public void error(String error) {
                    dismiss();
                    fileDownloadListener.error(error);
                    ToastUtil.showToast(context, "下载失败:" + error);
                }

                @Override
                public void progress(int current, int total) {
                    setCurrent(MathFormatUtil.formatByte(current));
                    setTotal(MathFormatUtil.formatByte(total));
                }
            })).start();
        } catch (IOException e) {
            Log.e(FileDownloadDialog.class.getSimpleName(), "[download]--->" + e.getMessage());
        }
    }

    /**
     * 设置当前进度
     *
     * @param current
     */
    public void setCurrent(String current) {
        currentView.setText(current);
    }

    /**
     * 设置总大小
     *
     * @param total
     */
    public void setTotal(String total) {
        totalView.setText(total);
    }

    public void show() {
        if (!dialog.isShowing())
            dialog.show();
    }

    public void dismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

}
