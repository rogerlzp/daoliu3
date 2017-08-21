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

import org.json.JSONException;
import org.json.JSONObject;


import cz.msebera.android.httpclient.Header;

/**
 * Created by jiajia on 2016/2/22.
 */
public class ProductBuyConfirmActivity extends BaseActivity {
    TextView tvBirdcoin;
    TextView tvDeadlineValue;
    TextView tvDaishouShouyi;
    TextView tvPayValue;
    TextView tv_coupon_value;
    TextView tvBuyAmount;
    double birdCoinAmount;
    double expectedRevenue;
    String expireDate;
    String orderAmount;
    String productID;
    String checkCoupon;
    String checkCouponValue;
    Button btn_confirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_buy_comfirm);
        initView();
        initData();
    }

    public void initView() {
        ((TextView) findViewById(R.id.title)).setText(R.string.confirm_buy);
        findViewById(R.id.back_btn).setOnClickListener(this);
        tvBirdcoin = (TextView) findViewById(R.id.tv_birdcoin);
        tvDeadlineValue = (TextView) findViewById(R.id.tv_deadline_value);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        tvBirdcoin = (TextView) findViewById(R.id.tv_birdcoin_value);
        tvDeadlineValue = (TextView) findViewById(R.id.tv_deadline_value);
        tvDaishouShouyi = (TextView) findViewById(R.id.tv_daishou_shouyi_value);
        tvPayValue = (TextView) findViewById(R.id.tv_pay_value);
        tv_coupon_value = (TextView) findViewById(R.id.tv_coupon_value);
        tvBuyAmount = (TextView) findViewById(R.id.tv_buy_amount);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        birdCoinAmount = bundle.getDouble(LTNConstants.BIRD_COIN); //使用鸟币
        expectedRevenue = bundle.getDouble(LTNConstants.EXPECTED_REVENUE); // 预期收益
        expireDate = bundle.getString(LTNConstants.PRODUCT_EXPIRE_DATE);  // 产品到期时间
        orderAmount = bundle.getString(LTNConstants.ORDER_AMOUNT, null); // 支付金额
        productID = bundle.getString(LTNConstants.PRO_Id); // 产品ID
        checkCoupon = bundle.getString(LTNConstants.USER_COUPON);
        checkCouponValue = bundle.getString(LTNConstants.USER_COUPON_VALUE);
        //购买金额
        tvPayValue.setText(orderAmount + "元");
        //待收收益
        tvDaishouShouyi.setText("" + expectedRevenue + "元");
        // 到期日期
        tvDeadlineValue.setText(expireDate);
        //选择的抵扣鸟币
        if (birdCoinAmount == 0) {
            findViewById(R.id.birdcoin_layout).setVisibility(View.GONE);
            findViewById(R.id.divide).setVisibility(View.GONE);
        } else {
            findViewById(R.id.birdcoin_layout).setVisibility(View.VISIBLE);
            tvBirdcoin.setText(birdCoinAmount + "鸟币");
        }
        //选择的理财金券
        if (checkCoupon.equals("0")) {
            findViewById(R.id.coupon_layout).setVisibility(View.GONE);
            findViewById(R.id.divide).setVisibility(View.GONE);
        } else {
            findViewById(R.id.coupon_layout).setVisibility(View.VISIBLE);
            tv_coupon_value.setText(checkCouponValue);
        }
        if (birdCoinAmount == 0 && checkCoupon.equals("0")) {
            findViewById(R.id.other_layout).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.other_layout).setVisibility(View.VISIBLE);
        }
        tvBuyAmount.setText(com.wash.daoliu.utility.TextUtils.formatDoubleValue(((double) Integer.parseInt(orderAmount) - birdCoinAmount)) + "元");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_confirm:
                gotoBuy();
                break;
        }
    }

    private void gotoBuy() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.ORDER_AMOUNT, orderAmount);
        mReqParams.add(LTNConstants.BIRD_COIN, Double.toString(birdCoinAmount));
        mReqParams.add(LTNConstants.USER_COUPON, checkCoupon); // 没有选择优惠券
        mReqParams.add(LTNConstants.PRO_Id, productID);

        //
        showLoadingProgressDialog(ProductBuyConfirmActivity.this, "购买确认中...");
        btn_confirm.setEnabled(false);

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.PRODUCT_BUY_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            dismissProgressDialog();
                            btn_confirm.setEnabled(true);
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject data = jsonObject.optJSONObject("data");
                                boolean hasGoldenEgg = data.optBoolean("hasGoldenEgg");
                                Intent intent = new Intent(ProductBuyConfirmActivity.this, ProductBuySuccessActivity.class);
                                intent.putExtra("hasGoldenEgg",hasGoldenEgg);
                                intent.putExtra("dturl",LTNConstants.ACCESS_URL.HOST+data.getString(LTNConstants.DT_URL));
                                startActivity(intent);
                            }
                        } catch (JSONException je) {
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        dismissProgressDialog();
                        btn_confirm.setEnabled(true);
                    }
                });
    }
}
