package com.wash.daoliu.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import com.umeng.analytics.MobclickAgent;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.utility.LTNConstants;

//import cn.jpush.android.api.JPushInterface;

/**
 * Created by rogerlzp on 15/12/18.
 */
public class LaunchActivity extends Activity {

    public final String TAG = LaunchActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(R.layout.loadingpage);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
//        JPushInterface.onResume(this);
        Thread waitThread = new Thread() {
            @Override
            public void run() {
                try {
//                    LTNApplication.getInstance()
//                    LTNApplication.getInstance() = (LTNApplication) LaunchActivity.this
//                            .getApplication();
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                ;

                runOnUiThread(new Runnable() {
                    // @Override
                    public void run() {
                        ///    if (LTNApplication.getInstance().isFirstLogin()) { // 第一次登录,跳转到欢迎页界面.
                        //        enterGuideActivity();
                        //   enterMainActivity(); // 已经登录,跳转到主页面
//                            enterLoginActivity();

                        // } else {
                        // 没有登录,跳转到登录界面
//                            enterLoginActivity();
                        //  if(TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())){
                        enterMainActivity(); // 已经登录,跳转到主页面
//                            }else{
//                                enterGesture();//进入手势密码
//                            }

//                        }
                    }
                });
            }
        };
        waitThread.start();

    }


    private void enterMainActivity() {

        Intent mainAct = new Intent();
        mainAct.setClass(this, MainActivity.class);
        mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainAct);
        finish();

    }

    private void enterGesture() {
        Intent mainAct = new Intent();
        mainAct.setClass(this, GestureVerifyActivity.class);
        mainAct.putExtra(LTNConstants.FROM_MODIFY_GESTURE_PASSWORD, LTNConstants.FROM_LAUNCH);
        mainAct.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        mainAct.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mainAct);
        finish();
    }

//    private void enterLoginActivity() {
//        Intent loginIntent = new Intent();
//        loginIntent.setClass(this, LoginActivity.class);
//        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(loginIntent);
//        finish();
//    }

    private void enterGuideActivity() {
        Intent gudieIntent = new Intent();
        gudieIntent.setClass(this, GuideActivity.class);
        startActivity(gudieIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }
}
