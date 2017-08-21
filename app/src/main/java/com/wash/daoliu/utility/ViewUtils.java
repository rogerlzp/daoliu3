package com.wash.daoliu.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.view.DialogUI;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: bobo Date: 13-6-26 Time: 下午5:13 To change
 * this template use File | Settings | File Templates.
 */
public class ViewUtils {

    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static float scaledDensity = 1.0f;
    public static float density = 1.0f;

    /**
     * 返回自己Activity
     *
     * @param context
     * @param clazz
     */
    public static void callMe(Context context, Class clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    /**
     * 返回自己Activity
     *
     * @param context
     * @param clazz
     */
    public static void callMeWithIntent(Context context, Class clazz, Intent intent) {
        intent.setClass(context, clazz);
        context.startActivity(intent);
    }

    /**
     * 计算界面宽高
     *
     * @param activity
     */
    public static void calculateScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        scaledDensity = dm.scaledDensity;
        density = dm.density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 隐藏界面标题栏
     *
     * @param activity
     */
    public static void hideTitle(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 全屏
     *
     * @param activity
     */
    public static void fullScreen(Activity activity) {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 获取设备基本信息
     *
     * @param context
     */
    @SuppressLint("LongLogTag")
    public static void getDeviceInfo(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String packageName = info.packageName;
            int sysVerCode = info.versionCode;
            String sysVerName = info.versionName;
            // cache sysVersionName
            SharedPrefsUtil.putString(context, Constant.Share.SHARE_SYS_VERSION_NAME, sysVerName);
            SharedPrefsUtil.putInt(context, Constant.Share.SHARE_SYS_VERSION_CODE, sysVerCode);
            SharedPrefsUtil.putString(context, Constant.Share.SHARE_PACKAGE_NAME, packageName);

            // 定义手持管理类，获取设备号信息
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//			String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceId = tm.getDeviceId();
            // cache imei
            SharedPrefsUtil.putString(context, Constant.Share.SHARE_IMEI, deviceId);
            SharedPrefsUtil.putString(context, Constant.Share.SHARE_SIM_NO, tm.getSimSerialNumber());
        } catch (PackageManager.NameNotFoundException e) {
            // Log.e("PackageManager.NameNotFoundException", e.getMessage());
        }
    }

    /**
     * 判断网络状态
     *
     * @param context
     * @return
     */
    public static boolean isNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                Constant.Variable.NETWORK_NAME = networkInfo.getTypeName();
                return true;
            }
        }
        return false;
    }

    /**
     * 获取网络状态类型
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return networkInfo.getTypeName();
            }
        }
        return null;
    }

    /**
     * 判断服务是否已启动
     *
     * @param serviceName
     * @return
     */
    public static boolean isRunning(Context context, String serviceName) {
        ActivityManager myAM = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServices = (ArrayList<ActivityManager.RunningServiceInfo>) myAM
                .getRunningServices(80);
        // 获取最多40个当前正在运行的服务，放进ArrList里,以现在手机的处理能力，要是超过40个服务，估计已经卡死，所以不用考虑超过40个该怎么办
        for (int i = 0; i < runningServices.size(); i++) { // 循环枚举对比
            if ((runningServices.get(i).service.getClassName().toString())
                    .equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 后台服务
     */
    public static final String SERVICE_NAME_PUSH = "com.xizi.app.activity.PushService";

    public static final String SERVICE_NAME_LOCATION = "com.quanminbb.app.activity.service.LocationService";

    public static final String SERVICE_NAME_UPLOAD = "com.quanminbb.app.activity.service.UploadService";

    /**
     * 开启服务
     *
     * @param context
     */
    public static void launchServices(Context context) {
        if (!isRunning(context, SERVICE_NAME_LOCATION)) {
//			context.startService(new Intent(context, LocationService.class));
        }
        if (!isRunning(context, SERVICE_NAME_UPLOAD)) {
//			context.startService(new Intent(context, UploadService.class));
        }
    }

    /**
     * 停止所有服务
     *
     * @param context
     */
    public static void stopServices(Context context) {
        if (isRunning(context, SERVICE_NAME_LOCATION)) {
//			context.stopService(new Intent(context, LocationService.class));
        }
        if (isRunning(context, SERVICE_NAME_UPLOAD)) {
//			context.stopService(new Intent(context, UploadService.class));
        }
    }

    /**
     * WIFI网络开关
     * TODO: 没有使用,因此关闭
     */
    public static void toggleWiFi(Context context, boolean enabled) {
        WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wm.setWifiEnabled(enabled);
    }

    /**
     * 移动网络开关
     */
    public static void toggleMobileData(Context context, boolean enabled) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> conMgrClass = null; // ConnectivityManager类
        AtomicReference<Field> iConMgrField = new AtomicReference<Field>(null); // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法
        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.getClass().getName());
            // 取得ConnectivityManager类中的对象mService
            iConMgrField.set(conMgrClass.getDeclaredField("mService"));
            // 设置mService可访问
            iConMgrField.get().setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get().get(conMgr);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            // 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod.setAccessible(true);
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断GPS开关状态
     */
    private void isOpenGps(Activity act) {
        LocationManager alm = (LocationManager) act
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // DialogUI.showToastShort(IndexActivity.this.getParent(),
            // "GPS模块正常");
            return;
        } else {
            DialogUI.showGpsSecuritySetting(act, "请先进入安全设置里，开启GSP开关", false);
        }
    }

    /**
     * <p/>
     * GPS开关
     * <p/>
     * 当前若关则打开
     * <p/>
     * 当前若开则关闭
     */
    private void toggleGPS(Context context) {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理运行内存
     *
     * @param context
     */
    public static void clearMemory(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ActivityManager activityManger = (ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> list = activityManger
                        .getRunningAppProcesses();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        ActivityManager.RunningAppProcessInfo apinfo = list
                                .get(i);
                        String[] pkgList = apinfo.pkgList;
                        if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                            for (int j = 0; j < pkgList.length; j++) {
                                activityManger
                                        .killBackgroundProcesses(pkgList[j]);
                            }
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * uninstall apk file
     *
     * @param context
     */
    public static void uninstallAPK(Context context) {
        Uri uri = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static String cmd_install = "pm install -r ";
    private static String cmd_uninstall = "pm uninstall ";

    public static void install(String apkPath) {
        String cmd = cmd_install + apkPath;
        excuteSuCMD(cmd, null);
    }

    public static void uninstall(String packageName) {
        String cmd = cmd_uninstall + packageName;
        // String cmd = cmd_uninstall + "com.xizi.app.activity";
        int i = excuteSuCMD(cmd, packageName);
    }

    protected static int excuteSuCMD(String cmd, String packageName) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(
                    (OutputStream) process.getOutputStream());
            // 部分手机Root之后Library path 丢失，导入path可解决该问题
            // dos.writeBytes((String)
            // "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
            cmd = String.valueOf(cmd);
            // dos.writeBytes("chmod 777 " + packageName + " \n");
            dos.writeBytes((String) (cmd + "\n"));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            int result = process.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }

    public static void exitActivity() {
        for (Activity actTemp : LTNApplication.getInstance().stackActivity) {
            if (!actTemp.isFinishing()) {
                actTemp.finish();
            }
        }
        System.exit(0);
    }

    /**
     * 打开键盘
     *
     * @param context
     * @param view
     */
    public static void openKeyBoard(Context context, View view) {
        InputMethodManager imm = null;
        if (context != null) {
            imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        }
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 隐藏键盘
     */
    public static void closeKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            View currentFocus = context.getCurrentFocus();
            if (currentFocus != null) {
                imm.hideSoftInputFromWindow(currentFocus.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 判断软键盘是否打开状态
     */
    public static boolean isOpenKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        return isOpen;
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context
     * @return TODO: update getWidth and getHeight later
     */
    public static int[] getScreenDisplay(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int result[] = {width, height};
        return result;

    }

    //拨打客服电话
    public static void showCallDialog(final Context context, final String phone) {
        // TODO Auto-generated method stub
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.call_kefu);
        TextView pTv = (TextView) window.findViewById(R.id.phone_text);
        pTv.setText(phone);
        // 为确认按钮添加事件,执行退出应用操作
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_CALL);   //android.intent.action.DIAL
                i.setData(Uri.parse("tel:" + phone));
                context.startActivity(i);
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }

    //拨打客服电话
    public static void showBirdCoinHelpDialog(final Context context) {
        // TODO Auto-generated method stub
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(true);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.birdcoin_help);

        ImageView pTv = (ImageView) window.findViewById(R.id.iv_bird_coin_close);

        // 为确认按钮添加事件,执行退出应用操作
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        pTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }

    //弹出通用提示
    public static void showWarnDialog(Context context, String warnText, String okText, final View.OnClickListener onOkClick) {
        // TODO Auto-generated method stub
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(true);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.warn_dialog);
        TextView content = (TextView) window.findViewById(R.id.warn_text);
        content.setText(warnText);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.ok);
        ok.setText(okText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick.onClick(v);
                dlg.cancel();
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
     //弹出通用提示
    public static void showWarnDialogWithBold(Context context,String title ,String warnText, String okText, final View.OnClickListener onOkClick) {
        // TODO Auto-generated method stub
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(true);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.warn_dialog_bold_text);
        TextView titleText = (TextView) window.findViewById(R.id.title);
        titleText.setText(title);
        TextView content = (TextView) window.findViewById(R.id.warn_text);
        content.setText(warnText);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.ok);
        ok.setText(okText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick.onClick(v);
                dlg.cancel();
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
    public static void showWarnDialogWithBold2(Context context,String title ,String warnText, String okText, final View.OnClickListener onOkClick) {
        // TODO Auto-generated method stub
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(true);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.warn_dialog_bold_text_2);
        TextView titleText = (TextView) window.findViewById(R.id.title);
        titleText.setText(title);
        TextView content = (TextView) window.findViewById(R.id.warn_text);
        content.setText(warnText);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.ok);
        ok.setText(okText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick.onClick(v);
                dlg.cancel();
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
    //弹出通用提示
    public static void showWarnDialogWithTitle(Context context,String titleText, String warnText, String okText,String cancelText, final View.OnClickListener onOkClick,final View.OnClickListener onCancelClick,boolean cancelable) {
        // TODO Auto-generated method stub
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(cancelable);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.warn_dialog_with_title);
        TextView content = (TextView) window.findViewById(R.id.warn_text);
        TextView title = (TextView) window.findViewById(R.id.title);
        title.setText(titleText);
        content.setText(warnText);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.ok);
        ok.setText(okText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick.onClick(v);
                dlg.cancel();
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.cancel);
        cancel.setText(cancelText);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCancelClick.onClick(v);
                dlg.cancel();
            }
        });
    }
    //弹出error通用提示
    public static void showErrorDialogWithTitle(Context context,String titleText, String warnText, String okText,final View.OnClickListener onOkClick) {
        // TODO Auto-generated method stub
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.warn_dialog_with_title);
        TextView content = (TextView) window.findViewById(R.id.warn_text);
        TextView title = (TextView) window.findViewById(R.id.title);
        title.setText(titleText);
        content.setText(warnText);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.ok);
        ok.setText(okText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOkClick!=null){
                    onOkClick.onClick(v);
                }
                dlg.cancel();
            }
        });
        window.findViewById(R.id.divide_1).setVisibility(View.GONE);
        window.findViewById(R.id.cancel).setVisibility(View.GONE);
    }
    //弹出通用提示
    public static void showWarnDialog2(Context context, String warnText, String okText, String cancelText, final View.OnClickListener onOkClick, final View.OnClickListener
            onCancelClick) {
        // TODO Auto-generated method stub
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(true);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.warn_dialog_2);
        TextView content = (TextView) window.findViewById(R.id.warn_text);
        content.setText(warnText);
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.ok);
        ok.setText(okText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick.onClick(v);
                dlg.cancel();
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.cancel);
        cancel.setText(cancelText);
        if (onCancelClick == null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dlg.cancel();
                }
            });
        } else {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCancelClick.onClick(v);
                    dlg.cancel();
                }
            });
        }
    }

    //弹出通用提示
