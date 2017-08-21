package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.fragment.ChooseCouponDialogFragment;
import com.wash.daoliu.fragment.ProductBuySuccessFragment;
import com.wash.daoliu.model.Coupon;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.UISwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/21.
 */
public class ProductBuyActivity extends BaseActivity implements ProductBuySuccessFragment.ProductBuyListener, ChooseCouponDialogFragment.OnChooseCouponListener {

    private static final String TAG = ProductBuyActivity.class.getSimpleName();

    Button btnBuy;
    TextView tvBuyAmount;
    TextView cbAgreement;
    TextView tvBirdcoin;
    TextView tvDeadlineValue;
    TextView tvDaishouShouyi;
    TextView tvPayValue;
    UISwitchButton tbBirdCoin;

    ArrayList<Coupon> mCoupons = new ArrayList<Coupon>();

    double birdCoinAmount, availableCoinAmount;
    double expectedRevenue;
    String expireDate;
    String orderAmount;
    String couponId;
    String paytAmount;

    String productID;
    // coupon1
    TextView tvCouponDeadline1, tvCouponCondition1, tvCouponName1,
            tvCouponDeadline2, tvCouponCondition2, tvCouponName2, tvCouponOther;
    RelativeLayout rlCoupon1, rlCoupon2, rlCoupons;
    ImageView ivCoupon1, ivCoupon2;
    RelativeLayout rl_birdcoin; // 鸟币收益栏
    //
//    boolean mCoupon1Selected = true;  // 默认为选中
//    boolean mCoupon2Selected = false; // 默认为补选中

    int checkCoupon = -1;//-1不选 0选中第一个券 1选中第二个券 2选择去他券
    //  private ImageViewCheckBox.OnCheckedChangeListener mOnCheckedChangeListener;
    private ChooseCouponDialogFragment fragment = null;
    private String cString = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_buy);
        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cString = bundle.getString(LTNConstants.PRACELABLE_COUPONS);
            if (!TextUtils.isEmpty(cString)) {
                mCoupons = new Gson().fromJson(cString, new TypeToken<ArrayList<Coupon>>() {
                }.getType());
            }
        }
        availableCoinAmount = bundle.getDouble(LTNConstants.BIRD_COIN); //可用鸟币
        birdCoinAmount = availableCoinAmount;  //
        expectedRevenue = bundle.getDouble(LTNConstants.EXPECTED_REVENUE); // 预期收益
        expireDate = bundle.getString(LTNConstants.PRODUCT_EXPIRE_DATE);  // 产品到期时间
        orderAmount = bundle.getString(LTNConstants.ORDER_AMOUNT, null); // 支付金额
        paytAmount = orderAmount;
        productID = bundle.getString(LTNConstants.PRO_Id); // 产品ID

        initData();
        initCoupons();


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.trust_html:
                Intent intent = new Intent(this, LTNWebPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.H5_PROFIT_URL);
                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "投资协议");
                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
                intent.putExtras( bundle);
                startActivity(intent);
                break;
            case R.id.checkbox_agreement:
                cbAgreement.setSelected(!cbAgreement.isSelected());
                if (cbAgreement.isSelected()) {
                    btnBuy.setEnabled(true);
                    btnBuy.setBackgroundResource(R.drawable.button_radius_no_round);
                } else {
                    btnBuy.setEnabled(false);
                    btnBuy.setBackgroundResource(R.color.label_grey1);
                }
                break;
            case R.id.rl_coupon1:
                if (checkCoupon == 0) {
                    checkCoupon = -1;
                } else {
                    checkCoupon = 0;
                }
                checkConpon();
                break;

            case R.id.rl_coupon2:
                if (checkCoupon == 1) {
                    checkCoupon = -1;
                } else {
                    checkCoupon = 1;
                }
                checkConpon();
                break;
            case R.id.tv_coupon_other:
