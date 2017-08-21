package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.eventtype.ShopServiceReserveEvent;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;
import com.wash.daoliu.utility.PasswordUtil;
import com.wash.daoliu.utility.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/11/26.
 */
public class ForgetPasswordActivity extends BaseActivity {

    private static final String TAG = ForgetPasswordActivity.class.getSimpleName();

    private EditText mUsernameEt;
    private EditText mIDcardEt;
    private EditText mPasswordEt;
    private EditText mCaptchaEt;

    private RelativeLayout mIDCardRl;

    private ImageButton mClearBtn;
    private ImageButton mIDClearBtn;

    boolean isCertificated = false;
    String mMobileNo;
    String mMobileCode;
    String idCard = "";
    private TextView mCaptchaBtn;
    private Button mConfirmBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forgetpassword_step1);
        initView();
    }

    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("找回密码");
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUsernameEt = (EditText) findViewById(R.id.mobile_number_et1);
        mCaptchaEt = (EditText) findViewById(R.id.captcha_et);
        mIDcardEt = (EditText) findViewById(R.id.idcard_et);
        mIDCardRl = (RelativeLayout) findViewById(R.id.rl5);
        mClearBtn = (ImageButton) findViewById(R.id.button_clear);
        mIDClearBtn = (ImageButton) findViewById(R.id.idcard_button_clear);


        //   mUsernameEt.addTextChangedListener(mTextWatcher);
        mUsernameEt.setOnFocusChangeListener(new OnFocusChangeListenerImpl());

        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsernameEt.setText("");
            }
        });
        mIDcardEt.addTextChangedListener(mTextWatcher);
        mIDClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIDcardEt.setText("");
            }
        });

        mCaptchaBtn = (TextView) findViewById(R.id.btn_captcha);
        mConfirmBtn = (Button) findViewById(R.id.btn_confirm);
        mCaptchaBtn.setOnClickListener(baseOnClickListener);
        mConfirmBtn.setOnClickListener(baseOnClickListener);

        mUsernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mConfirmBtn.setEnabled(s.length() > 0 && mCaptchaEt.getText().toString().length() > 0);
                if (s.length() > 0) {
                    mClearBtn.setVisibility(View.VISIBLE);
                } else {
                    mClearBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCaptchaEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mConfirmBtn.setEnabled(s.length() > 0 && mUsernameEt.getText().toString().length() > 0);
//                if(s.length()>0) {
//                    mIDClearBtn.setVisibility(View.VISIBLE);}
//                else {
//                    mIDClearBtn.setVisibility(View.INVISIBLE);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    /**
     * 点击view, 向后台请求获取验证码
     */
    public void getCaptcha() {
        mMobileNo = mUsernameEt.getText().toString().trim();
        if (!Utils.isValidMobile(mMobileNo)) {
            Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.get_correct_mobile),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mCaptchaBtn.setEnabled(false);


        HashMap<String, String> mReqParams = new HashMap();

////        TreeMap<String, String> map = new TreeMap<String, String>();
//        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
//        mReqParams.put(LTNConstants.MOBILE_NO, mMobileNo);
//        mReqParams.put(LTNConstants.SEND_TYPE,  LTNConstants.SENT_TYPE_FORGET_PASSWORD);
//      //  map.put(PasswordUtil.private_key, "=" + PasswordUtil.private_value);


        TimeCounter timeCounter = new TimeCounter(60000, 1000);
        timeCounter.start();
//        HashMap<String, String> mReqParams = new HashMap();
        //     RequestParams mReqParams = new RequestParams();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.MOBILE_NO, mMobileNo);
        mReqParams.put(LTNConstants.SEND_TYPE, LTNConstants.SENT_TYPE_FORGET_PASSWORD);


        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.VERIFY_CODE_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
//                                JSONObject dataObject = (JSONObject) jsonObject.get(LTNConstants.DATA);
//                                isCertificated = dataObject.getString(LTNConstants.CERTIFICATION).equals(LTNConstants.CERTIFICATION_TRUE) ? true : false;
//                                if (isCertificated) {
//                                    mIDCardRl.setVisibility(View.VISIBLE);
//                                } else {
//                                    mIDCardRl.setVisibility(View.INVISIBLE);
//                                }

                            } else {
                                String resultMessage = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(ForgetPasswordActivity.this, resultMessage,
                                        Toast.LENGTH_SHORT).show();
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
            mCaptchaBtn.setTextColor(getResources().getColor(R.color.label_grey1));
            mCaptchaBtn.setText(millisUntilFinished / 1000 + getResources().getString(R.string.resendable_after_seconds));
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (mUsernameEt.getText().toString() != null && !mUsernameEt.getText().toString().equals("")) {
//                mClearBtn.setVisibility(View.VISIBLE);
//            } else {
//                mClearBtn.setVisibility(View.INVISIBLE);
//            }
            if (mIDcardEt.getText().toString() != null && !mIDcardEt.getText().toString().equals("")) {
                mIDClearBtn.setVisibility(View.VISIBLE);
            } else {
                mIDClearBtn.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            finish();
        }
    }

    // go to next view
    public void confirm() {
        mMobileNo = mUsernameEt.getText().toString().trim();
        if (!Utils.isValidMobile(mMobileNo)) {
            Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.get_correct_mobile),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mMobileCode = mCaptchaEt.getText().toString().trim();
        if (mMobileCode.length() < 4) {
            Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.get_correct_verifycode),
                    Toast.LENGTH_SHORT).show();
            return;
        }


        HashMap<String, String> mReqParams = new HashMap();

        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.MOBILE_NO, mMobileNo);
        mReqParams.put(LTNConstants.MOBILE_CODE, mMobileCode);
        mReqParams.put(LTNConstants.SEND_TYPE, LTNConstants.SENT_TYPE_FORGET_PASSWORD);

        if (isCertificated) {
            idCard = mIDcardEt.getText().toString().trim();
            if (!idCard.matches(LTNConstants.IDCARD_PATTERN)) {
                Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.get_correct_idcard),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mReqParams.put(LTNConstants.ID_CARD, idCard);
        }


        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.VERIFY_MOBILE_CODE_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString(LTNConstants.RESULT_CODE).equals(LTNConstants.MSG_SUCCESS)) {
                                // 跳转到登录页面
                                Intent newIntent = new Intent(ForgetPasswordActivity.this, ForgetPasswordActivity2.class);
                                newIntent.putExtra(LTNConstants.MOBILE_NO, mMobileNo);
                                newIntent.putExtra(LTNConstants.MOBILE_CODE, mMobileCode);
                                newIntent.putExtra(LTNConstants.ID_CARD, idCard);
                                startActivityForResult(newIntent, 1001);
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(ForgetPasswordActivity.this, resultMsg,
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


    private class OnFocusChangeListenerImpl implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() == R.id.mobile_number_et1) {
                if (!hasFocus) {
                    mClearBtn.setVisibility(RelativeLayout.GONE);
                }
                if (((EditText) v).getText().length() > 0 && hasFocus) {
                    mClearBtn.setVisibility(RelativeLayout.VISIBLE);
                }
            }
        }
    }


    /**
     * 重写BaseActivity 中的 clickEffective 方法
     *
     * @param v
     */
    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_captcha:
                //  获取验证码;
                getCaptcha();
                break;
            case R.id.btn_confirm:
                // 确认
                confirm();
                break;
            default:
                break;

        }

    }

}
