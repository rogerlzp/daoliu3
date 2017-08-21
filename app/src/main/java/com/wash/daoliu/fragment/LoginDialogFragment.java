package com.wash.daoliu.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.ForgetPasswordActivity;
import com.wash.daoliu.activities.OnClickEffectiveListener;
import com.wash.daoliu.activities.RegisterActivity;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.view.LoginClearableEditText;
import com.wash.daoliu.view.MyPwdEditText;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/11/23.
 */
public class LoginDialogFragment extends DialogFragment {

    private static final String TAG = "LoginActivity";
    private ImageView mPicCodeBtn;
    private LoginListener loginListener = null;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof  LoginListener){
             loginListener = (LoginListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//        setContentView(R.layout.login);
//        initView();
    }
    public void setLoginListener(LoginListener loginListener){
        this.loginListener = loginListener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login,null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPicCode();
    }

    /**
     * 初始化view
     */
    public void initView(View view) {
        mPicCodeBtn = (ImageView) view.findViewById(R.id.picCode_iv);

        mUsernameEt = (LoginClearableEditText) view.findViewById(R.id.username_et);
        mUsernameEt.setText("18615322132");
        mUsernameEt.setMaxLength(11);
        mPasswordEt = (MyPwdEditText) view.findViewById(R.id.password_et);
        mPasswordEt.setMaxLength(20);
        mPasswordEt.setText("psklpskl1");
        mPasswordEt.setPasswordFormat();
        mPicCodeEt = (EditText) view.findViewById(R.id.picCode_et);


        mTvForgerPassword = (TextView) view.findViewById(R.id.tv_forget_password);
        mTvRegister = (TextView) view.findViewById(R.id.tv_register);
        mBtnLogin = (Button) view.findViewById(R.id.btn_login);

        mTvForgerPassword.setOnClickListener(baseOnClickListener);
        mTvRegister.setOnClickListener(baseOnClickListener);
        mBtnLogin.setOnClickListener(baseOnClickListener);

    }
    private OnClickEffectiveListener baseOnClickListener = new  OnClickEffectiveListener(){

        @Override
        public void onClickEffective(View view) {
            switch (view.getId()) {
                case R.id.btn_login:
                    // 登录
                    String sMobile = mUsernameEt.getText().toString().trim();
                    if (!Utils.isValidMobile(sMobile)) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.get_correct_mobile),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String passwordHash = mPasswordEt.getText().toString().trim();
                    if (!passwordHash.matches(LTNConstants.PASSWORD_PATTERN)) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.get_correct_password),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }


                    String picCode = mPicCodeEt.getText().toString().trim();
                    if (mPicCodeEt.length() < 4) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.get_correct_verifycode),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // TODO: 使用唯一的机器码
                    login(LTNConstants.CLIENT_TYPE_MOBILE, sMobile, passwordHash, mUuid, picCode);
                    break;
                case R.id.tv_forget_password:
                    //  跳转到 忘记密码 页面
                    Intent mIntent = new Intent(getActivity(), ForgetPasswordActivity.class);
                    startActivity(mIntent);
                    break;
                case R.id.tv_register:
                    //跳转到 注册 页面
                    Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(registerIntent);
                    break;
                default:
                    break;

            }
        }
    };
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
//        mUuid = Utils.genUUid();
//        RequestParams mReqParams = new RequestParams();
//        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
//        mReqParams.add(LTNConstants.MACHINE_NUMBER, mUuid);
//
//        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.PICTURE_CODE_HOST, mReqParams,
//                new JsonHttpResponseHandler() {
//                    String pictureUrl = null;
//
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
//                        try {
//                           // Log.d(TAG, jsonObject.toString());
//                            JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
//
//                            pictureUrl = (String) resultObj.getString(LTNConstants.PICTURE_CODE);
//
//                            LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.IMAGE_HOST + pictureUrl, null,
//                                    new AsyncHttpResponseHandler() {
//                                        @Override
//                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                                if (responseBody.length > 0) {
//                                                    Matrix matrix = new Matrix();
//                                                    Bitmap sBitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
//                                                    Bitmap tBitmap = Bitmap.createScaledBitmap(sBitmap,
//                                                            mPicCodeBtn.getWidth(), mPicCodeBtn.getHeight(), true);
//                                                    mPicCodeBtn.setImageBitmap(tBitmap);
//                                                } else {
//                                                    Toast.makeText(getActivity(),
//                                                            getResources().getString(R.string.ERROR_PIC_CODE),
//                                                            Toast.LENGTH_SHORT).show();
//                                                }
//                                        }
//
//                                        @Override
//                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                                        }
//                                    });
//
//                        } catch (JSONException je) {
//
//                        }
//                    }
//                });
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
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.MOBILE_NO, _mobielNo);
        mReqParams.add(LTNConstants.PASSWORD, _passwordHash);

        // 去穿
        mReqParams.add(LTNConstants.MACHINE_NUMBER, _machineNumber);
        mReqParams.add(LTNConstants.PICTURE_CODE, _picCode);

        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.LOGIN_CODE_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    String pictureUrl = null;

                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                           // Log.d(TAG, jsonObject.toString());
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                // 存储sessionKey到本地
                                SharedPreferences.Editor sharedUser = LTNApplication.getInstance().getSharedPreferences(
                                        LTNConstants.LTN_CONFIG, Context.MODE_PRIVATE)
                                        .edit();
                                sharedUser.putString(LTNConstants.SESSION_KEY,
                                        resultObj.getString(LTNConstants.SESSION_KEY));
                                sharedUser.commit();
                                LTNApplication.getInstance().setSessionKey(resultObj.getString(LTNConstants.SESSION_KEY));
                                User.getUserInstance().setUserPhone(_mobielNo);
                                // 进入到MainActivity


                                // 跳转到绑定卡页面.
//                                Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
//                             //   Intent mIntent = new Intent(LoginActivity.this, AccountInfoActivity.class);
//                                startActivity(mIntent);

//                                LoginUtils.loginJump(LoginDialogFragment.this, getIntent().getExtras());
//                                finish();
                                if(loginListener!=null){
                                 loginListener.onLoginSuccess();
                                    dismissAllowingStateLoss();
                                }
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(getActivity(),
                                        resultMsg, Toast.LENGTH_LONG).show();
                                if(loginListener!=null){
                                    loginListener.onLoginFailed(resultMsg);
                                    dismissAllowingStateLoss();
                                }
                            }
                        } catch (JSONException je) {
                           // Log.d(TAG, je.getMessage());
                            if(loginListener!=null){
                                loginListener.onLoginFailed("网络异常");
                                dismissAllowingStateLoss();
                            }
                        }
                    }
                });
    }

}