//                Bundle b = getIntent().getBundleExtra(LTNConstants.BUNDLE_OBJ);
                if (!TextUtils.isEmpty(cString)) {
                    if (fragment == null) {
                        fragment = new ChooseCouponDialogFragment();
                        Bundle b = new Bundle();
                        b.putString(LTNConstants.PRACELABLE_COUPONS, cString);
                        fragment.setArguments(b);
                    }
                    fragment.show(getSupportFragmentManager(), "showChooseCouponDialog");
                } else {
                    Toast.makeText(this, "您还没有券", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void initView() {
        rl_birdcoin = (RelativeLayout) findViewById(R.id.rl_birdcoin);
        btnBuy = (Button) findViewById(R.id.btn_buy);
        btnBuy.setOnClickListener(baseOnClickListener);
        tvBuyAmount = (TextView) findViewById(R.id.tv_buyamount);
        cbAgreement = (TextView) findViewById(R.id.checkbox_agreement);
        cbAgreement.setSelected(true);
        cbAgreement.setOnClickListener(this);
        findViewById(R.id.trust_html).setOnClickListener(this);
        tvBirdcoin = (TextView) findViewById(R.id.tv_birdcoin);
        tvDeadlineValue = (TextView) findViewById(R.id.tv_deadline_value);

        tvBirdcoin = (TextView) findViewById(R.id.tv_birdcoin);
        tvDeadlineValue = (TextView) findViewById(R.id.tv_deadline_value);

        tvDaishouShouyi = (TextView) findViewById(R.id.tv_daishou_shouyi_value);
        tvPayValue = (TextView) findViewById(R.id.tv_pay_value);
        tbBirdCoin = (UISwitchButton) findViewById(R.id.toggleButton);


        tbBirdCoin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    birdCoinAmount = availableCoinAmount;
                    // TODO: 加上鸟币计算规则
                } else {
                    birdCoinAmount = 0.0;
                }
                //   tvBirdcoin.setText("可用" + birdCoinAmount + "鸟币抵扣" + birdCoinAmount + "元");
                tvBuyAmount.setText("实付金额：" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(((double) Integer.parseInt(orderAmount) - birdCoinAmount)) + "元");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LTNConstants.PRODUCT_BUY_SUCCESS:
                finish();
                break;
            case LTNConstants.GRANT_RESULT_SUCCESS:
                User.getUserInstance().getUserInfo().setAgreementTZ("1");
                jumpToConfirm();
                break;
                //  ProductBuySuccessFragment dialog = new ProductBuySuccessFragment();
                //  dialog.show(ProductBuyActivity.this.getFragmentManager(), "");
        }

    }


    public void initData() {

        //待收收益
        tvDaishouShouyi.setText("" + expectedRevenue + "元");

        //鸟币收益
        tvBirdcoin.setText("" + birdCoinAmount);

        // 到期日期
        tvDeadlineValue.setText(expireDate);

        findViewById(R.id.back_btn).setOnClickListener(this);
        // set title
        ((TextView) findViewById(R.id.title)).setText("确认投资");

        // 应付金额
        ((TextView) findViewById(R.id.tv_pay_value)).setText(orderAmount + "元");


        // 默认为可见状态
        // 如果为鸟币为0,则不能显示
        if (availableCoinAmount == 0) {
            rl_birdcoin.setVisibility(View.GONE);
            findViewById(R.id.bird_coin_line).setVisibility(View.GONE);
        } else {
            tvBirdcoin.setText("可用" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(birdCoinAmount) + "鸟币抵扣" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(birdCoinAmount) + "元");
        }

        tvBuyAmount.setText("实付金额：" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(((double) Integer.parseInt(orderAmount) - birdCoinAmount)) + "元");
    }

    // TODO: 优化
    public void initCoupons() {
        rlCoupons = (RelativeLayout) findViewById(R.id.rl2);
        tvCouponOther = (TextView) findViewById(R.id.tv_coupon_other);
        tvCouponOther.setOnClickListener(this);
        ivCoupon2 = (ImageView) findViewById(R.id.iv_coupon_2);
        rlCoupon2 = (RelativeLayout) findViewById(R.id.rl_coupon2);
        tvCouponDeadline2 = (TextView) findViewById(R.id.tv_coupon_deadline_2);
        tvCouponCondition2 = (TextView) findViewById(R.id.tv_coupon_condition_2);
        tvCouponName2 = (TextView) findViewById(R.id.tv_coupon_name_2);

        ivCoupon1 = (ImageView) findViewById(R.id.iv_coupon_1);
        rlCoupon1 = (RelativeLayout) findViewById(R.id.rl_coupon1);
        tvCouponDeadline1 = (TextView) findViewById(R.id.tv_coupon_deadline_1);
        tvCouponCondition1 = (TextView) findViewById(R.id.tv_coupon_condition_1);
        tvCouponName1 = (TextView) findViewById(R.id.tv_coupon_name_1);

        rlCoupon1.setOnClickListener(this);
        rlCoupon2.setOnClickListener(this);
        if (mCoupons.size() > 0) {
            checkCoupon = 0;
        }
        setCouponShow();
        checkConpon();
    }

    //    LoginDialogFragment fragment = new LoginDialogFragment();
