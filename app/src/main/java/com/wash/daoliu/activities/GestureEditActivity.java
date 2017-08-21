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
import com.wash.daoliu.view.LockIndicator;

/**
 * Created by rogerlzp on 15/12/28.
 */
public class GestureEditActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 手机号码
     */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    /**
     * 首次提示绘制手势密码，可以选择跳过
     */
    public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
    private TextView mTextTitle;
    private TextView mTextCancel;
    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
//    private TextView mTextReset;

    private boolean mIsFirstInput = true;
    private String mFirstPassword = "";
    private String mConfirmPassword = null;
    private int mParamIntentCode;

    private boolean isFromModify = false;

    private Bundle mBundle = null;
    private boolean isFROM_LOGIN = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit);
        setUpViews();

        // 根据参数判断是修改密码还是第一次重设密码
        if (getIntent().getStringExtra(LTNConstants.FROM_MODIFY_GESTURE_PASSWORD) != null) {
            isFromModify = getIntent().getStringExtra(LTNConstants.FROM_MODIFY_GESTURE_PASSWORD).equals(LTNConstants.GESTURE_PASSWORD) ? true : false;
            if (isFromModify) {
                ((TextView) findViewById(R.id.text_title)).setText(getResources().getString(R.string.modify_gesture));
                ((TextView) findViewById(R.id.text_tip)).setText(getResources().getString(R.string.modify_gesture_new));
//                ((TextView) findViewById(R.id.text_forget_gesture)).setVisibility(View.VISIBLE);
            }
        }
        // 如果没有手势密码, 进入手势密码设置界面.
        mBundle = getIntent().getExtras();
        isFROM_LOGIN = (getIntent().getStringExtra(Constant.LoginParams.LOGIN_TYPE) != null) ? true : false;

    }

    private void setUpViews() {

        mTextTitle = (TextView) findViewById(R.id.text_title);
        //  mTextReset = (TextView) findViewById(R.id.text_reset);
//        mTextReset.setClickable(false);
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>"));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                if (mIsFirstInput) {
                    mFirstPassword = inputCode;
                    updateCodeList(inputCode);
                    mGestureContentView.clearDrawlineState(0L);
//                    mTextReset.setClickable(true);
//                    mTextReset.setText(getString(R.string.reset_gesture_code));
                    mIsFirstInput = false;
                } else {
                    if (inputCode.equals(mFirstPassword)) {
                        // 存储手势密码 到本地
                        LTNApplication.getInstance().storeGesturePassword(mFirstPassword);
                        if (isFromModify) {
                            Toast.makeText(GestureEditActivity.this, "手势密码设置成功", Toast.LENGTH_SHORT).show();
                            mGestureContentView.clearDrawlineState(0L);
//                            Intent mIntent = new Intent(GestureEditActivity.this, AccountInfoActivity.class);
//                            startActivity(mIntent);
                            finish();
                        } else if (mBundle != null) {
//                            LoginUtils.loginJump(GestureEditActivity.this, getIntent().getExtras());
//                            getUserInfo();
                            finish();
                        } else {
                            Toast.makeText(GestureEditActivity.this, "手势密码设置成功", Toast.LENGTH_SHORT).show();
                            mGestureContentView.clearDrawlineState(0L);
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    } else {
                        mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>"));
                        mIsFirstInput = true;
                        mFirstPassword = "";
                        updateCodeList("");
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(1300L);
                    }
                }
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
        updateCodeList("");
    }

//    private void setUpListeners() {
//        mTextReset.setOnClickListener(this);
//    }

    private void updateCodeList(String inputCode) {
        // 更新选择的图案
        mLockIndicator.setPath(inputCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //   case R.id.text_reset:
            //       mIsFirstInput = true;
            //      updateCodeList("");
            //     mTextTip.setText(getString(R.string.set_gesture_pattern));
            //    break;
            default:
                break;
        }
    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
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

//    public void getUserInfo() {
//       // Log.e("TAG", LTNApplication.getInstance().getSessionKey());
//        RequestParams mReqParams = new RequestParams();
//        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
//        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
//        //TODO: 添加错误或者失败的跳转
//        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.MY_USER_INFO_URL, mReqParams,
//                new JsonHttpResponseHandler() {
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
//                        try {
//                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
//
//
//                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
    // 将用户信息存储到
//                                Gson gson = new Gson();
//                                JSONObject accountInfoObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
//                                LTNApplication.getInstance().getCurrentUser().setUserInfo(gson.fromJson(accountInfoObj.toString(), User.UserInfo.class));
//                                User.UserInfo userInfo = LTNApplication.getInstance().getCurrentUser().getUserInfo();
//                                if (userInfo == null || (userInfo != null && TextUtils.isEmpty(userInfo.getUserName()))) {
//                                    ViewUtils.showWarnDialog(GestureEditActivity.this, getString(R.string.realname_text), getString(R.string.next),
//                                            new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Intent intent = new Intent(GestureEditActivity.this, UserAuthActivity.class);
//                                                    startActivity(intent);
//                                                }
//                                            });
//                                }
//                            } else {
//                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
//                               // Log.e("TAG", resultMsg);
//                            }
//                        } catch (JSONException je) {
//                           // Log.e("TAG", "出错了");
//                        }
//                    }
//                });

//    }

}





