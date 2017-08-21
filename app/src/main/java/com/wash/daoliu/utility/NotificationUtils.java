package com.wash.daoliu.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.AccountInfoActivity;
import com.wash.daoliu.activities.MainActivity;
import com.wash.daoliu.application.LTNApplication;

//import cn.jpush.android.api.JPushInterface;
//
/**
 * Created by jiajia on 2016/2/17.
 */
public class NotificationUtils {
    public static int count = 0;

    public static void showNotification(Context context, String message) {
        count++;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //API level 11
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("新消息");
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.logo);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.getNotification();
        if (LTNApplication.getInstance().getNotificationSound()) {
            notification.defaults |= Notification.DEFAULT_SOUND;
        }
        if (LTNApplication.getInstance().getNotificationVibrate()) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(count, notification);
    }

    public static void showNotificationFromBundle(Context context, Bundle bundle) {
        count++;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //API level 11
        Notification.Builder builder = new Notification.Builder(context);
//        builder.setContentTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
//
//        builder.setContentText(bundle.getString(JPushInterface.EXTRA_ALERT));
//        builder.setSmallIcon(R.drawable.icon160);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, AccountInfoActivity.class), PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.getNotification();
        if (LTNApplication.getInstance().getNotificationSound()) {
            notification.defaults |= Notification.DEFAULT_SOUND;
        }
        if (LTNApplication.getInstance().getNotificationVibrate()) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(count, notification);
    }

}
