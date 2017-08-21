package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.Coupon;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/21.
 */
public class ProductTYBuyActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ProductTYBuyActivity.class.getSimpleName();


    TextView tvProductName;
    ImageView ivProductImage;
    TextView tvProductAnnualIncome;
    TextView tvProductDeadline;
    TextView tvProductRemainingAmount;
    TextView acbAgree;
    ImageView ivCoupon;

    TextView tvBirdCoin;
    TextView tvCouponId;
    TextView tvOrderAmount;

    String mCouponId = "";
    double mOrderAmount = 10000;
    double mBirdCoin = 13.18;
    String mProductId;

    Coupon mCoupon;

    String expireDate;

    Button btnBuy;
    private boolean mCouponSelected = true; // 默认为true

    // coupon1
    TextView tvCouponDeadline1, tvCouponCondition1, tvCouponName1;
    RelativeLayout rlCoupon1;
    ImageView ivCoupon1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.product_ty_buy);


        mCoupon = (Coupon) getIntent().getExtras().getSerializable(LTNConstants.COUPONS);

        mProductId = getIntent().getExtras().getString(LTNConstants.PRO_Id);
        expireDate = getIntent().getExtras().getString(LTNConstants.PRODUCT_EXPIRE_DATE);

        initView();
        initData();
    }

    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("订单确认");
        findViewById(R.id.trust_html).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        tvProductName = (TextView) findViewById(R.id.tv_name);
        ivProductImage = (ImageView) findViewById(R.id.img_show);
        tvProductAnnualIncome = (TextView) findViewById(R.id.tv_amountIncome);
        tvProductDeadline = (TextView) findViewById(R.id.tv_deadline);
        acbAgree = (TextView) findViewById(R.id.checkbox_agreement);
        acbAgree.setSelected(true);
        acbAgree.setOnClickListener(this);
        ivCoupon = (ImageView) findViewById(R.id.iv_coupon1);

        ((TextView) findViewById(R.id.tv_deadline_value)).setText(expireDate);
        ((TextView) findViewById(R.id.tv_daishou_shouyi_value)).setText("13.18 鸟币");
        btnBuy = (Button) findViewById(R.id.btn_buy);
        btnBuy.setOnClickListener(this);
        ivCoupon1 = (ImageView) findViewById(R.id.iv_coupon_1);
        rlCoupon1 = (RelativeLayout) findViewById(R.id.rl_coupon1);
        tvCouponDeadline1 = (TextView) findViewById(R.id.tv_coupon_deadline_1);
        tvCouponCondition1 = (TextView) findViewById(R.id.tv_coupon_condition_1);
        tvCouponName1 = (TextView) findViewById(R.id.tv_coupon_name_1);

        rlCoupon1.setOnClickListener(this);
        btnBuy.setEnabled(mCouponSelected);
        setCheckCoupon();


    }

    public void initData() {
        tvCouponName1.setText(mCoupon.getCouponName() + " " + mCoupon.getAmount());
        tvCouponDeadline1.setText("有效期至" + mCoupon.getCouponDate());
        tvCouponCondition1.setText("" + mCoupon.getDesc());
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_buy:
                //  获取验证码;
                buyProduct();
                break;
            case R.id.rl_coupon1:
                mCouponSelected = !mCouponSelected;
                setCheckCoupon();
                setButtonEnable();
                break;
            case R.id.checkbox_agreement:
                acbAgree.setSelected(!acbAgree.isSelected());
                setButtonEnable();
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.trust_html:
                Intent intent = new Intent(this, LTNWebPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.H5_PROFIT_URL);
                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "投资协议");
                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
    }
    public void setButtonEnable(){
        if (acbAgree.isSelected()&&mCouponSelected) {
            btnBuy.setEnabled(true);
            btnBuy.setBackgroundResource(R.drawable.button_radius_no_round);
        } else {
            btnBuy.setEnabled(false);
            btnBuy.setBackgroundResource(R.color.label_grey1);
        }
    }
    private void setCheckCoupon() {
        if (mCouponSelected) {
            ivCoupon.setImageResource(R.drawable.icon_selected);
        } else {
            ivCoupon.setImageResource(R.drawable.icon_choice);
        }
        btnBuy.setEnabled(mCouponSelected);
    }

    /**
     * 重写BaseActivity 中的 clickEffective 方法
     *
     * @param v
     */
    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_buy:
                //  获取验证码;
                buyProduct();
                break;
        }

    }


    public void buyProduct() {
        // TODO: 完善标志
        if (!acbAgree.isSelected()) {
            Toast.makeText(ProductTYBuyActivity.this, "请先同意投资协议", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mCouponSelected) {
            Toast.makeText(ProductTYBuyActivity.this, "请先选择体验金券", Toast.LENGTH_SHORT).show();
            return;
        }


        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.ORDER_AMOUNT, "10000");  // hard code
        mReqParams.add(LTNConstants.BIRD_COIN, "0");
        mReqParams.add(LTNConstants.USER_COUPON, String.valueOf(mCoupon.getUserCouponId()));
        mReqParams.add(LTNConstants.PRO_Id, mProductId);

        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.PRODUCT_BUY_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                           // Log.d(TAG, jsonObject.toString());
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                // 返回优惠券
                                //TODO: 跳转到购买产品成功页面

                                Intent nIntent = new Intent(ProductTYBuyActivity.this, ProductBuySuccessActivity.class);
                                startActivity(nIntent);
                                // 跳转后,不能回退回来
                                finish();
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(ProductTYBuyActivity.this, resultMsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException je) {
                           // Log.d(TAG, je.getMessage());
                        }
                    }
                });

    }

}
