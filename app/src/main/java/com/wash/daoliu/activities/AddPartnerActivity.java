package com.wash.daoliu.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.wash.daoliu.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tendcloud.tenddata.TCAgent;

import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jiajia on 2016/1/5.
 */
public class AddPartnerActivity extends BaseActivity {
    private Button confirm = null;
    private EditText content = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partner);
        initView();
    }

    private void initView() {
        content = (EditText) findViewById(R.id.content);
        findViewById(R.id.back_btn).setOnClickListener(this);
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText(getString(R.string.partner_add) + "信息");
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirm.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.confirm:
                String mobile = content.getText().toString();
                if (TextUtils.isEmpty(mobile)) {
                    return;
                }
                if (!Utils.isValidMobile(mobile)) {
                    Toast.makeText(this, getResources().getString(R.string.get_correct_mobile),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mobile.equals(LTNApplication.getInstance().getCurrentUser().getUserInfo().getMobile())) {

                    Toast.makeText(AddPartnerActivity.this, "您输入的推荐人号码是您自己的手机号码!", Toast.LENGTH_SHORT).show();

                    //为确认按钮添加事件,执行退出应用操作
                    return;
                }
                addPartner(mobile);
                break;
        }
    }

    private void addPartner(String mobile) {
        // TODO: use GSON later
        // 判断是否是本人

        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.MOBILE_NO, mobile);

        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.REPLENISH, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                Toast.makeText(AddPartnerActivity.this, "补充成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(AddPartnerActivity.this, jsonObject.optString(LTNConstants.RESULT_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException je) {
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(AddPartnerActivity.this, R.string.ERROR_CODE_1, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Talking data
        TCAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Talking data
        TCAgent.onPause(this);
    }
}
