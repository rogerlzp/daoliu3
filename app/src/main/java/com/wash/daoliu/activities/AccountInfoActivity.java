package com.wash.daoliu.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.ActivityUtils;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;
import com.wash.daoliu.utility.MarketUtils;
import com.wash.daoliu.utility.ShareUtils;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.MyEditText;
import com.wash.daoliu.view.UISwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/18.
 */
public class AccountInfoActivity extends BaseActivity implements UMShareListener {
    private static final String TAG = AccountInfoActivity.class.getSimpleName();
    private TextView tvUserAuth, tvBindBankCard, tvModifyLoginPwd, tvResetTradePwd, tvModifyTradePwd,
            tvModifyGesturePwd;

    private Button mLogoutBtn;

    private MyEditText myEditText;

    private TextView tvTitle, tv_share; //标题
    private UISwitchButton mUISwitchButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);
        initView();
    }

    @Override
    public void onClick(View v) {
        Intent nIntent;
        switch (v.getId()) {
            case R.id.back_btn:
                //  获取验证码;
                super.onBackPressed();
                break;
            case R.id.tv_feedback:
                Intent intent = null;
                if (TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
                    intent = new Intent(this, LoginActivity.class);
                    Bundle b = new Bundle();
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_FEEDBACK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtras(b);
                } else {
                    intent = new Intent(this, FeedBackActivity.class);
                }
                startActivity(intent);

                break;
            case R.id.tv_score:
                //TODO: 等在应用市场确定后在修改

                int result = MarketUtils.mainMarkets(this);

                try {
                    Uri uri = Uri.parse("market://details?id=" + "com.wash.daoliu");
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    switch (result) {
                        case 1:
                            intent1.setPackage(MarketUtils.MARKET_YINGYONGBAO_APP);
                            break;
                        case 2:
                            intent1.setPackage(MarketUtils.MARKET_360_APP);
                            break;
                        default:
                            break;
                    }
                    startActivity(intent1);
                } catch (Exception e) {
                    Toast.makeText(this, "请先去安装应用宝或者其他主流应用市场!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_about:
                intent = new Intent(this, LTNWebPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.H5_ABOUT_URL);
                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "关于神马贷款");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_phone:
                ViewUtils.showCallDialog(this, LTNConstants.PHONE_NUMBER);
                break;

            case R.id.set_host:
                intent = new Intent(this, SetHostActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_help:
                intent = new Intent(this, LTNWebPageActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.H5_HELP_URL);
                bundle1.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "帮助中心");
                intent.putExtras(bundle1);
                startActivity(intent);
                break;

            case R.id.btn_logout:
                // 确认
                logout();
                break;

            case R.id.tv_share:
                if(Build.VERSION.SDK_INT>=23){
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,
                            Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,
                            Manifest.permission.WRITE_APN_SETTINGS};
                    ActivityCompat.requestPermissions(this,mPermissionList,123);
                }

                ShareUtils.share(AccountInfoActivity.this, this);
                break;
        }
    }

    public void getUserInfo() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.MY_USER_INFO_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                // 将用户信息存储到
                                Gson gson = new Gson();
                                JSONObject accountInfoObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                LTNApplication.getInstance().getCurrentUser().setUserInfo(gson.fromJson(accountInfoObj.toString(), User.UserInfo.class));
                                initData();
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                            }
                        } catch (JSONException je) {

                        }
                    }

                    //TODO: 增加对网络错误的处理,等待产品统一确定
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        // Log.d(TAG, "onFailure in UserUtils" + errorResponse.toString());

                    }
                });

    }

    public void initData() {

    }

    public void initView() {


        findViewById(R.id.tv_feedback).setOnClickListener(this);
        findViewById(R.id.tv_score).setOnClickListener(this);
        findViewById(R.id.tv_about).setOnClickListener(this);
        findViewById(R.id.tv_phone).setOnClickListener(this);
        findViewById(R.id.tv_share).setOnClickListener(this);
        findViewById(R.id.tv_help).setOnClickListener(this);


        mLogoutBtn = (Button) findViewById(R.id.btn_logout);
        mLogoutBtn.setOnClickListener(this);

        findViewById(R.id.tv_share).setOnClickListener(this);


        this.findViewById(R.id.back_btn).setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(getResources().getText(R.string.account_settings));

    }


    public void logout() {
        ViewUtils.showWarnDialog(this, getString(R.string.quit_app), getString(R.string.btn_confirm),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        RequestParams mReqParams = new RequestParams();
                        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
                        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
                        //TODO: 添加错误或者失败的跳转
                        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.LOGOUT_URL, mReqParams,
                                new JsonHttpResponseHandler() {
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                                        try {
                                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {

                                            } else {
                                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                            }
                                        } catch (JSONException je) {

                                        }
                                    }

                                    //TODO: 增加对网络错误的处理,等待产品统一确定
                                    public void onFailure(int statusCode, Header[] headers,
                                                          Throwable throwable, JSONObject errorResponse) {
                                    }
                                });

                        // 先提交请求,不等返回结果,直接从客户端退出
                        LTNApplication.getInstance().clearUser();
                        ActivityUtils.finishAll();
                        Intent intent = new Intent(AccountInfoActivity.this, MainActivity.class);
                        startActivity(intent);


                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();
        // Talking data
        TCAgent.onResume(this);
        //   getUserInfo();
        //    mUISwitchButton.setChecked(LTNApplication.getInstance().getNotification());
    }

    @Override
    public void onPause() {
        super.onPause();
        // Talking data
        TCAgent.onPause(this);
    }


    @Override
    public void onStart(SHARE_MEDIA share_media) {
        if (share_media == SHARE_MEDIA.QQ) {

        }
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }


}
