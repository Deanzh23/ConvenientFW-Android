package com.dean.android.famework.convenient.bitmap.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.dean.android.famework.convenient.bitmap.cache.BitmapCache;
import com.dean.android.famework.convenient.bitmap.listener.BitmapDownloadListener;
import com.dean.android.famework.convenient.bitmap.listener.PictureWriteListener;
import com.dean.android.famework.convenient.exception.FileException;
import com.dean.android.famework.convenient.exception.HasNoPermissionException;
import com.dean.android.famework.convenient.permission.util.PermissionsUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Bitmap Util
 * <p>
 * 1.打开系统相册
 * 2.将从相册选择的图片转换成BitmapVo
 * 3.保存图片到指定路径
 * 4.指定路径下的文件是否存在
 * 5.保存图片到指定路径
 * 6.获取 Bitmap ，指定是否需要压缩
 * 7.将指定图片设置到指定ImageView（主要作用于ListView等位置中）
 * 8.图片下载并保存在本地指定位置（指定文件名）
 * 9.打开系统相机
 * <p>
 * Created by Dean on 16/5/10.
 */
public class BitmapUtil {

    /**
     * 系统Action的类型（用于打开相册的参数）
     */
    private static final String INTENT_ACTION_TYPE = "image/*";
    /**
     * 打开相册的 onActivityResult requestCode
     */
    public static final int ACTION_OPEN_PHOTO_ALBUM = 0;
    /**
     * 打开系统相机的 onActivityResult requestCode
     */
    public static final int ACTION_OPEN_CAMERA = 1;

    /**
     * 打开系统相机
     * <p>
     * 需要 <uses-permission android:name="android.permission.CAMERA"/> 权限
     *
     * @param context
     */
    public static void openSystemCamera(Context context, String path, String fileSavePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (!file.exists())
                file.mkdirs();
        } else
            path = Environment.getExternalStorageDirectory().getPath();

