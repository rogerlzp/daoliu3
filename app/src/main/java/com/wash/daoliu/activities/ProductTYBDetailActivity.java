package com.wash.daoliu.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.Coupon;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.ProductTagView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/21.
 */
public class ProductTYBDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ProductTYBDetailActivity.class.getSimpleName();

    TextView tvProductName;
    ProductTagView productTagView;
    ImageView ivProductImage, ivReturn;
    TextView tvProductAnnualIncome;
    TextView tvProductDeadline;
    TextView tvProductRemainingAmount;
    TextView tvProductProfitType;
    TextView tvProductRateDate;
    TextView tv_deadline_unit;
    TextView tv_tyb_desc;

    String productID, repaymentType, staRateDate, productTag, productType, productTitle,productDeadLineUnit;
    int productDeadline;
    float productRemainAmount, productTotalAmount, annualIncome;
    String annualIncomeText;

    Button btnBuy;                       // 购买按钮
    Button btnLogin;                       // 登录按钮
    Button btnRegister;                       // 注册按钮
    LinearLayout rlNotLogin;                // 没有登录时候显示的按钮

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("" + Constant.LoginParams.LOGIN_TO_TYB_PRODUCT)) {
                rlNotLogin.setVisibility(View.GONE);
                btnBuy.setVisibility(View.VISIBLE);
            }
            if(intent.getBooleanExtra(LTNConstants.IS_REGISTER, false)){
                Utils.showRealNameWarn(ProductTYBDetailActivity.this);
            }else{
                ProductTYBDetailActivity.this.setResult(Activity.RESULT_OK);
                ProductTYBDetailActivity.this.finish();
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_ty_detail);

        Bundle bundle = getIntent().getExtras();


        productTag = bundle.getString(LTNConstants.PRO_Tags); //产品标签
        productID = bundle.getString(LTNConstants.PRO_Id); // 产品ID
        productRemainAmount = bundle.getFloat(LTNConstants.PRO_PrAmount); // 产品余额
        productDeadline = bundle.getInt(LTNConstants.PRO_Deadline);      // 截止时间
        repaymentType = bundle.getString(LTNConstants.PRO_RepaymentType);  // 还款方式
        //    staRateDate = bundle.getString(LTNConstants.PRO_StaRateDate);   //起息日期
        staRateDate = bundle.getString(LTNConstants.PRO_RateCalculateType);   //计息方式
        productTitle = bundle.getString(LTNConstants.PRO_Title);   //产品title
        productDeadLineUnit = bundle.getString(LTNConstants.PRO_DEADLINEUNIT);   //投资期限单位


