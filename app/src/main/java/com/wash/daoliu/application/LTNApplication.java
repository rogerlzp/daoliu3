package com.wash.daoliu.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.wash.daoliu.model.BankItem;
import com.wash.daoliu.model.User;
import com.wash.daoliu.utility.FileHelper;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;
import com.wash.daoliu.utility.Utils;

import java.util.ArrayList;
import java.util.List;

//import cn.jpush.android.api.JPushInterface;

/**
 * Created by rogerlzp on 15/11/23.
 */
public class LTNApplication extends Application {

    private static LTNApplication instance;

    public static List<Activity> stackActivity = new ArrayList<Activity>();

    private boolean isShowGesture = false;
    private long background_time = 0;

    public boolean isShowAd() {
        return showAd;
    }

    public void setShowAd(boolean showAd) {
        this.showAd = showAd;
    }

    private boolean showAd = true;

    private ArrayList<BankItem> bankItems = null;

    public void setSessionKey(String sessionKey) {
        storeSessionKey(sessionKey);
    }


    {
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
//        PlatformConfig.setWeixin("wx48dd00fc989b7326", "97ff25e9b9389470fbc0470727ed1de6");
        //豆瓣RENREN平台目前只能在服务器端配置
        //新浪微博

        //       微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wx0f4a84e8caa54b9b", "0448899d0b7eb130d4e386a533032d14");
        //新浪微博
        //     PlatformConfig.setSinaWeibo("2130967206", "12726406159b3a564821b18b57de11eb");
        PlatformConfig.setQQZone("1106260004", "GY8TjVEbQljBEnlE");



    }

    public ArrayList<BankItem> getBankItems() {
        return bankItems;
    }

    public void setBankItems(ArrayList<BankItem> bankItems) {
        this.bankItems = bankItems;
    }

    public void setIsShowGesture(boolean isShowGesture) {

        this.isShowGesture = isShowGesture;
        if (isShowGesture) {
            background_time = System.currentTimeMillis();
            LogUtils.e(background_time + "" + isShowGesture);
        }
    }

    public boolean isShowGesture() {
        return isShowGesture;
    }

    public long getBackground_time() {
        return background_time;
    }

    public void setBackground_time(long background_time) {
        this.background_time = background_time;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        com.tendcloud.tenddata.TCAgent.init(this.getApplicationContext());
        // 测试版本
        TCAgent.setReportUncaughtExceptions(true);

        //Talking data
        TCAgent.LOG_ON=true;

        TCAgent.init(this);

        // 极光推送
//        JPushInterface.setDebugMode(LogUtils.isDebug);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this.getApplicationContext());
//        setNoticeStyle();
        setNotification();

//Umeng
        UMShareAPI.get(this);

        // 配置友盟共享
        Config.DEBUG = true;


    }

    public void setNotification() {
//        if (getNotification()) {
//            if (JPushInterface.isPushStopped(this)) {
//                JPushInterface.resumePush(this);
//            }
//        } else {
//            if (!JPushInterface.isPushStopped(this)) {
//                JPushInterface.stopPush(this);
//            }
//        }
    }

//    public void setNoticeStyle() {
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
//        builder.statusBarDrawable = R.drawable.icon160;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
//        if (getNotificationSound()) {
//            builder.notificationDefaults |= Notification.DEFAULT_SOUND;
//        }
//        if (getNotificationVibrate()) {
//            builder.notificationDefaults |= Notification.DEFAULT_VIBRATE;
//        }
//        JPushInterface.setDefaultPushNotificationBuilder(builder);
//    }

    public User getCurrentUser() {
        return User.getUserInstance();
    }

