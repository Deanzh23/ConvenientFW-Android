package com.dean.android.framework.convenient.voice;

import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * 音频工具类
 * <p>
 * Created by dean on 2017/4/6.
 */
public class VoiceUtil {

    private static MediaRecorder mediaRecorder;

    /**
     * 初始化编码器
     *
     * @param path
     * @param fileName
     * @return
     */
    private static void initMediaRecorder(String path, String fileName) throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + (TextUtils.isEmpty(path) ? "" : "/" + path));
        File file = new File(dir.getPath(), fileName);

        if (!file.exists() || !file.isFile()) {
            if (!TextUtils.isEmpty(path))
                dir.mkdirs();

            file.createNewFile();
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(file.getPath());
    }

    /**
     * 开始录音
     *
     * @param path
     * @param fileName
     * @throws IOException
     */
    public static void startRecord(String path, String fileName) throws IOException {
        if (TextUtils.isEmpty(fileName))
            return;

        initMediaRecorder(path, fileName);

        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    /**
     * 停止录制
     */
    public static void stopRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
        }
    }

}
