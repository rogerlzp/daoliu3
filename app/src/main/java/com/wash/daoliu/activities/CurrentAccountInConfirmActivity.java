package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;
import com.wash.daoliu.utility.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jiajia on 2016/2/22.
 */
public class CurrentAccountInConfirmActivity extends BaseActivity {
    private TextView tv_daishou_shouyi_value = null;
    double inAcount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_buy_comfirm);
        ((TextView) findViewById(R.id.title)).setText(R.string.confirm_buy);
        inAcount = getIntent().getDoubleExtra(LTNConstants.CURRENT_IN_ACCOUNT, 0);
        double totalHold = getIntent().getDoubleExtra(LTNConstants.CURRENT_IN_ACCOUNT_AFTER, 0) + inAcount;
        ((TextView) findViewById(R.id.tv_pay_value)).setText(inAcount + "元");
        ((TextView) findViewById(R.id.tv_deadline_value)).setText(totalHold + "元");
        tv_daishou_shouyi_value = (TextView) findViewById(R.id.tv_daishou_shouyi_value);
        double perday = getIntent().getDoubleExtra(LTNConstants.ANNUAL_INCOME_PERDAY, 0);
        double income = perday * totalHold / 10000;
        if (income < 0.01) {
            tv_daishou_shouyi_value.setText("0.00元");
        } else {
            tv_daishou_shouyi_value.setText(TextUtils.formatDoubleValueWithUnit(income));
        }
        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_confirm:
                showProgressDialog(this, "加载中...");
                buy();
                break;

        }
    }

    public void buy() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.ORDER_AMOUNT2, Double.toString(inAcount));
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.PRODUCT_CURRENT_BUY, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        dismissProgressDialog();

                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {

                                Intent intent = new Intent(CurrentAccountInConfirmActivity.this, CurrentAccountBuySuccessActivity.class);
                                intent.putExtra(LTNConstants.AVAILABLE_AMOUNT, Double.toString(inAcount));
                                startActivity(intent);
                            }
                        } catch (JSONException je) {
                            // Log.e("JSONException", je.toString());
                        }
                    }

                    //TODO: 增加对网络错误的处理,等待产品统一确定
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        LogUtils.e("" + statusCode);
                        dismissProgressDialog();
                    }
                });

    }
}
