package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jiajia on 2016/1/4.
 */
public class PartnerActivity extends BaseActivity implements View.OnClickListener {
    private TextView welcomeTitle = null;
    private TextView reward_money = null;
    private TextView tv_partner_reword = null;
    private TextView tv_partner_friend = null;
    private TextView tv_partner_intend = null;
    private TextView tv_partner_add = null;
    private TextView has_add = null;

    String userLevel = "普通用户";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        initView();
        getPartnerReward();
        //
        if (getIntent().getBooleanExtra(LTNConstants.IS_REGISTER, false)) {
            Utils.showRealNameWarn(this);
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
                                // 更新title

                                if ((!TextUtils.isEmpty(User.getUserInstance().getUserInfo().getUserName())) && !User.getUserInstance().getUserInfo().getUserName().equals("null")) {
                                    welcomeTitle.setText("您好，" + User.getUserInstance().getUserInfo().getUserName());
                                } else {
                                    welcomeTitle.setText("您好，" + User.getUserInstance().getUserInfo().getMobile());
                                }
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

    private void initView() {
        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("合伙人");
        welcomeTitle = (TextView) findViewById(R.id.welcome_title);


        if ((!TextUtils.isEmpty(User.getUserInstance().getUserInfo().getUserName())) && !User.getUserInstance().getUserInfo().getUserName().equals("null")) {
            welcomeTitle.setText("您好，" + User.getUserInstance().getUserInfo().getUserName());
        } else {
            // 如果没有用户名的话,则拉取一下
            getUserInfo();
            //    welcomeTitle.setText("您好，" + User.getUserInstance().getUserInfo().getMobile());
        }

        reward_money = (TextView) findViewById(R.id.reward_money);
        tv_partner_reword = (TextView) findViewById(R.id.tv_partner_reword);
        tv_partner_friend = (TextView) findViewById(R.id.tv_partner_friend);
        tv_partner_intend = (TextView) findViewById(R.id.tv_partner_intend);
        tv_partner_add = (TextView) findViewById(R.id.tv_partner_add);
        has_add = (TextView) findViewById(R.id.has_add);
        findViewById(R.id.reward_layout).setOnClickListener(this);
        tv_partner_reword.setOnClickListener(this);
        tv_partner_friend.setOnClickListener(this);
        tv_partner_intend.setOnClickListener(this);
        tv_partner_add.setOnClickListener(this);
        findViewById(R.id.tv_my_qrcode).setOnClickListener(this);

    }

    private void getData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.reward_layout:
                Intent intent = new Intent(this, ParterEarnings.class);
                startActivity(intent);
                break;
            case R.id.tv_partner_reword://合伙人奖励规则
                intent = new Intent(this, LTNWebPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.H5_REWARD_RULE_URL);
                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "合伙人奖励规则");
                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_partner_friend://好友统计
                intent = new Intent(this, PartnerCountActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_partner_intend://马上邀请好友
//                ShareUtils.share(this, new UMShareListener() {
//                    @Override
//                    public void onResult(SHARE_MEDIA platform) {
//                        //        Toast.makeText(LTNApplication.getInstance(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA platform, Throwable t) {
//                        //      Toast.makeText(LTNApplication.getInstance(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA platform) {
//                        //    Toast.makeText(LTNApplication.getInstance(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//                    }
//                });
                break;
            case R.id.tv_partner_add:
                intent = new Intent(this, AddPartnerActivity.class);
                startActivityForResult(intent, 1001);
                break;
            case R.id.tv_my_qrcode:
                intent = new Intent(this, QRCodeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            getPartnerReward();
        }
    }

    public void getPartnerReward() {
        // TODO: use GSON later
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.PARTNER_REWARD, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();
                            if (jsonObject != null && jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                int isPartnerDown = dataObj.optInt("isPartnerDown");
                                int isPartnerUp = dataObj.optInt("isPartnerUp");
                                int isStaff = dataObj.optInt("isStaff");

                                userLevel = dataObj.optString("userLerver");
                                String totalReward = dataObj.optString("totalReward");
                                String mobile = dataObj.optString("mobile");
                                reward_money.setText(totalReward);
                                setLevel(isPartnerDown);
                                //
                                if (isStaff == 1) {
                                    has_add.setVisibility(View.VISIBLE);
                                    tv_partner_add.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                    tv_partner_add.setEnabled(false);
                                    has_add.setText("员工");
                                } else if (isPartnerUp == 0) {
                                    has_add.setVisibility(View.GONE);
                                    tv_partner_add.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_enter), null);
                                    tv_partner_add.setEnabled(true);
                                } else {
                                    has_add.setVisibility(View.VISIBLE);
                                    tv_partner_add.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                    tv_partner_add.setEnabled(false);
                                    has_add.setText(com.wash.daoliu.utility.TextUtils.replaceStarToString(mobile));
                                }
                            }
                        } catch (JSONException je) {
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    }
                });

    }

    private void setLevel(int isPartnerDown) {
        String name = User.getUserInstance().getUserInfo().getUserName();
        String mobile = User.getUserInstance().getUserInfo().getMobile();
        welcomeTitle.setText(String.format("您好，%s，%s", (TextUtils.isEmpty(name) ? mobile : name), userLevel));

        String level = isPartnerDown == 0 ? "0" : User.getUserInstance().getUserInfo().getUserLevelId();
        //  welcomeTitle.setText(String.format("您好，%s，%s", (TextUtils.isEmpty(name) ? mobile : name), UserLevel.getEnName(level).getValue()));
    }
}

enum UserLevel {
    STATUS_O("0", "马上成为合伙人"),
    STATUS_1001("1001", "普通合伙人"),
    STATUS_1002("1002", "金牌合伙人");

    private String code;
    private String value;

    UserLevel(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static UserLevel getEnName(String code) {
        for (UserLevel userlevel : UserLevel.values()) {
            if (userlevel.code.equalsIgnoreCase(code)) {
                return userlevel;
            }
        }
        return UserLevel.STATUS_O;
    }

}
