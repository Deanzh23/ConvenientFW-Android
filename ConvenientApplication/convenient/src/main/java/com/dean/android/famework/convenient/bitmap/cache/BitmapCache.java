package com.dean.android.famework.convenient.bitmap.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dean.android.famework.convenient.bitmap.util.BitmapUtil;

import java.io.FileNotFoundException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 位图缓存
 * <p>
 * 用于控制OOM。
 * 因为使用了软引用，当系统内存不足发生GC时，自动回收没有被引用的Bitmap。
 * <p>
 * Created by Dean on 16/5/10.
 */
public class BitmapCache {

    public static BitmapCache instance;

    public static BitmapCache getInstance() {
        if (instance == null)
            instance = new BitmapCache();

        return instance;
    }

    private BitmapCache() {
    }

    /**
     * 软引用的 Bitmap内存缓存容器
     */
    private Map<String, SoftReference<Bitmap>> mBitmapMap = new HashMap<>();

    /**
     * 获取 Bitmap（经测试，每张16M左右，同时加载10张没毛病－－正常没屏也就能显示6张图）
     *
     * @param bitmapPath
     * @return
     */
    public Bitmap getBitmap(String bitmapPath, boolean isInSample, boolean isFromCache) throws FileNotFoundException {
        SoftReference<Bitmap> bitmapSoftReference = null;

        if (isFromCache) {
            /** 检索mBitmapMap中是否存在要找的Bitmap **/
            bitmapSoftReference = mBitmapMap.get(bitmapPath);
        }

        if (bitmapSoftReference == null || bitmapSoftReference.get() == null) {
            /** 生成Bitmap **/
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (isInSample)
                options.inSampleSize = BitmapUtil.getCompressionCoefficient(bitmapPath);

            bitmapSoftReference = new SoftReference<>(BitmapFactory.decodeFile(bitmapPath, options));
            /** 保存到mBitmapMap **/
            if (bitmapSoftReference.get() != null) {
                mBitmapMap.put(bitmapPath, bitmapSoftReference);
            }
        }
        return bitmapSoftReference.get();

    }


}
