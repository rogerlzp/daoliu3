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

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.Car;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bobo on 2015/12/30.
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {
    private EditText content = null;
    private Button submit = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
        if (getIntent().getBooleanExtra(LTNConstants.IS_REGISTER, false)) {
            Utils.showRealNameWarn(this);
        }
    }

    private void initView() {
        ((TextView) findViewById(R.id.title)).setText("意见反馈");
        findViewById(R.id.back_btn).setOnClickListener(this);
        content = (EditText) findViewById(R.id.content);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickEffectiveListener() {
            @Override
            public void onClickEffective(View view) {
                submitFeedback();
            }
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submit.setEnabled(s.length() > 0);
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
        }
    }

    //提交
    private void submitFeedback() {
        String contentString = content.getText().toString();
        if (TextUtils.isEmpty(contentString)) {
            Toast.makeText(this, "请输入反馈意见", Toast.LENGTH_SHORT);
            return;
        }

        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.put(LTNConstants.FEED_BACK, contentString);

        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.FEED_BACK,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                // 将用户信息存储到
                                Toast.makeText(FeedBackActivity.this,
                                        "提交成功!", Toast.LENGTH_LONG).show();
                                finish();

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(FeedBackActivity.this,
                                        resultMsg, Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException je) {
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                    }
                });


    }
}