//    if(bundle!=null){
//        fragment.setArguments(bundle);
//    }
//    fragment.show(getFragmentManager(),"showLoginDialog");
    private void setCouponShow() {
        if (mCoupons == null) {
            findViewById(R.id.v5).setVisibility(View.GONE);
            rlCoupon1.setVisibility(View.GONE);
            rlCoupon2.setVisibility(View.GONE);
            rlCoupons.setVisibility(View.GONE);
            return;
        }
        switch (mCoupons.size()) {
            case 0:
                findViewById(R.id.v5).setVisibility(View.GONE);
                rlCoupon1.setVisibility(View.GONE);
                rlCoupon2.setVisibility(View.GONE);
                rlCoupons.setVisibility(View.GONE);
                break;
            case 1:
                rlCoupon1.setVisibility(View.VISIBLE);
                tvCouponName1.setText(mCoupons.get(0).getCouponName() + mCoupons.get(0).getAmount());
                tvCouponDeadline1.setText("有效期至" + mCoupons.get(0).getCouponDate());
                tvCouponCondition1.setText("" + mCoupons.get(0).getAmount());
                rlCoupon2.setVisibility(View.GONE);
                break;
            case 2:
                rlCoupon1.setVisibility(View.VISIBLE);
                tvCouponName1.setText(mCoupons.get(0).getCouponName() + mCoupons.get(0).getAmount());
                tvCouponDeadline1.setText("有效期至" + mCoupons.get(0).getCouponDate());
                tvCouponCondition1.setText("" + mCoupons.get(0).getAmount());
                rlCoupon2.setVisibility(View.VISIBLE);
                tvCouponName2.setText(mCoupons.get(1).getCouponName() + mCoupons.get(1).getAmount());
                tvCouponDeadline2.setText("有效期至" + mCoupons.get(1).getCouponDate());
                tvCouponCondition2.setText("" + mCoupons.get(1).getAmount());
                break;
            default:
                // > 2个优惠券
                rlCoupon1.setVisibility(View.VISIBLE);
                tvCouponName1.setText(mCoupons.get(0).getCouponName() + mCoupons.get(0).getAmount());
                tvCouponDeadline1.setText("有效期至" + mCoupons.get(0).getCouponDate());
                tvCouponCondition1.setText("" + mCoupons.get(0).getAmount());
                rlCoupon2.setVisibility(View.VISIBLE);
                tvCouponName2.setText(mCoupons.get(1).getCouponName() + mCoupons.get(1).getAmount());
                tvCouponDeadline2.setText("有效期至" + mCoupons.get(1).getCouponDate());
                tvCouponCondition2.setText("" + mCoupons.get(1).getAmount());
                tvCouponOther.setVisibility(View.VISIBLE);

                break;
        }
    }


    private void gotoSign() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.AGREEMENT_TYPE, LTNConstants.AGREEMENT_TYPE_TZ);

        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.MIANMI_AGREEMENT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    String pictureUrl = null;

                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            // Log.d(TAG, jsonObject.toString());
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                // 跳转到确认购买页面
                                String url = (String) resultObj.getString(LTNConstants.RETURN_URL);
                                // 在新的webpage中打开一个url
                                Intent intent = new Intent(ProductBuyActivity.this, LTNWebPageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(LTNWebPageActivity.BUNDLE_URL, url);
                                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "联动优势");
                                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_PRODUCT_BUY);
                                intent.putExtras(bundle);
                                //  startActivity(intent);
                                // 登台跳转回来
                                startActivityForResult(intent, LTNConstants.FROM_WEBVIEW_INTERFACE);


                                // 购买成功后关闭该页面.
                                // finish();


                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(ProductBuyActivity.this,
                                        resultMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException je) {
                        }
                    }
                });
    }
