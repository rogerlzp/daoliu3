package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;
import com.wash.daoliu.utility.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jiajia on 2016/1/20.
 */
public class CurrentAccountOutActivity extends BaseActivity {
    private EditText money_edit = null;
    private TextView current_account = null;
    private Button submit = null;
    double currentHoldAmount = 0;
    String leastRevenueAmount = "0";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_current_account_out);

        currentHoldAmount = getIntent().getExtras().getDouble(LTNConstants.CURRENT_HOLD_AMOUNT);
        leastRevenueAmount = String.valueOf(getIntent().getExtras().getInt(LTNConstants.LEAST_REVENUE_AMOUNT));


        initView();

    }

    private void initView() {
        View view = findViewById(R.id.top_layout);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        money_edit = (EditText) findViewById(R.id.money_edit);

        //TODO: 添加每天最大转出限制
        money_edit.setHint("持有金额:" + currentHoldAmount);

        current_account = (TextView) findViewById(R.id.current_account);
        current_account.setText("￥" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(currentHoldAmount));

        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText(R.string.current_account_out_title);
        money_edit.addTextChangedListener(new TextWatcher() {
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

        ((TextView) findViewById(R.id.tips)).setText(String.format(getResources().getText(R.string.current_account_out_tips).toString(), leastRevenueAmount));

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.submit:
                // 判断条件
                String amount = money_edit.getText().toString().trim();
                if (Double.valueOf(amount) > currentHoldAmount) {
                    Toast.makeText(CurrentAccountOutActivity.this, "转出金额不能大于持有金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!com.wash.daoliu.utility.TextUtils.checkNum(amount)){
                    ViewUtils.showErrorDialogWithTitle(this,getString(R.string.money_error),getString(R.string.money_error_hint),getString(R.string.know),null);
                    return;
                }
                extract(amount);
                break;
        }
    }

    public void extract(final String amount) {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.ORDER_AMOUNT2, amount);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.PRODUCT_CURRENT_EXTRACT, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                Intent intent = new Intent(CurrentAccountOutActivity.this, CurrentAccountOutSuccessActivity.class);
                                intent.putExtra(LTNConstants.OUT_MONEY, amount);
                                startActivity(intent);
                                finish();
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(LTNApplication.getInstance(), resultMsg, Toast.LENGTH_SHORT);
                            }
                        } catch (JSONException je) {
                            // Log.e("JSONException", je.toString());
                        }
                    }

                    //TODO: 增加对网络错误的处理,等待产品统一确定
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        // Log.d(TAG, "onFailure in UserUtils");
                        LogUtils.e("" + statusCode);
                    }
                });

    }


}
