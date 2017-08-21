package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rogerlzp on 15/12/7.
 */
public class ForgetPasswordActivity2 extends BaseActivity implements View.OnClickListener {

    private String TAG = "ForgetPasswordActivity2";

    private EditText mPasswordEt, mPasswordEt2;
    private String mMobileNo;   // 手机号码
    private String mIdCardNo;   // 身份证号码
    private String mCaptcha;    // 短信验证码

    private Button confirm_modify_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.forgetpassword_step2);

        initView();

        // 从上一步Activity 中传过来的值
        Intent mIntent = getIntent();
        mMobileNo = mIntent.getStringExtra(LTNConstants.MOBILE_NO);
        mIdCardNo = mIntent.getStringExtra(LTNConstants.ID_CARD);
        mCaptcha = mIntent.getStringExtra(LTNConstants.MOBILE_CODE);


    }

    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("找回密码");
        confirm_modify_btn = (Button) findViewById(R.id.confirm_modify_btn);
        confirm_modify_btn.setOnClickListener(this);
        mPasswordEt = (EditText) findViewById(R.id.password_et1);
        mPasswordEt2 = (EditText) findViewById(R.id.password_et2);
        findViewById(R.id.back_btn).setOnClickListener(this);

        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirm_modify_btn.setEnabled(s.length() > 0 && mPasswordEt2.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirm_modify_btn.setEnabled(s.length() > 0 && mPasswordEt.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    // 修改密码
    public void modifyPassword() {
        String passwd1 = mPasswordEt.getText().toString().trim();
        if (!passwd1.matches(LTNConstants.PASSWORD_PATTERN)) {
            Toast.makeText(ForgetPasswordActivity2.this, getResources().getString(R.string.password_info1),
                    Toast.LENGTH_SHORT).show();
            // Log.d(TAG, "passwd1");
            return;
        }

        String passwd2 = mPasswordEt2.getText().toString().trim();
        if (!passwd2.matches(LTNConstants.PASSWORD_PATTERN)) {
            Toast.makeText(ForgetPasswordActivity2.this, getResources().getString(R.string.password_info1),
                    Toast.LENGTH_SHORT).show();
            // Log.d(TAG, "passwd2");
            return;
        }

        if (!passwd1.equals(passwd2)) {
            Toast.makeText(ForgetPasswordActivity2.this, getResources().getString(R.string.password_not_match),
                    Toast.LENGTH_SHORT).show();
            return;
        }


        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.MOBILE_NO, mMobileNo);
        mReqParams.put(LTNConstants.MOBILE_CODE, mCaptcha);
        mReqParams.put(LTNConstants.NEW_PASSWORD, passwd1);
        mReqParams.put(LTNConstants.ID_CARD, mIdCardNo);

        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.RETRIEVE_PASSWORD_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                // 跳转到登录页面
                                LTNApplication.getInstance().clearUser();
                                Intent mIntent = new Intent(ForgetPasswordActivity2.this, MainActivity.class);
                                mIntent.putExtra(LTNConstants.FROM_FORGET_PASSWORD, true);
                                startActivity(mIntent);
                                setResult(RESULT_OK);
                                finish();// 关闭忘记密码

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(ForgetPasswordActivity2.this, resultMsg,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException je) {
                            Log.e(TAG, je.getMessage());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e(TAG, errorMsg);
                    }
                });


    }

    /**
     * 回退到前一个页面
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.confirm_modify_btn:
                modifyPassword();
                break;
        }
    }


}