//    public static void showWarnDialog3(Context context, String warnText, String okText, final View.OnClickListener onOkClick) {
//        // TODO Auto-generated method stub
//        final AlertDialog dlg = new AlertDiaLog.Builder(context).create();
//        dlg.setCancelable(true);
//        dlg.show();
//        Window window = dlg.getWindow();
//        // *** 主要就是在这里实现这种效果的.
//        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
//        window.setContentView(R.layout.fragment_dialog3);
//        TextView content = (TextView) window.findViewById(R.id.warn_text);
//        content.setText(warnText);
//        // 为确认按钮添加事件,执行退出应用操作
//        TextView ok = (TextView) window.findViewById(R.id.ok);
//        ok.setText(okText);
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOkClick.onClick(v);
//                dlg.cancel();
//            }
//        });
//
//        TextView cancel = (TextView) window.findViewById(R.id.cancel);
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dlg.cancel();
//            }
//        });
//    }
//    //弹出通用提示
//    public static void showInfoDialog(Context context, String warnText,  String okText,final View.OnClickListener onOkClick) {
//        // TODO Auto-generated method stub
//        final AlertDialog dlg = new AlertDiaLog.Builder(context).create();
//        dlg.setCancelable(true);
//        dlg.show();
//        Window window = dlg.getWindow();
//        TextView content = (TextView) window.findViewById(R.id.warn_text);
//        content.setText(warnText);
//        // *** 主要就是在这里实现这种效果的.
//        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
//        window.setContentView(R.layout.fragment_one_button);
//        // 为确认按钮添加事件,执行退出应用操作
//        TextView ok = (TextView) window.findViewById(R.id.btn_next);
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             //   onOkClick.onClick(v);
//                dlg.cancel();
//            }
//        });
//
//    }
}
