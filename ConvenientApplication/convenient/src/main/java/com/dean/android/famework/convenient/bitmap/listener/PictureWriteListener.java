package com.dean.android.famework.convenient.bitmap.listener;

/**
 * 图片写入监听器
 * <p>
 * 监听 a.开始写入；b.写入完成；c.写入出错
 * Created by Dean on 16/5/10.
 */
public interface PictureWriteListener {

    void writeStart();

    void writeEnd();

    void writeError();

}
