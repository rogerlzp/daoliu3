package com.wash.daoliu.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LoginUtils;
import com.wash.daoliu.utility.StringUtils;
import com.wash.daoliu.utility.UserUtils;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.view.LoginClearableEditText;
import com.wash.daoliu.view.MyPwdEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rogerlzp on 15/11/23.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, MyPwdEditText.OnMyPwdEditTextChangedListener, LoginClearableEditText.OnEditTextChangedListener {

    private static final String TAG = "LoginActivity";
    private ImageView mPicCodeBtn;

    private LoginClearableEditText mUsernameEt;
    private MyPwdEditText mPasswordEt;
    private EditText mPicCodeEt;
    private TextView mTvForgerPassword;
    private TextView mTvRegister;
    private Button mBtnLogin;

    private String mUuid;
    // private Handler mUpdatePicHandler;
    // private UpdatePicThread mUpdatePicThread;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
    }


    /**
     * 初始化view
     */
    public void initView() {
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mPicCodeBtn = (ImageView) findViewById(R.id.picCode_iv);
        getPicCode();
        mUsernameEt = (LoginClearableEditText) findViewById(R.id.username_et);
        mUsernameEt.setOnEditTextChangedListener(this);
        //    mUsernameEt.setText("15298801871");
        mUsernameEt.setMaxLength(11);
        mPasswordEt = (MyPwdEditText) findViewById(R.id.password_et);
        mPasswordEt.setMaxLength(20);
        //  mPasswordEt.setText("123qqq");
        mPasswordEt.setPasswordFormat();

        mPasswordEt.setOnPWDEditTextChangedListener(this);

        mPicCodeEt = (EditText) findViewById(R.id.picCode_et);


        mBtnLogin.setOnClickListener(baseOnClickListener);

        findViewById(R.id.tv_forget_password).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);


        mPicCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnLogin.setEnabled(s.length() > 0 && mUsernameEt.getText().toString().length() > 0 && mPasswordEt.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onEditTextChanged(boolean flag) {
        mBtnLogin.setEnabled(flag && mPicCodeEt.getText().toString().length() > 0 && mPasswordEt.getText().toString().length() > 0);

    }

    @Override
    public void onPWDEditTextChanged(boolean flag) {
        mBtnLogin.setEnabled(flag && mPicCodeEt.getText().toString().length() > 0 && mUsernameEt.getText().toString().length() > 0);

    }


    /**
     * layout 中描述的 getPicCode
     *
     * @param v
     */
    public void getPicCode(View v) {
        getPicCode();
    }

    /**
     * 点击view, 向后台请求获取图片验证码
     */
    public void getPicCode() {
        mUuid = Utils.genUUid();
        HashMap<String, String> mReqMap = new HashMap();
        mReqMap.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqMap.put(LTNConstants.MACHINE_NUMBER, mUuid);

        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.PICTURE_CODE_HOST, WCOKHttpClient.TYPE_GET, mReqMap,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        String pictureUrl = null;
                        try {
                            JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                            pictureUrl = (String) resultObj.getString(LTNConstants.PICTURE_CODE);
                        } catch (JSONException je) {
                            Log.e(TAG, je.getMessage());
                        }
                        if (!StringUtils.isEmpty(pictureUrl)) {
                            WCOKHttpClient.getOkHttpClient(LoginActivity.this).requestAsyn(true, LTNConstants.ACCESS_URL.IMAGE_HOST + pictureUrl,
                                    WCOKHttpClient.TYPE_GET, new HashMap<String, String>(),
                                    new ReqCallBack<byte[]>() {

                                        @Override
                                        public void onReqSuccess(byte[] responseBody) {
                                            try{
                                                if (responseBody.length > 0) {
                                                    Bitmap sBitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                                                    Bitmap tBitmap = Bitmap.createScaledBitmap(sBitmap,
                                                            mPicCodeBtn.getWidth(), mPicCodeBtn.getHeight(), true);
                                                    mPicCodeBtn.setImageBitmap(tBitmap);
                                                } else {
                                                    Toast.makeText(LoginActivity.this,
                                                            getResources().getString(R.string.ERROR_PIC_CODE),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }catch (Exception e){
                                                Toast.makeText(LoginActivity.this,
                                                        getResources().getString(R.string.ERROR_PIC_CODE),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onReqFailed(String errorMsg) {
                                            Log.d(TAG, errorMsg);
                                        }
                                    });

                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.d(TAG, errorMsg);
                    }
                });

    }

    /**
     * use ImageLoader to download image
     *
     * @param imageUrl
     */
//    public void downloadImage(String imageUrl) {
//        ImageLoader.getInstance().loadImage(imageUrl, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
//                mPicCodeBtn.setImageBitmap(loadedImage);
//            }
//        });
//    }


    /**
     * login 方法,
     *
     * @param _clientType
     * @param _mobielNo
     * @param _passwordHash
     * @param _machineNumber
     * @param _picCode
     */
    private void login(String _clientType, final String _mobielNo, String _passwordHash, String _machineNumber,
                       String _picCode) {

        HashMap<String, String> mReqMap = new HashMap();
        mReqMap.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqMap.put(LTNConstants.MOBILE_NO, _mobielNo);
        mReqMap.put(LTNConstants.PASSWORD, _passwordHash);

        // 去穿
        mReqMap.put(LTNConstants.MACHINE_NUMBER, _machineNumber);
        mReqMap.put(LTNConstants.PICTURE_CODE, _picCode);


        showLoadingProgressDialog(LoginActivity.this, "正在登录中,请稍候");


        //TODO: enable it later
        //   LTNHttpClient.getLTNHttpClient().addHeader(LTNConstants.JPUSH_REG_ID, LTNApplication.getInstance().getJPushID());

        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.LOGIN_CODE_URL, WCOKHttpClient.TYPE_GET, mReqMap,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        dismissProgressDialog();
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                LTNApplication.getInstance().setSessionKey(resultObj.getString(LTNConstants.SESSION_KEY));
                                User.getUserInstance().setUserPhone(_mobielNo);
                                UserUtils.getUserInfo();
                                LTNApplication.getInstance().setUserMobile(_mobielNo);
                                LoginUtils.loginJump(LoginActivity.this, getIntent().getExtras());
                                finish();
                            } else {
                                // 重新刷新一下验证码
                                getPicCode();
                                if (mPicCodeEt != null) {
                                    if (mPicCodeEt.getText().length() > 0) {
                                        mPicCodeEt.setText("");
                                    }
                                }
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(LoginActivity.this,
                                        resultMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException je) {
                            Log.d(TAG, je.getMessage());
                        }
                    }


                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.d(TAG, errorMsg);
                    }
                });

    }


    /**
     * close app
     *
     * @param v
     */
    public void closeApp(View v) {
        this.finish();
    }


    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_login:
                // 登录
                String sMobile = mUsernameEt.getText().toString().trim();
                if (!Utils.isValidMobile(sMobile)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.get_correct_mobile),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String passwordHash = mPasswordEt.getText().toString().trim();
                if (!passwordHash.matches(LTNConstants.PASSWORD_PATTERN)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.password_info1),
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                String picCode = mPicCodeEt.getText().toString().trim();
                if (mPicCodeEt.length() < 4) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.get_correct_verifycode),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO: 使用唯一的机器码
                login(LTNConstants.CLIENT_TYPE_MOBILE, sMobile, passwordHash, mUuid, picCode);
                break;

            default:
                break;

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPasswordEt != null) {
            if (mPasswordEt.getText().length() > 0) {
                mPasswordEt.setText("");
            }
        }
        if (mPicCodeEt != null) {
            if (mPicCodeEt.getText().length() > 0) {
                mPicCodeEt.setText("");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_password:
                //  跳转到 忘记密码 页面
                Intent mIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_register:
                //跳转到 注册 页面
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                registerIntent.putExtra(LTNConstants.REGISTER_FORM_LOGIN, true);
                if (getIntent().getExtras() != null) { //修改如果没有,直接崩溃的bug
                    registerIntent.putExtras(getIntent().getExtras());
                }
                startActivityForResult(registerIntent, 1001);
                break;
        }
    }

}