    //版本第一次打开之后关闭欢迎页显示
    public void setIsFirstLogin() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(LTNConstants.FIRST_LOGIN_FLAG + Utils.getVersionCode(LTNApplication.getInstance()), false);
        editor.commit();

    }

    //版本第一次打开之后关闭欢迎页显示
    public void setBankList(String bankJson) {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LTNConstants.BANK_LIST, bankJson);
        editor.commit();
    }

    public String getBankList() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        if (android.text.TextUtils.isEmpty(sp.getString(LTNConstants.BANK_LIST, null))) {
            String bankList = FileHelper.getFromAssets(this, LTNConstants.BANK_FILE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(LTNConstants.BANK_LIST, bankList);
            editor.commit();
        }
        return sp.getString(LTNConstants.BANK_LIST, null);
    }

    //设置推送
    public void setNotification(boolean isRecever) {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(LTNConstants.NOTIFICATION_RECEIVER, isRecever);
        editor.commit();
        setNotification();
    }

    public boolean getNotification() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        return sp.getBoolean(LTNConstants.NOTIFICATION_RECEIVER, true);
    }

    //设置声音
    public void setNotificationSound(boolean isRecever) {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(LTNConstants.NOTIFICATION_SOUND, isRecever);
        editor.commit();
//        setNoticeStyle();
    }

    public boolean getNotificationSound() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        return sp.getBoolean(LTNConstants.NOTIFICATION_SOUND, true);
    }

    //设置振动
    public void setNotificationVibrate(boolean isRecever) {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(LTNConstants.NOTIFICATION_VIBRATE, isRecever);
        editor.commit();
//        setNoticeStyle();
    }

    public boolean getNotificationVibrate() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        return sp.getBoolean(LTNConstants.NOTIFICATION_VIBRATE, true);
    }

    public boolean isFirstLogin() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        return sp.getBoolean(LTNConstants.FIRST_LOGIN_FLAG + Utils.getVersionCode(LTNApplication.getInstance()), true);
    }

    public void setLock() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(LTNConstants.IS_LOCK, true);
        editor.putLong(LTNConstants.LOCK_TIME, System.currentTimeMillis());
        editor.commit();

    }

    public void unLock() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(LTNConstants.IS_LOCK);
        editor.remove(LTNConstants.LOCK_TIME);
        editor.commit();

    }

    public boolean isLock() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        return sp.getBoolean(LTNConstants.IS_LOCK, false);
    }

    public long getLockTime() {
        SharedPreferences sp = getSharedPreferences(LTNConstants.LTN_CONFIG,
                Context.MODE_PRIVATE);
        return sp.getLong(LTNConstants.LOCK_TIME, 0);
    }

    public String getSessionKey() {
        SharedPreferences sp = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE);
        return sp.getString(LTNConstants.SESSION_KEY, null);
    }

    public void storeSessionKey(String sessionKey) {
        SharedPreferences.Editor editor = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE).edit();
        editor.putString(LTNConstants.SESSION_KEY, sessionKey);
        editor.commit();
    }


    public String getEggData() {
        SharedPreferences sp = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE);
        return sp.getString(LTNConstants.DT_URL, null);
    }

    public void storeEggData(String date) {
        SharedPreferences.Editor editor = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE).edit();
        editor.putString(LTNConstants.DT_URL, date);
        editor.commit();
    }


    /**
     * 获取 jpush reg id
     *
     * @return
     */
    public String getJPushID() {
        SharedPreferences sp = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE);
        return sp.getString(LTNConstants.JPUSH_REG_ID, null);
    }

    /**
     * 存取 jpush reg id
     */
    public void storeJPushID(String jpushId) {
        SharedPreferences.Editor editor = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE).edit();
        editor.putString(LTNConstants.JPUSH_REG_ID, jpushId);
        editor.commit();
    }


    public Boolean hasCheckedCurrent() {
        SharedPreferences sp = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE);
        return sp.getBoolean(LTNConstants.HAS_CHECKED_CURRENT, false);
    }

    public void setCheckedCurrent() {
        SharedPreferences.Editor editor = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE).edit();
        editor.putBoolean(LTNConstants.HAS_CHECKED_CURRENT, true);
        editor.commit();
    }


    public Boolean getShowMoeny() {
        SharedPreferences sp = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE);
        return sp.getBoolean(LTNConstants.SHOW_MONEY, true);
    }

    public void setShowMoney(boolean showMoneyFlag) {
        SharedPreferences.Editor editor = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE).edit();
        editor.putBoolean(LTNConstants.SHOW_MONEY, showMoneyFlag);
        editor.commit();
    }


    public String getGesturePassword() {
        SharedPreferences sp = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE);
        return sp.getString(LTNConstants.GESTURE_PASSWORD, null);
    }

    public void storeGesturePassword(String gesturePassword) {
        SharedPreferences.Editor editor = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE).edit();
        editor.putString(LTNConstants.GESTURE_PASSWORD, gesturePassword);
        editor.commit();
    }

    public void clearGesturePassword() {
        SharedPreferences.Editor editor = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE).edit();
        editor.remove(LTNConstants.GESTURE_PASSWORD);
        editor.commit();
    }

    public void clearUser() {
        SharedPreferences.Editor editor = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE).edit();
//        editor.remove(LTNConstants.GESTURE_PASSWORD);
        editor.remove(LTNConstants.SESSION_KEY);
        // 删除show money key
        editor.remove(LTNConstants.SHOW_MONEY);

        //
        editor.remove(LTNConstants.DT_URL);

        editor.commit();

        User.clearUser();
    }

    public String getVersion() {
        String version = null;
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static LTNApplication getInstance() {
        return instance;
    }

    public String getUserMobile() {
        SharedPreferences sp = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE);
        return sp.getString(LTNConstants.USER_MOBILE, null);
    }

    public void setUserMobile(String mobile) {
        SharedPreferences.Editor editor = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE).edit();
        editor.putString(LTNConstants.USER_MOBILE, mobile);
        editor.commit();
    }
}