//        annualIncome = bundle.getFloat(LTNConstants.PRO_AnnualIncome); // 年收益率
        annualIncomeText = bundle.getString(LTNConstants.PRO_AnnualIncomeText);
        productType = bundle.getString(LTNConstants.PRO_Type);  // 产品类型
        productTag = bundle.getString(LTNConstants.PRO_Tags); //产品标签

        initView();
        initData();

        registerBroadcastReceiver();

    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.LoginParams.LOGIN_TO_TYB_PRODUCT);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void initView() {
        tv_deadline_unit = (TextView) findViewById(R.id.tv_deadline_unit);
        tv_tyb_desc = (TextView) findViewById(R.id.tv_tyb_desc);
        tvProductName = (TextView) findViewById(R.id.tv_name);
        productTagView = (ProductTagView) findViewById(R.id.tag_view);
        productTagView.setTag(productTag, productType);
        ivProductImage = (ImageView) findViewById(R.id.img_show);
        tvProductAnnualIncome = (TextView) findViewById(R.id.tv_amount_income_value);
        tvProductDeadline = (TextView) findViewById(R.id.tv_deadline_value);
        tvProductRemainingAmount = (TextView) findViewById(R.id.tv_remain_amount_value);

        tvProductProfitType = (TextView) findViewById(R.id.tv_profit_type_value);
        tvProductRateDate = (TextView) findViewById(R.id.tv_st_date_value);

        rlNotLogin = (LinearLayout) findViewById(R.id.ll6);
        btnBuy = (Button) findViewById(R.id.btn_buy);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);

        btnBuy.setOnClickListener(baseOnClickListener);

        tv_deadline_unit.setText(productDeadLineUnit);
        if (LTNApplication.getInstance().getSessionKey() == null) {
            btnBuy.setVisibility(View.GONE);
            rlNotLogin.setVisibility(View.VISIBLE);
        } else {
            btnBuy.setVisibility(View.VISIBLE);
            rlNotLogin.setVisibility(View.GONE);
        }

        ivReturn = (ImageView) findViewById(R.id.iv_return);
        ivReturn.setOnClickListener(this);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        ((ImageView) findViewById(R.id.iv_birdcoin)).setOnClickListener(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void initData() {

        tvProductAnnualIncome.setText(annualIncomeText); //
        tvProductDeadline.setText("" + productDeadline);
        // TODO: hard code it
        tvProductRemainingAmount.setText("10000"); // 体验金券为1万元

        if (repaymentType.equals(LTNConstants.PRODUCT.REPAYMENT_YCXHK)) {
            tvProductProfitType.setText(LTNConstants.PRODUCT.REPAYMENT_YCXHK_CODE);
        }
        tvProductRateDate.setText(staRateDate);

        tv_tyb_desc.setText(productTitle);
    }


    private void preBuy() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.ORDER_AMOUNT, LTNConstants.TYB_AMOUNT);
        mReqParams.add(LTNConstants.PRO_Id, productID);

        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.PRODUCT_PREBUY_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                           // Log.d(TAG, jsonObject.toString());

                            // 体验标没有result code
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                Coupon coupon = new Coupon();
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                // 返回 coupon, 等后面确认
                                JSONArray couponArray = (JSONArray) resultObj.get(LTNConstants.COUPONS);

                                Gson gson = new Gson();
                                coupon = gson.fromJson(couponArray.get(0).toString(), Coupon.class);


                                // 跳转到确认购买页面
                                Intent mIntent = new Intent(ProductTYBDetailActivity.this, ProductTYBuyActivity.class);
                                Bundle bundle = new Bundle();
                                if (coupon != null) {
                                    bundle.putSerializable(LTNConstants.COUPONS, coupon);
                                }

                                bundle.putString(LTNConstants.PRO_Id, productID); // 产品ID
                                bundle.putString(LTNConstants.PRODUCT_EXPIRE_DATE, (String) resultObj.get(LTNConstants.PRODUCT_EXPIRE_DATE)); // 产品ID
                                mIntent.putExtras(bundle);
                                startActivity(mIntent);

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(ProductTYBDetailActivity.this,
                                        resultMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException je) {
                        }
                    }
                });
    }


    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();
        Intent mIntent;
        switch (viewId) {
            case R.id.btn_buy:
                // 跳转到购买页面
                // 检查是否已经买过
                preBuy();
                break;
            case R.id.iv_birdcoin:
                ViewUtils.showBirdCoinHelpDialog(this);
                break;
            case R.id.btn_login:
                // 跳转到登录页面
                // TODO: 带入 产品ID


                if (TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    Bundle b = getIntent().getExtras();
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_TYB_PRODUCT);
                    intent.putExtras(b);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;
            case R.id.btn_register:
                // 跳转到注册页面
                // TODO: 带入 产品ID
                Intent intent = new Intent(this, RegisterActivity.class);
                Bundle b = getIntent().getExtras();
                b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_TYB_PRODUCT);
                intent.putExtras(b);
                startActivity(intent);

                break;
        }

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Intent mIntent;
        switch (viewId) {
            case R.id.iv_return:
                finish();
                break;
            case R.id.iv_birdcoin:
                ViewUtils.showBirdCoinHelpDialog(this);
                break;
            case R.id.btn_login:
                // 跳转到登录页面
                // TODO: 带入 产品ID


                if (TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    Bundle b = getIntent().getExtras();
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_TYB_PRODUCT);
                    intent.putExtras(b);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;
            case R.id.btn_register:
                // 跳转到注册页面
                // TODO: 带入 产品ID
                Intent intent = new Intent(this, RegisterActivity.class);
                Bundle b = getIntent().getExtras();
                b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_TYB_PRODUCT);
                intent.putExtras(b);
                startActivity(intent);

                break;


        }

    }
}