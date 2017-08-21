package com.wash.daoliu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.AgreementCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 16/1/10.
 */
public class UserHasAuthedActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = UserAuthActivity.class.getSimpleName();

    private TextView etRealname;
    private TextView etId;
    private Button btnAuth;
    private AgreementCheckBox chkMoney;

    private String userName, cardId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_authed);

        initView();

        // TODO: 如果已经实名了,则不能够再修改了
        initData();
       
    }

    public void initData() {
        // 有用户名的时候,则显示
        if (LTNApplication.getInstance().getCurrentUser().getUserInfo().getUserName() != null) {
            etRealname.setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getUserName());
            etId.setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getCardId());
        } else {
            getUserInfo();
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

                                etRealname.setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getUserName());
                                etId.setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getCardId());
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);

                            }
                        } catch (JSONException je) {
                        }
                    }
                });

    }


    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("实名认证");
        etRealname = (TextView) findViewById(R.id.et_realname);
        etId = (TextView) findViewById(R.id.et_id);

        this.findViewById(R.id.back_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;

        }

    }


}


