package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LoginUtils;
import com.wash.daoliu.utility.PasswordUtil;
import com.wash.daoliu.utility.Utils;

import com.wash.daoliu.view.ClearableEditText;
import com.wash.daoliu.view.ClearablePwdEditText;
import com.wash.daoliu.view.TrustView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;


/**
 * Created by rogerlzp on 15/11/23.
 */
public class RegisterActivity extends BaseActivity implements TrustView.OnTrustCheckChanged, ClearableEditText.OnEditTextChangedListener, ClearablePwdEditText.OnPWDEditTextChangedListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    ClearableEditText mUsernameEt;
    ClearablePwdEditText mPasswordEt;
    EditText mCaptchaEt;
    Button showSwitchBtn;
    private boolean hasRecommend = false;

    TextView mCaptchaTv;
    private TextView mCaptchaBtn;
    private Button mRegiserBtn;
    private TextView mRecommendTv;


    TrustView mAgreementCheckBox;

    RelativeLayout mRecommendRl;

    String mPhoneNumber;
    String mVerifyCode = null;
    //Boolean flag = false;
    Button iconClearBtn;
    String recommendMobile = "";
    String sMobile = null;
    private EditText recommandPhone = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
    }

    public void initView() {
        mRegiserBtn = (Button) findViewById(R.id.btn_register);
        recommandPhone = (EditText) findViewById(R.id.et_recommend);
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (getIntent().getBooleanExtra(LTNConstants.REGISTER_FORM_LOGIN, false)) {
//                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                    Bundle b = getIntent().getExtras();
//                    if (b != null) {
//                        intent.putExtras(b);
//                    }
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    startActivity(intent);
//                }
                finish();
            }
        });
        ((TextView) findViewById(R.id.title)).setText(R.string.register);
        mUsernameEt = (ClearableEditText) findViewById(R.id.mobile_number_et1);
        mUsernameEt.setOnEditTextChangedListener(this);
        mPasswordEt = (ClearablePwdEditText) findViewById(R.id.password_et);
        mPasswordEt.setOnPWDEditTextChangedListener(this);
        mPasswordEt.setPasswordFormat();
        mCaptchaEt = (EditText) findViewById(R.id.verify_code_et);

        mCaptchaEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegiserBtn.setEnabled(s.length() > 0 && mUsernameEt.getText().toString().length() > 0 && mCaptchaEt.getText().toString().length() > 0 && mAgreementCheckBox.isAgreeSelected());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordEt.setHint(this.getResources().getString(R.string.new_password_hint));

        mRecommendRl = (RelativeLayout) findViewById(R.id.rl5);
        //   mCaptchaTv = (TextView) findViewById(R.id.verify_code_et);
        mAgreementCheckBox = (TrustView) findViewById(R.id.agreement_checkbox);
        mAgreementCheckBox.setOnTrustCheckChanged(this);

        mAgreementCheckBox.setData("我已同意", this.getResources().getString(R.string.niaoren_agreement), LTNConstants.ACCESS_URL.H5_REGISTER_URL);

