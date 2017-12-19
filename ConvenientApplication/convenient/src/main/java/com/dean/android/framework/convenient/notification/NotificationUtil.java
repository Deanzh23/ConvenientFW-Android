package com.dean.android.framework.convenient.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.dean.android.framework.convenient.notification.exception.NotificationLackIconException;
import com.dean.android.framework.convenient.util.SetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知管理工具类
 * <p>
 * sIconRes（只需要/至少 设置一次） 和 方法中的 iconRes 必须至少设置一个
 * <p>
 * Created by Dean on 16/5/19.
 */
public class NotificationUtil {

    /**
     * 真正管理通知的Manager
     */
    private static NotificationManager sNotificationManager;
    /**
     * 装载所有被发出的通知的集合
     */
    private static Map<Integer, Notification> sNotificationMap = new HashMap<>();
    /**
     * 分类通知集合
     * key->分类
     * value->指定分类的通知集合
     */
    private static Map<Integer, List<Notification>> typeNotificationMap = new HashMap<>();
    /**
     * 通知图标资源ID
     */
    private static Integer sIconRes;

    private static void initManager(Context context) {
        if (sNotificationManager == null)
            sNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 设置通知图标资源ID
     *
     * @param iconRes
     */
    public static void setIconRes(int iconRes) {
        sIconRes = iconRes;
    }

    /**
     * 获取一个通知实例
     *
     * @param context
     * @param iconRes       图标
     * @param defaults      声音、震动 等
     * @param ticker        弹出显示的消息
     * @param title         标题
     * @param content       内容
     * @param pendingIntent 行为
     * @return
     */
    private static Notification getNotificationCompat(Context context, Integer iconRes, Integer defaults, String ticker, String title, String content, int type,
                                                      PendingIntent pendingIntent) throws NotificationLackIconException {
        Notification notification;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        /** 如果传递了图标资源，则使用传递的图标资源；如果没有传递，则判断是否设置了默认图标mIconRes **/
        if (iconRes != null)
            builder.setSmallIcon(iconRes);
        else if (sIconRes != null)
            builder.setSmallIcon(sIconRes.intValue());
        else
            throw new NotificationLackIconException();

        /** 声音、震动 等 **/
        builder.setDefaults(defaults == null ? Notification.DEFAULT_ALL : defaults.intValue());
        /** 设置标题 **/
        if (!TextUtils.isEmpty(title))
            builder.setContentTitle(title);
        /** 设置内容 **/
        if (!TextUtils.isEmpty(content))
            builder.setContentText(content);
        /** 弹出显示内容 **/
        if (!TextUtils.isEmpty(ticker))
            builder.setTicker(ticker);
        /** 右下角的数量标记 **/
        if (type == 0) {
            // 单个通知
            if (sNotificationMap.size() > 0) {
                builder.setNumber(sNotificationMap.size());
            }
            // 类型通知
            else {
                List<Notification> notifications = typeNotificationMap.get(type);
                if (SetUtil.isEmpty(notifications))
                    notifications = new ArrayList<>();

                builder.setNumber(notifications.size());
            }
        }
        /** 设置行为 **/
        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent);

        notification = builder.getNotification();

        return notification;
    }

    /**
     * 发送通知
     *
     * @param context
     * @param iconRes       图标
     * @param defaults      声音、震动 等
     * @param ticker        弹出显示的消息
     * @param title         标题
     * @param content       内容
     * @param pendingIntent 行为
     */
    public static void sendNotification(Context context, Integer iconRes, Integer defaults, String ticker, String title, String content, PendingIntent pendingIntent)
            throws NotificationLackIconException {

        initManager(context);

        Notification notification = getNotificationCompat(context, iconRes, defaults, ticker, title, content, 0, pendingIntent);
        sNotificationMap.put(sNotificationMap.size(), notification);

        // 发送通知
        sNotificationManager.notify(sNotificationMap.size() - 1, notification);
    }

    /**
     * 发送分类通知
     *
     * @param context
     * @param iconRes       图标
     * @param defaults      声音、震动 等
     * @param ticker        弹出显示的消息
     * @param title         标题
     * @param content       内容
     * @param pendingIntent 行为
     * @param type          类型
     * @param alternative   替换那个字符
     * @throws NotificationLackIconException
     */
    public static void sendNotification(Context context, Integer iconRes, Integer defaults, String ticker, String title, String content, PendingIntent pendingIntent,
                                        int type, String alternative) throws NotificationLackIconException {

        initManager(context);

        List<Notification> notifications = typeNotificationMap.get(type);
        if (SetUtil.isEmpty(notifications))
            notifications = new ArrayList<>();

        int count = SetUtil.isEmpty(notifications) ? 1 : notifications.size() + 1;
        content = content.replace(alternative, count > 99 ? "99+" : String.valueOf(count));

        Notification notification = getNotificationCompat(context, iconRes, defaults, ticker, title, content, type, pendingIntent);
        notifications.add(notification);
        typeNotificationMap.put(type, notifications);

        // 发送通知
        sNotificationManager.notify(type, notification);
    }

    /**
     * 清除所有通知
     */
    public static void clear() {
        sNotificationMap.clear();
    }

}
