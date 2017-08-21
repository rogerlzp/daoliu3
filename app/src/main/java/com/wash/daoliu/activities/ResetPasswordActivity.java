package com.wash.daoliu.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.view.MyEditText2;
import com.wash.daoliu.view.MyPwdEditText;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/11/26.
 */
public class ResetPasswordActivity extends BaseActivity {

    private static final String TAG = ResetPasswordActivity.class.getSimpleName();
    MyEditText2 mUsernameEt;
    MyPwdEditText mPasswordEt;
    MyPwdEditText mPassword2Et;
    EditText mVerifyCodeEt;
    Button mCaptchaBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);

        initView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.modify_tv:
//                if(TextUtils.isEmpty(mVerifyCodeEt.getText().toString())){
//                    Toast.makeText(ResetPasswordActivity.this, R.string.verifycode_hint,
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String passwordHash = mPasswordEt.getText().toString().trim();
//
//                if (!passwordHash.matches(LTNConstants.PASSWORD_PATTERN)) {
//                    Toast.makeText(this, getResources().getString(R.string.get_correct_password),
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String passwordHash1= mPassword2Et.getText().toString().trim();
//                if (!passwordHash1.matches(LTNConstants.PASSWORD_PATTERN)) {
//                    Toast.makeText(this, getResources().getString(R.string.get_correct_password),
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(!passwordHash.equals(passwordHash1)){
//                    Toast.makeText(this, getResources().getString(R.string.password_not_match),
//                            Toast.LENGTH_SHORT).show();
//                }
//                modifyPwd(mUsernameEt.getText().toString(),mPasswordEt.getText().toString(),
//                        mPassword2Et.getText().toString(),LTNConstants.CLIENT_TYPE_MOBILE,mVerifyCodeEt.getText().toString(),
//                        LTNApplication.getInstance().getSessionKey());
                modifyPwd();
                break;
            case R.id.btn_captcha:
                mCaptchaBtn.setTextColor(getResources().getColor(R.color.text_color3));
                mCaptchaBtn.setEnabled(false);
                getVerifycode();
                break;
        }
    }

    public void initView() {
        mUsernameEt = (MyEditText2) findViewById(R.id.et_mobile_number);
        mUsernameEt.setEnabled(false);
        mUsernameEt.setText(User.getUserInstance().getUserPhone());
        mPasswordEt = (MyPwdEditText) findViewById(R.id.password1_et);
        mPassword2Et = (MyPwdEditText) findViewById(R.id.password2_et);
        mVerifyCodeEt = (EditText) findViewById(R.id.captcha_et);

        mCaptchaBtn = (Button) findViewById(R.id.btn_captcha);
        mCaptchaBtn.setOnClickListener(this);

        mUsernameEt.setMaxLength(11);
        mPasswordEt.setMaxLength(20);
        mPassword2Et.setMaxLength(20);
        mPasswordEt.setPasswordFormat();
        mPassword2Et.setPasswordFormat();
        this.findViewById(R.id.back_btn).setOnClickListener(this);
        this.findViewById(R.id.modify_tv).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("修改密码");
    }


    /**
     * 点击view, 向后台请求获取验证码
     *
     * @param
     */
    public void getVerifycode() {
        String sMobile = mUsernameEt.getText().toString().trim();
        if (!Utils.isValidMobile(sMobile)) {
            Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.get_correct_mobile),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.MOBILE_NO, sMobile);

        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.VERIFY_CODE_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject jsonObject) {
                        //TODO: 动画更新UI界面
                        TimeCounter timeCounter = new TimeCounter(60000, 1000);
                        timeCounter.start();
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject dataObject = (JSONObject) jsonObject.get(LTNConstants.DATA);
                            } else {
                                String resultMessage = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(ResetPasswordActivity.this, resultMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException je) {
                           // Log.d(TAG, je.getMessage());
                            Toast.makeText(ResetPasswordActivity.this, "获取二维码失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                    //TODO: 增加对网络错误的处理,等待产品统一确定
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                       // Log.d(TAG, "onFailure in UserUtils");
                        Toast.makeText(ResetPasswordActivity.this, "获取二维码失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class TimeCounter extends CountDownTimer {
        public TimeCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mCaptchaBtn.setText(getResources().getString(R.string.reget_verify_code));
            mCaptchaBtn.setTextColor(getResources().getColor(R.color.recommend));
            mCaptchaBtn.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mCaptchaBtn.setEnabled(false);//防止重复点击
            mCaptchaBtn.setTextColor(getResources().getColor(R.color.label_grey1));
            mCaptchaBtn.setText(millisUntilFinished / 1000 + getResources().getString(R.string.resendable_after_seconds));
        }
    }

    public void modifyPwd() {

        String mMobile = mUsernameEt.getText().toString().trim();
        if (!Utils.isValidMobile(mMobile)) {
            Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.get_correct_mobile),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String oldPasswordHash = mPasswordEt.getText().toString().trim();
        String newPasswordHash = mPassword2Et.getText().toString().trim();

        if (!oldPasswordHash.matches(LTNConstants.PASSWORD_PATTERN)) {
            Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.get_correct_password),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPasswordHash.matches(LTNConstants.PASSWORD_PATTERN)) {
            Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.get_correct_password),
                    Toast.LENGTH_SHORT).show();
            return;
        }

//        if (!newPasswordHash.equals(oldPasswordHash)) {
//            Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.password_not_match),
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }


        String verifyCode = mVerifyCodeEt.getText().toString().trim();
        if (verifyCode.length() < 4) {
            Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.get_correct_verifycode),
                    Toast.LENGTH_SHORT).show();
            return;
        }


        String clientType = LTNConstants.CLIENT_TYPE_MOBILE;

        SharedPreferences sp = getSharedPreferences(
                LTNConstants.LTN_CONFIG, MODE_PRIVATE);
        String sessionKey = sp.getString(LTNConstants.SESSION_KEY, null);
        modifyPwd(mMobile, oldPasswordHash, newPasswordHash, clientType, verifyCode, sessionKey);

    }

    public void modifyPwd(String _mobile, String _oldPasswordHash, String _newPasswordHash,
                          String _clientType, String _verifyCode,
                          String _sessionKey) {

        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.MOBILE_NO, _mobile);
        mReqParams.add(LTNConstants.OLD_PASSWORD, _oldPasswordHash);
        mReqParams.add(LTNConstants.NEW_PASSWORD, _newPasswordHash);
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, _clientType);
        mReqParams.add(LTNConstants.MOBILE_CODE, _verifyCode);
        mReqParams.add(LTNConstants.SESSION_KEY, _sessionKey);


        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.RESET_PASSWORD_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //重新登录
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
//                                Intent nIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
//                                startActivity(nIntent);
                                finish();
                            }else{

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                       // Log.d(TAG, "onFailure in UserUtils");
                        Toast.makeText(ResetPasswordActivity.this, "失败了",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