        if (!TextUtils.isEmpty(fileSavePath)) {
            File file = new File(path + "/" + fileSavePath);
            if (!file.exists())
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            Uri uri = Uri.fromFile(file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        if (context instanceof Activity)
            ((Activity) context).startActivityForResult(intent, ACTION_OPEN_CAMERA);
    }

    /**
     * 打开系统相册
     * <p>
     * 需要在Activity中实现 onActivityResult(int requestCode, int resultCode, Intent data)
     * 可以在 onActivityResult 中使用
     * <p>
     * (1)BitmapUtil.toBitmapVo 获取图片/路径
     *
     * @param context
     */
    public static void openSystemPhotoAlbum(Context context) {
        // 方式一
//        Intent intent = new Intent();
//        intent.setType(INTENT_ACTION_TYPE);
//        intent.setAction(Intent.ACTION_GET_CONTENT);

        // 方式二
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (context instanceof Activity)
            ((Activity) context).startActivityForResult(intent, ACTION_OPEN_PHOTO_ALBUM);
    }

    /**
     * 从相册选择图片后获取图片路径
     *
     * @param activity
     * @param data
     * @return
     */
    public static String intent2ImagePath(Activity activity, Intent data) {
        try {
            // 统用手机获取相册图片路径
            Uri imageUri = data.getData();
            return getImagePathByUri(activity, imageUri);
        } catch (Exception e) {
            // 小米手机获取相册图片路径
            return getImagePathByUri(activity, getImageUriFromPhotoAlbumByXiaomi(activity, data));
        }
    }

    private static String getImagePathByUri(Activity activity, Uri imageUri) {
        String[] imageArray = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(imageUri, imageArray, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    private static Uri getImageUriFromPhotoAlbumByXiaomi(Activity activity, Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = activity.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }

        return uri;
    }

    /**
     * 获取 Bitmap ，指定是否需要压缩
     *
     * @param bitmapPath
     * @param isInSample
     * @return
     */
    public static Bitmap getBitmap(String bitmapPath, boolean isInSample, boolean isFromCache) throws FileNotFoundException {
        BitmapCache sBitmapCache = BitmapCache.getInstance();
        return sBitmapCache.getBitmap(bitmapPath, isInSample, isFromCache);
    }

    /**
     * 获取图片大小
     *
     * @param bitmapPath
     * @return
     * @throws FileNotFoundException
     */
    public static int getCompressionCoefficient(String bitmapPath) throws FileNotFoundException {
        File file = new File(bitmapPath);
        if (!file.exists())
            throw new FileNotFoundException();

        long fileLength = file.length();
        Log.d(BitmapUtil.class.getSimpleName(), "fileLength is " + fileLength);

        int coefficient = 4;
        coefficient = (int) (fileLength / (1024 * 1024) * coefficient);
        Log.d(BitmapUtil.class.getSimpleName(), "coefficient is " + coefficient);

        return coefficient;
    }

    /**
     * 将指定图片设置到指定ImageView（默认压缩，主要作用于ListView等位置中）
     *
     * @param activity
     * @param imageView            要显示到的ImageView
     * @param bitmapPath           图片资料本地路径
     * @param isFromCache          是否从缓存读取
     * @param defaultPicResourceId 图片未加载出来前显示的默认图片资源ID
     * @param isSetOnBackground    是否装载到背景
     */
    private static synchronized void setBitmap2View(final Activity activity, final ImageView imageView, final String bitmapPath, final boolean isFromCache, Integer
            defaultPicResourceId, final boolean isSetOnBackground) {
        /** 设置图片未加载出来前显示的默认图片 **/
        if (defaultPicResourceId != null)
            imageView.setImageResource(defaultPicResourceId);

        /** 异步加载图片 **/
        new Thread(new Runnable() {
            @Override
            public void run() {
                /** 通过 BitmapCache 生成 Bitmap **/
                try {
                    final Bitmap bitmap = getBitmap(bitmapPath, true, isFromCache);
                    /** 回到UI线程中设置图片显示 **/
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /** 打完收工 **/
                            if (!isSetOnBackground)
                                imageView.setImageBitmap(bitmap);
                            else
                                imageView.setBackground(new BitmapDrawable(bitmap));
                        }
                    });
                } catch (FileNotFoundException e) {
                }
            }
        }).start();
    }

    /**
     * 将指定图片设置到指定ImageView（默认压缩，主要作用于ListView等位置中）
     *
     * @param activity
     * @param imageView            要显示到的ImageView
     * @param bitmapPath           图片资料本地路径
     * @param isFromCache          是否从缓存读取
     * @param defaultPicResourceId 图片未加载出来前显示的默认图片资源ID
     */
    public static synchronized void setBitmap2ViewOnBackground(final Activity activity, final ImageView imageView, final String bitmapPath,
                                                               final boolean isFromCache, Integer defaultPicResourceId) {
        setBitmap2View(activity, imageView, bitmapPath, isFromCache, defaultPicResourceId, true);
    }

    /**
     * 将指定图片设置到指定ImageView（默认压缩，主要作用于ListView等位置中）
     *
     * @param activity
     * @param imageView            要显示到的ImageView
     * @param bitmapPath           图片资料本地路径
     * @param isFromCache          是否从缓存读取
     * @param defaultPicResourceId 图片未加载出来前显示的默认图片资源ID
     */
    public static synchronized void setBitmap2ViewOnImageBitmap(final Activity activity, final ImageView imageView, final String bitmapPath,
                                                                final boolean isFromCache, Integer defaultPicResourceId) {
        setBitmap2View(activity, imageView, bitmapPath, isFromCache, defaultPicResourceId, false);
    }

    /**
     * 指定路径下的文件是否存在
     *
     * @param bitmapPath
     * @return
     */
    public static boolean hasBitmapBySDCard(String bitmapPath) {
        File bitmapFile = new File(bitmapPath);
        return bitmapFile.exists();
    }

    /**
     * * 保存图片到指定路径
     * <p>
     * 需要添加<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>权限
     * <p>
     *
     * @param activity
     * @param bitmap               bitmap实例
     * @param bitmapName           bitmap名称
     * @param sdCardPath           要保存到的目录
     * @param pictureWriteListener 写入状态监听器
     * @throws HasNoPermissionException 没有SD卡写入权限异常
     */
    public static void saveBitmapToSDCard(final Activity activity, final Bitmap bitmap, final String bitmapName, final String sdCardPath, final PictureWriteListener
            pictureWriteListener) throws HasNoPermissionException {

        /** 检查SD卡写入权限 **/
        if (!PermissionsUtil.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            throw new HasNoPermissionException();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                File directory = new File(sdCardPath);
                File bitmapFile = new File(directory, bitmapName);
                /** 创建路径 **/
                if (!directory.exists())
                    directory.mkdirs();
                /** 替换文件 **/
                if (bitmapFile.exists())
                    bitmapFile.delete();

                if (pictureWriteListener != null)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pictureWriteListener.writeStart();
                        }
                    });
                try {
                    writeToSDCard(bitmap, bitmapFile);
                } catch (Exception e) {
                    if (pictureWriteListener != null)
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pictureWriteListener.writeError();
                            }
                        });
                }
                if (pictureWriteListener != null)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pictureWriteListener.writeEnd();
                        }
                    });
            }
        }).start();
    }

    /**
     * 图片下载并保存在本地指定位置（指定文件名）
     * <p>
     * 需要 <uses-permission android:name="android.permission.INTERNET"/> 权限
     * <p>
     *
     * @param activity
     * @param url                    下载地址
     * @param localPath              本地存储路径
     * @param pictureName            图片文件名
     * @param bitmapDownloadListener 下载状态监听器
     */
    public static void download(Activity activity, String url, String localPath, String pictureName, BitmapDownloadListener bitmapDownloadListener) {
        BitmapDownloadUtil.download(activity, url, localPath, pictureName, bitmapDownloadListener);
    }

    /**
     * 图片写入
     *
     * @param bitmap
     * @param bitmapFile
     */
    private static void writeToSDCard(Bitmap bitmap, File bitmapFile) throws FileException, HasNoPermissionException, IOException {
        if (bitmap == null)
            throw new FileException();

        FileOutputStream fileOutputStream;
        fileOutputStream = new FileOutputStream(bitmapFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.flush();
        try {
            if (fileOutputStream != null)
                fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
