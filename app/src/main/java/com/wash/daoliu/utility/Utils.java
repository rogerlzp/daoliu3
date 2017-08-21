package com.wash.daoliu.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.BindBankCardActivity;
import com.wash.daoliu.activities.LoginActivity;
import com.wash.daoliu.activities.UserAuthActivity;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/11/24.
 */
public class Utils {
    // IMEI 设备号
    public String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 检查手机号码格式
     * 判断是否为11位数字和开头是否为"13 14 15 18 "
     *
     * @param _mobile
     * @return
     */
    public static boolean isValidMobile(String _mobile) {
        if (_mobile != null && _mobile.length() == 11
                && (_mobile.startsWith("13") || _mobile.startsWith("15")
                || _mobile.startsWith("18") || _mobile
                .startsWith("14") || _mobile
                .startsWith("17"))) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 产生UUID 等图片验证码使用
     *
     * @return
     */
    public static String genUUid() {
        return UUID.randomUUID().toString();
    }


    public static boolean checkDateIsEnd(String date) {
        // 当前时间
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean isBefore = false;
        try {
//            System.currentTimeMillis()
            isBefore = dfs.parse(date).getTime() <= new Date().getTime();
        } catch (Exception e) {
        }
        return isBefore;
    }

    public static void checkVersion(final Activity context) {

        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.APP_VERSION, getVersionCode(context));
        // 如果有sessionKey.则添加sessionKey

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.CHECK_UPDATA, mReqParams,
                new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                try {
                    // Log.e("TAG", jsonObject.toString());
                    String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                    // 验证码错误
                    if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                        JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                        String hasUpdate = resultObj.optString("hasUpdate");
                        if (hasUpdate.equals("1")) {
                            String forceUpdate = resultObj.optString("forceUpdate");
                            String updateInfo = resultObj.optString("updateInfo");
                            String url = resultObj.optString("downloadUrl");
                            UpdateManager updateManager = new UpdateManager(context, url, forceUpdate.equals("1"));
                            updateManager.checkUpdate(updateInfo, url);
                        }
                    }
                } catch (JSONException je) {
                }

            }
        });

    }

    public static String getVersionCode(Context context) {
        String versionCode = "";
        try {
            // 获取软件版本号
            versionCode = context.getPackageManager().getPackageInfo(LTNConstants.PACKAGE_NAME, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    //    public  static void checkSession(final Context context) {
//        if(LTNApplication.getInstance()==null){
//            return;
//        }
//        if(!TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
//            RequestParams mReqParams = new RequestParams();
//            mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
//            mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
//            LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.MY_USER_INFO_URL, mReqParams,
//                    new JsonHttpResponseHandler() {
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
//                            try {
//                                String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
//                                if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
//                                    // 将用户信息存储到
//                                    Gson gson = new Gson();
//                                    JSONObject accountInfoObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
//                                    LTNApplication.getInstance().getCurrentUser().setUserInfo(gson.fromJson(accountInfoObj.toString(), User.UserInfo.class));
//                                } else {
//                                    Toast.makeText(context,"登录超时，请重新登录",Toast.LENGTH_SHORT).show();
//                                    String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
//                                   // Log.e("TAG", resultMsg);
//                                    if (resultCode.equals(LTNConstants.MAG_SESSION)) {
//                                        LTNApplication.getInstance().clearUser();
////                                        ActivityUtils.finishAll();
//                                        Intent intent = new Intent(context, LoginActivity.class);
//                                        Bundle b = new Bundle();
//                                        b.putString(Constant.LoginParams.LOGIN_TYPE,Constant.LoginParams.LOGIN_TO_MAIN);
//                                        intent.putExtras(b);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                        context.startActivity(intent);
//                                    }
//                                }
//                            } catch (JSONException je) {
//                               // Log.e("TAG", "出错了");
//                            }
//                        }
//                    });
//        }
//    }
    //判断网络是否可用
    public static boolean isNetworkConnected(Context context) {
        boolean isAvailable = false;
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                isAvailable = mNetworkInfo.isAvailable();
            }
        }
        if (!isAvailable) {
            Toast.makeText(context, R.string.net_error, Toast.LENGTH_SHORT).show();
        }
        return isAvailable;
    }

    //隐藏虚拟键盘
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    public static void showRealNameWarn(final Context context) {
        ViewUtils.showWarnDialog2(context, context.getString(R.string.realname_text), context.getString(R.string.user_auth_now), context.getString(R.string.user_auth_cancel),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserAuthActivity.class);
                        context.startActivity(intent);
                    }
                }, null);

    }

    public static void showBindBanWarn(final Context context) {
        ViewUtils.showWarnDialog2(context, context.getString(R.string.bindbank_text), context.getString(R.string.bind_bank_now), context.getString(R.string.bind_bank_cancel),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, BindBankCardActivity.class);
//                            intent.putExtra(Constant.USER_AUTH_FROM,Constant.FROM_BIND_BANK_CARD);
                        context.startActivity(intent);
                    }
                }, null);

    }

    public static String addOneDay() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    public static String addOneDay(int day) {
//        try {
//            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
            if (day != 0) {
                cal.add(Calendar.DATE, day);
            }
            return new SimpleDateFormat("yyyy-MM-dd E").format(cal.getTime());
//        } catch (ParseException pe) {
//            pe.getMessage();
//        }
//        return null;
    }

    public static boolean checkIsBindBank(Context context, User.UserInfo userInfo) {
        boolean flag = true;
        if (TextUtils.isEmpty(userInfo.getUserName())) {
            Utils.showRealNameWarn(context);
            flag = false;
        } else if (!(userInfo.getBankAuthStatus() != null && userInfo.getBankAuthStatus().equals("1"))) {
            Utils.showBindBanWarn(context);
            flag = false;
        }
        return flag;
    }

    public static void loginJump(Context context, Bundle b) {
        Intent intent = new Intent(context, LoginActivity.class);
//        Bundle b = getIntent().getExtras();
//        b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_PRODUCT);
        intent.putExtras(b);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
