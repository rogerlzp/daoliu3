package com.wash.daoliu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.wash.daoliu.activities.CouponsActivity;
import com.wash.daoliu.activities.LTNWebPageActivity;
import com.wash.daoliu.activities.MainActivity;
import com.wash.daoliu.activities.MyInvestActivity;
import com.wash.daoliu.activities.ProductDetailActivity;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.JPushMessage;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;

//import cn.jpush.android.api.JPushInterface;

/**
 * Created by rogerlzp on 16/2/3.
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private static final String TAG = MyJPushReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            LogUtils.d("regId" + regId);
//            // 如果换了登录时候,需要重新启动吗?
//
//            LTNApplication.getInstance().storeJPushID(regId);
//
//        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            //json
//            LogUtils.d("" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
////            if (LTNApplication.getInstance().getNotification()&&!TextUtils.isEmpty(message)) {
//            //    NotificationUtils.showNotification(context, message);
////            }
//        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//            String jsonString = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
//            String notification = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//            // TODO: 增加跳转
//            //      NotificationUtils.showNotificationFromBundle(context, bundle);
//
//
//        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            LogUtils.d("[MyReceiver] 用户点击打开了通知");
//
//            String jsonString = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Gson gson = new Gson();
//            JPushMessage jpMessage = null;
//            if (jsonString != null) {
//                jpMessage = gson.fromJson(jsonString.toString(), JPushMessage.class);
//            }
//            if (jpMessage != null) {
//
//                Intent i = null;
//                //打开自定义的Activity
//                if (jpMessage.getUrl().equals("my/product/status/4")) {
//                    i = new Intent(context, MyInvestActivity.class);
//                    bundle.putInt(LTNConstants.CURRENT_PAGE, 3);
//
//                } else if (jpMessage.getUrl().equals("my/product/status/3")) {
//                    i = new Intent(context, MyInvestActivity.class);
//                    bundle.putInt(LTNConstants.CURRENT_PAGE, 2);
//                } else if (jpMessage.getUrl().equals("my/product/status/2")) {
//                    i = new Intent(context, MyInvestActivity.class);
//                    bundle.putInt(LTNConstants.CURRENT_PAGE, 1);
//                } else if (jpMessage.getUrl().equals("coupon/notice")) {
//                    i = new Intent(context, CouponsActivity.class);
//                } else if (jpMessage.getUrl().matches("product/.*")) {
//                    i = new Intent(context, ProductDetailActivity.class);
//                    int startLength = jpMessage.getUrl().indexOf("/");
//                    bundle.putString(LTNConstants.PRO_Id, jpMessage.getUrl().substring(startLength+1, jpMessage.getUrl().length()));
//                    bundle.putString(LTNConstants.FROM_JPUSH, "1");
//                } else if (jpMessage.getUrl().matches("h5.*")) {
//
//                    i = new Intent(context, LTNWebPageActivity.class);
//                    bundle.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.HOST + "/" + jpMessage.getUrl());
//                    intent.putExtras(bundle);
//
//                } else {
//                    i = new Intent(context, MainActivity.class);
//                }
//
//                i.putExtras(bundle);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(i);
//            }
//
//
//        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//            LogUtils.d("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//
//        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//            LogUtils.w("[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
//        } else {
//            LogUtils.d("[MyReceiver] Unhandled intent - " + intent.getAction());
//        }
    }


}
