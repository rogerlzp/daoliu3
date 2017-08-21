package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 16/2/19.
 */
public class DepositConfirmActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DepositConfirmActivity.class.getSimpleName();

    String orderAmount = null;
    String getOrderAmount = null;

    Button btnRecharge;
    TextView tvHintValue;
    String changeAmount;
    String deposit_note;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_confirm);
        orderAmount = getIntent().getExtras().getString(LTNConstants.ORDER_AMOUNT);
        initView();

    }

    public void initView() {
        findViewById(R.id.btn_deposit).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);

        ((TextView) findViewById(R.id.title)).setText("确认充值");
        ((TextView) findViewById(R.id.tv_deposit_get_value)).setText(orderAmount);
        ((TextView) findViewById(R.id.tv_deposit_value)).setText(orderAmount);
        ((TextView) findViewById(R.id.tv_cardId_value)).setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getBankNo());
        ((TextView) findViewById(R.id.tv_bank_value)).setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getBelongBank());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_deposit:
                recharge();
                break;
            case R.id.back_btn:
                finish();
                break;
        }

    }

    public void recharge() {

        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.ORDER_AMOUNT, orderAmount);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        showLoadingProgressDialog(DepositConfirmActivity.this, "正在充值中...");
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().getConnectTimeout();
        LTNHttpClient.getLTNHttpClient().getResponseTimeout();
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.DEPOSIT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {

                            dismissProgressDialog();
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                // 不需要跳转,直接结束了

                                Intent mIntent = new Intent(DepositConfirmActivity.this, DepositSuccessActivity.class);
                                startActivity(mIntent);
                                finish();

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                //TODO: 弹出错误页面
                                ViewUtils.showWarnDialog2(DepositConfirmActivity.this, resultMsg, "确定", "取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }, null);

                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }
                    }



                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        dismissProgressDialog();
                    }
                }

        );

    }


}