//        mAgreementCheckBox.initAgreement(this.getResources().getString(R.string.niaoren_agreement),
//                LTNConstants.ACCESS_URL.H5_REGISTER_URL);

        mCaptchaBtn = (TextView) findViewById(R.id.btn_captcha);

        View view = findViewById(R.id.title_layout);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        mRecommendTv = (TextView) findViewById(R.id.tv_recommend);
        mCaptchaBtn.setOnClickListener(baseOnClickListener);
        mRegiserBtn.setOnClickListener(baseOnClickListener);
        mRecommendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecommend();

                recommandPhone.requestFocus();
            }
        });
    }


    /**
     * 点击view, 向后台请求获取验证码
     */
    public void getVerifycode() {
        mCaptchaBtn.setEnabled(false);
        sMobile = mUsernameEt.getText().toString().trim();
        if (!Utils.isValidMobile(sMobile)) {
            // Log.d(TAG, "getVerifycode");
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.get_correct_mobile),
                    Toast.LENGTH_SHORT).show();
            mCaptchaBtn.setEnabled(true);
            return;
        }

        TreeMap<String, String> map = new TreeMap<String, String>();
        map.put(LTNConstants.CLIENT_TYPE_PARAM, "=" + LTNConstants.CLIENT_TYPE_MOBILE);
        map.put(LTNConstants.MOBILE_NO, "=" + sMobile);
        map.put(LTNConstants.SEND_TYPE, "=" + LTNConstants.SENT_TYPE_REGISTER);
        map.put(PasswordUtil.private_key, "=" + PasswordUtil.private_value);


        TimeCounter timeCounter = new TimeCounter(60000, 1000);
        timeCounter.start();

        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.MOBILE_NO, sMobile);
        mReqParams.put(LTNConstants.SEND_TYPE, LTNConstants.SENT_TYPE_REGISTER);

        //TODO: enable it later
        //  LTNHttpClient.getLTNHttpClient().addHeader(LTNConstants.HEADER_SIGN, PasswordUtil.getMD5(map));


        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.VERIFY_CODE_URL, WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (!resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                String resultMessage = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(RegisterActivity.this, resultMessage,
                                        Toast.LENGTH_SHORT).show();
                            } else {

                            }

                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e(TAG, errorMsg);
                    }
                });
    }

    /**
     * 注册请求
     *
     * @param _mobile
     * @param _passwordHash
     * @param _clientType
     * @param _verifyCode
     * @param readAndAgree
     */
    public void register(final String _mobile, String _passwordHash, String _clientType, String _verifyCode,
                         String readAndAgree, String parterMobile) {

        HashMap<String, String> mReqMap = new HashMap();
        mReqMap.put(LTNConstants.MOBILE_NO, _mobile);
        mReqMap.put(LTNConstants.PASSWORD, _passwordHash);
        mReqMap.put(LTNConstants.CLIENT_TYPE_PARAM, _clientType);
        mReqMap.put(LTNConstants.MOBILE_CODE, _verifyCode);
        mReqMap.put(LTNConstants.READ_AND_AGREE, readAndAgree);
        if (!parterMobile.equals("")) {
            mReqMap.put(LTNConstants.PARTER_MOBILE, parterMobile);
        }


        showLoadingProgressDialog(RegisterActivity.this, "正在注册中,请稍候");

        // disable it later
        //  LTNHttpClient.getLTNHttpClient().addHeader(LTNConstants.JPUSH_REG_ID, LTNApplication.getInstance().getJPushID());


        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.REGISTER_URL, WCOKHttpClient.TYPE_GET, mReqMap,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        dismissProgressDialog();
                        String resultCode = null;
                        String sessionKey = null;
                        try {
                            resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {


                                sessionKey = ((JSONObject) jsonObject.get(LTNConstants.DATA)).getString(LTNConstants.SESSION_KEY);
                                LTNApplication.getInstance().setSessionKey(sessionKey);
                                LTNApplication.getInstance().getCurrentUser().setUserPhone(sMobile);
                                LTNApplication.getInstance().setUserMobile(sMobile);
                                LTNApplication.getInstance().clearGesturePassword();

                                String resultMessage = jsonObject.optString(LTNConstants.RESULT_MESSAGE);
                                if (!TextUtils.isEmpty(resultMessage)) {
                                    Toast.makeText(RegisterActivity.this, resultMessage,
                                            Toast.LENGTH_SHORT).show();
                                }

                                Bundle b = getIntent().getExtras();
                                b.putBoolean(LTNConstants.IS_REGISTER, true);
                                // 判断其他路径
                                LoginUtils.loginJump(RegisterActivity.this, b);
                                setResult(RESULT_OK);
                                finish();


                            } else {
                                String resultMessage = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(RegisterActivity.this, resultMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                            dismissProgressDialog();
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.d(TAG, errorMsg);
                        dismissProgressDialog();
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 点击进入注册界面
     * 验证mobile 是否符合规范, 具体见Utils.isValidMobile
     * 验证 password 长度是否正确
     * 验证码必须为4位数字
     */
    public void register() {
        String sMobile = mUsernameEt.getText().toString().trim();
        if (!Utils.isValidMobile(sMobile)) {
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.get_correct_mobile),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordHash = mPasswordEt.getText().toString().trim();
        if (!passwordHash.matches(LTNConstants.PASSWORD_PATTERN)) {
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.password_info1),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //  passwordHash = PasswordService.getInstance().encrypt(passwordHash);


        String verifyCode = mCaptchaEt.getText().toString().trim();
        if (verifyCode.length() < 4) {
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.get_correct_verifycode),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mAgreementCheckBox.isAgreeSelected()) {
            Toast.makeText(RegisterActivity.this, "请同意在线服务协议再注册",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 如果有推荐人
        if (hasRecommend) {
            recommendMobile = recommandPhone.getText().toString().trim();
            if (!Utils.isValidMobile(recommendMobile)) {
                Toast.makeText(RegisterActivity.this, "请填写正确的推荐人的号码",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        register(sMobile, passwordHash, LTNConstants.CLIENT_TYPE_MOBILE,
                verifyCode, LTNConstants.READ_AND_AGREE_TRUE, recommendMobile);

    }


    @Override
    public void onResume() {
        super.onResume();
        // Log.d(TAG, "onResume");
    }

    /**
     * 点击View, 展开或者收缩显得推荐人
     */
    public void showRecommend() {
        if (!hasRecommend) {
            mRecommendRl.setVisibility(View.VISIBLE);
            hasRecommend = true;
            recommandPhone.setFocusable(true);
            recommandPhone.setFocusableInTouchMode(true);
            recommandPhone.requestFocus();
        } else {
            mRecommendRl.setVisibility(View.GONE);
            hasRecommend = false;
        }
    }

    public class TimeCounter extends CountDownTimer {
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
//            mCaptchaBtn.setEnabled(false);//防止重复点击
            mCaptchaBtn.setTextColor(getResources().getColor(R.color.label_grey1));
            mCaptchaBtn.setText(millisUntilFinished / 1000 + getResources().getString(R.string.resendable_after_seconds));
        }

    }

    /**
     * 回退到前一个页面
     *
     * @param v
     */
    public void backToParent(View v) {
        super.onBackPressed();
    }


    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_register:
                // 注册页面
                register();
                break;
            case R.id.btn_captcha:
                //  跳转到 忘记密码 页面
                getVerifycode();
                break;
            default:
                break;
        }
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (getIntent().getBooleanExtra(LTNConstants.REGISTER_FORM_LOGIN, false)) {
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                Bundle b = getIntent().getExtras();
//                if (b != null) {
//                    intent.putExtras(b);
//                }
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent);
//            }
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onChanged(boolean selected) {
        mRegiserBtn.setEnabled(selected && mUsernameEt.getText().toString().length() > 0 && mPasswordEt.getText().toString().length() > 0 && mCaptchaEt.getText().toString().length() > 0);
    }


    @Override
    public void onPWDEditTextChanged(boolean selected) {
        mRegiserBtn.setEnabled(selected && mUsernameEt.getText().toString().length() > 0 && mCaptchaEt.getText().toString().length() > 0 && mAgreementCheckBox.isAgreeSelected());
    }

    @Override
    public void onEditTextChanged(boolean selected) {
        mRegiserBtn.setEnabled(selected && mPasswordEt.getText().toString().length() > 0 && mCaptchaEt.getText().toString().length() > 0 && mAgreementCheckBox.isAgreeSelected());
    }

}
