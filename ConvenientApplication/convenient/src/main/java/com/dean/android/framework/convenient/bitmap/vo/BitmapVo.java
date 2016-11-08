package com.dean.android.framework.convenient.bitmap.vo;

import android.graphics.Bitmap;

/**
 * Bitmap Value Object
 * <p>
 * 包含 a.图片路径(如果是系统相册，可能为null)；b.bitmap实例
 * Created by Dean on 16/5/10.
 */
public class BitmapVo {

    private String mBitmapPath;

    private Bitmap mBitmap;

    public String getBitmapPath() {
        return mBitmapPath;
    }

    public void setBitmapPath(String bitmapPath) {
        mBitmapPath = bitmapPath;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
}