//    private void gotoBuy_Nopwd() {
//        if (!cbAgreement.isSelected()) {
//            Toast.makeText(this, "请先同意投资协议", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        RequestParams mReqParams = new RequestParams();
//        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
//        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
//        mReqParams.add(LTNConstants.ORDER_AMOUNT, orderAmount);
//        mReqParams.add(LTNConstants.BIRD_COIN, Double.toString(birdCoinAmount));
//        if (checkCoupon > -1) {
//            // 选择优惠券
//            mReqParams.add(LTNConstants.USER_COUPON, mCoupons.get(checkCoupon).getUserCouponId());
//        } else {
//            mReqParams.add(LTNConstants.USER_COUPON, "0"); // 没有选择优惠券
//        }
//        mReqParams.add(LTNConstants.PRO_Id, productID);
//
//        //TODO: 添加错误或者失败的跳转
//        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.PRODUCT_BUY_URL, mReqParams,
//                new JsonHttpResponseHandler() {
//                    String pictureUrl = null;
//
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
//                        try {
//                            // Log.d(TAG, jsonObject.toString());
//                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
//
//                            // 验证码错误
//                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
//                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
//
//                                // 跳转到确认购买页面
//                                String url = (String) resultObj.getString(LTNConstants.RETURN_URL);
//                                // 在新的webpage中打开一个url
//                                Intent intent = new Intent(ProductBuyActivity.this, LTNWebPageActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString(LTNWebPageActivity.BUNDLE_URL, url);
//                                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "联动优势");
//                                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_PRODUCT_BUY);
//                                intent.putExtra(LTNConstants.BUNDLE_OBJ, bundle);
//                                //  startActivity(intent);
//                                // 登台跳转回来
//                                startActivityForResult(intent, LTNConstants.FROM_WEBVIEW_INTERFACE);
//
//
//                                // 购买成功后关闭该页面.
//                                // finish();
//
//
//                            } else {
//                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
//                                Toast.makeText(ProductBuyActivity.this,
//                                        resultMsg, Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException je) {
//                        }
//                    }
//                });
//    }


    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_buy:
                // 跳转到购买页面
                if (!cbAgreement.isSelected()) {
                    Toast.makeText(this, "请先同意投资协议", Toast.LENGTH_SHORT).show();
                    return;
                }
                User.UserInfo userInfo = User.getUserInstance().getUserInfo();
                if (userInfo.getAgreementTZ().equals("0")) {
                    gotoSign();
                } else {
                    jumpToConfirm();
                }
                break;
            default:
                break;

        }
    }
    private void jumpToConfirm(){
        Intent intent = new Intent(this, ProductBuyConfirmActivity.class);
        Bundle bundle = new Bundle(getIntent().getExtras());
        bundle.putDouble(LTNConstants.BIRD_COIN, birdCoinAmount);
        if (checkCoupon > -1) {
            // 选择优惠券
            bundle.putString(LTNConstants.USER_COUPON, mCoupons.get(checkCoupon).getUserCouponId());
            bundle.putString(LTNConstants.USER_COUPON_VALUE,mCoupons.get(checkCoupon).getCouponName() + mCoupons.get(checkCoupon).getAmount());
        } else {
            bundle.putString(LTNConstants.USER_COUPON, "0"); // 没有选择优惠券
            bundle.putString(LTNConstants.USER_COUPON_VALUE,"");
    }
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void checkConpon() {
        switch (checkCoupon) {
            case -1:
                ivCoupon1.setImageResource(R.drawable.icon_choice);
                ivCoupon2.setImageResource(R.drawable.icon_choice);
                couponId = "0";
                break;
            case 0:
                ivCoupon1.setImageResource(R.drawable.icon_selected);
                ivCoupon2.setImageResource(R.drawable.icon_choice);
                break;
            case 1:
                ivCoupon1.setImageResource(R.drawable.icon_choice);
                ivCoupon2.setImageResource(R.drawable.icon_selected);
                break;
        }
    }

    @Override
    public void onBuyProductSucceedListener() {
        // 跳转到购买产品页面
//        Intent mIntent = new Intent(ProductBuyActivity.this, MyInvestActivity.class);
//        startActivity(mIntent);
        Intent mIntent = new Intent(ProductBuyActivity.this, MainActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mIntent);
        finish();
    }


    @Override
    public void onChoose(Coupon coupon) {
        if (mCoupons == null) {
            return;
        }
        mCoupons.clear();
        mCoupons.add(coupon);
        setCouponShow();
        checkCoupon = 0;
        checkConpon();
        fragment.dismiss();
    }
}
