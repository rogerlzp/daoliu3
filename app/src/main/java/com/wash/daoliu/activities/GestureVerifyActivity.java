package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.GestureContentView;
import com.wash.daoliu.view.GestureDrawline;

/**
 * 手势绘制/校验界面
 */
public class GestureVerifyActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    private TextView mTextCancel;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextForget;
    private TextView mTextOther;
    private TextView mTextTitle;
    private long mExitTime = 0;
    private boolean isFromModify = false;
    private boolean isFromLaunch = false;
    Bundle mBundle;
    private int wrongCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_verify);
        setUpViews();
        setUpListeners();

        // 根据参数来确定是解锁还是修改密码
        String from = getIntent().getStringExtra(LTNConstants.FROM_MODIFY_GESTURE_PASSWORD);
        if (!TextUtils.isEmpty(from)) {
            isFromModify = from.equals(LTNConstants.GESTURE_PASSWORD) ? true : false;
            if (isFromModify) {
                mTextTitle.setText(getResources().getString(R.string.modify_gesture));
                ((TextView) findViewById(R.id.text_tip)).setText(getResources().getString(R.string.modify_gesture_original));
            }
            isFromLaunch = from.equals(LTNConstants.FROM_LAUNCH) ? true : false;
        }
        mBundle = getIntent().getExtras();
    }


    private void setUpViews() {
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
        mTextTip.setText(R.string.unlock_gesture_welcome);

        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, true, LTNApplication.getInstance().getGesturePassword(),
                new GestureDrawline.GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {
                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
                        if (LTNApplication.getInstance().isLock()) {
                            long nowTime = System.currentTimeMillis();
                            long lockTime = LTNApplication.getInstance().getLockTime();
                            long time = nowTime - lockTime;
                            if (time < 30 * 60 * 1000) {
                                long minute = time / 60000;
                                long lastMin = 30 - minute;
                                if (lastMin < 1) {
                                    lastMin = 1;
                                }
                                Toast.makeText(GestureVerifyActivity.this, "您已经被锁屏，请" + lastMin + "分钟后再试", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            LTNApplication.getInstance().unLock();
                        }
                        Toast.makeText(GestureVerifyActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
                        if (isFromModify) {
                            Intent mIntent = new Intent(GestureVerifyActivity.this, GestureEditActivity.class);
                            mIntent.putExtra(LTNConstants.FROM_MODIFY_GESTURE_PASSWORD, LTNConstants.GESTURE_PASSWORD);
                            startActivity(mIntent);
                            finish();
                        } else if (isFromLaunch) {
                            Intent mIntent = new Intent(GestureVerifyActivity.this, MainActivity.class);
                            startActivity(mIntent);
                            finish();
                        } else {
                            GestureVerifyActivity.this.finish();
                        }
                    }

                    @Override
                    public void checkedFail() {
                        mGestureContentView.clearDrawlineState(20L);
                        if (LTNApplication.getInstance().isLock()) {
                            long nowTime = System.currentTimeMillis();
                            long lockTime = LTNApplication.getInstance().getLockTime();
                            long time = nowTime - lockTime;
                            if (time < 30 * 60 * 1000) {
                                long minute = time / 60000;
                                long lastMin = 30 - minute;
                                if (lastMin < 1) {
                                    lastMin = 1;
                                }
                                Toast.makeText(GestureVerifyActivity.this, "您已经被锁屏，请" + lastMin + "分钟后再试", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            LTNApplication.getInstance().unLock();
                        }
                        wrongCount++;
                        if (wrongCount >= 5) {
                            LTNApplication.getInstance().setLock();
                            Toast.makeText(GestureVerifyActivity.this, "错误超过5次，请30分钟后再试", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GestureVerifyActivity.this, "手势密码错误，您还有" + (5 - wrongCount) + "次机会", Toast.LENGTH_SHORT).show();
                        }
                        mTextTip.setVisibility(View.VISIBLE);
                        mTextTip.setText(Html
                                .fromHtml("<font color='#c70c1e'>密码错误</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                    }
                });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }

    private void setUpListeners() {
        mTextForget.setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_login)).setOnClickListener(this);
    }

    private String getProtectedMobile(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.subSequence(7, 11));
        return builder.toString();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_forget_gesture:
                // 忘记手势密码
                // 登陆后重新设置密码
                Intent intent = new Intent(GestureVerifyActivity.this, LoginActivity.class);
                if (mBundle == null) {
                    mBundle = new Bundle();
                    mBundle.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_MAIN);
                }
                mBundle.putBoolean(Constant.LoginParams.FROM_FORGET_GESTURE, true);
                intent.putExtras(mBundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            case R.id.tv_login:
                // 忘记手势密码
                // 登陆后重新设置密码
                Intent intent1 = new Intent(GestureVerifyActivity.this, LoginActivity.class);
                if (mBundle == null) {
                    mBundle = new Bundle();
                    mBundle.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_MAIN);
                }
                mBundle.putBoolean(Constant.LoginParams.FROM_FORGET_GESTURE, true);
                intent1.putExtras(mBundle);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                break;


            default:
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isFromModify) {
                return super.onKeyDown(keyCode, event);
            } else {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
