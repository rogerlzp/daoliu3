package com.wash.daoliu.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.Coupon;
import com.wash.daoliu.model.Product;
import com.wash.daoliu.model.PurchaseRecord;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.UserUtils;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.ProductTagView;
import com.wash.daoliu.view.TipsPopupWindowUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/21.
 */
public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ProductDetailActivity.class.getSimpleName();
    TextView tv_deadlin_day = null;//投资期限单位
    TextView tvProductName;                  // 产品名称
    ProductTagView productTagView;   // 产品Tag
    ImageView ivProductImage;                // 产品图片
    TextView tvProductAnnualIncome;          // 年化收益率
    TextView tvProductDeadline;              // 投标期限
    TextView tvProductRemainAmount;       // 剩余金额
    TextView tvStaInvestAmount;             // 起投金额
    TextView tvRepaymentType;            // 收益方式
    TextView tvStaRateDate;              // 起息日期
    TextView tvProgressValue;                // 进度

    TextView tvDetailDeadlineValue;          // 截止日期
    RelativeLayout rlDetail;

    TextView tvUserBalanceValue;             // 可用余额
    TextView tvBirdcoinValue;             // 可用鸟币
    EditText etOrderAmountValue;          // 投资值

    Button btnBuy;                       // 购买按钮

    Button btnLogin;                       // 登录按钮
    Button btnRegister;                       // 注册按钮

    RelativeLayout rlBuy;                // 没有登录时候显示的按钮

    LinearLayout llNotLogin;
    RelativeLayout rlRecord;                  // 显示的购买记录
    TextView tvRecordValue;                  // 一条购买记录

    String orderAmount;

    String productID, status, repaymentType, staRateDate, productTag, raiseEndDate, detailUrl, productName, productType, productDeadlineUnit;
    int productDeadline, staInvestAmount;
    double productRemainAmount, productTotalAmount, annualIncome;

    String annualIncomText;
    ImageView ivReturn;

    com.wash.daoliu.view.MyProgressBar pbBuyProgress;

    Product mProduct;

    private ArrayList<PurchaseRecord> recordList = new ArrayList<PurchaseRecord>(); // 购买记录列表

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("" + Constant.LoginParams.LOGIN_TO_PRODUCT)) {
                llNotLogin.setVisibility(View.GONE);
                rlBuy.setVisibility(View.VISIBLE);
                // 更新页面
                // 获取账户信息
                if (LTNApplication.getInstance().getSessionKey() != null) {
                    UserUtils.getAccountInfo();

                    // 获取用户信息
                    UserUtils.getUserInfo();
                }
                tvUserBalanceValue.setText("" + LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance());
                tvBirdcoinValue.setText("" + LTNApplication.getInstance().getCurrentUser().getAccount().getBirdCoin());
                showPopWindow();
            }
            if (intent.getBooleanExtra(LTNConstants.IS_REGISTER, false)) {
                com.wash.daoliu.utility.Utils.showRealNameWarn(ProductDetailActivity.this);
            }
        }
    };

    private void showPopWindow() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LTNApplication.getInstance().getSessionKey() != null && (!productType.equals(LTNConstants.PRODUCT.TYPE_XSB)) && !(status.equals(LTNConstants.PRODUCT.STATUS_JS) || status.equals(LTNConstants.PRODUCT.STATUS_HKZ) ||
                        status.equals(LTNConstants.PRODUCT.STATUS_YHK))) {
                    if (productTotalAmount == productRemainAmount) {
                        TipsPopupWindowUtils.showPopupView(ProductDetailActivity.this, findViewById(R.id.last_layout), "壕鸟哥悄悄话：首笔有效投资有奖励，先下手为强！", 0, -ViewUtils.dip2px(ProductDetailActivity.this, 80));
                    } else if (productRemainAmount != 0 && productRemainAmount <= staInvestAmount * 3) {
                        TipsPopupWindowUtils.showPopupView(ProductDetailActivity.this, findViewById(R.id.last_layout), "“壕鸟哥的秘密：理财项目成功扫尾，神秘奖励收入囊中！”", 0, -ViewUtils.dip2px(ProductDetailActivity.this, 80));
                    }
                }
            }
        }, 500);
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.LoginParams.LOGIN_TO_PRODUCT);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        setContentView(R.layout.product_detail);

        registerBroadcastReceiver();

        initView();

        Bundle bundle = getIntent().getExtras();
        productID = bundle.getString(LTNConstants.PRO_Id); // 产品ID
        if (bundle.getString(LTNConstants.FROM_JPUSH) == null) {
            productRemainAmount = bundle.getFloat(LTNConstants.PRO_PrAmount); // 产品余额
            productTotalAmount = bundle.getFloat(LTNConstants.PRO_PtAmount); // 产品总额
            productDeadline = bundle.getInt(LTNConstants.PRO_Deadline);      // 截止时间
            repaymentType = bundle.getString(LTNConstants.PRO_RepaymentType);  // 还款方式
            staRateDate = bundle.getString(LTNConstants.PRO_StaRateDate);   //起息日期
            productDeadlineUnit = bundle.getString(LTNConstants.PRO_DEADLINEUNIT);//截止时间日期
            // staRateDate = bundle.getString(LTNConstants.PRO_RateCalculateType);   //计息方式
            raiseEndDate = bundle.getString(LTNConstants.PRO_RaiseEndDate);   //截止时间
            annualIncome = bundle.getFloat(LTNConstants.PRO_AnnualIncome); // 年收益率
            productName = bundle.getString(LTNConstants.PRO_Name);   // 产品名称
            detailUrl = bundle.getString(LTNConstants.PRO_DetailUrl);   // 产品详情页面
            productType = bundle.getString(LTNConstants.PRO_Type);  // 产品类型
            productTag = bundle.getString(LTNConstants.PRO_Tags); //产品标签
            staInvestAmount = bundle.getInt(LTNConstants.PRO_StaInvestAmount); //起投金额
            productName = bundle.getString(LTNConstants.PRO_Name); // 产品名称
            annualIncomText = bundle.getString(LTNConstants.PRO_AnnualIncomeText); // 产品格式
            status = bundle.getString(LTNConstants.PRO_Status); //产品装

            initData();
        } else {
            getPurductDetail();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void getAccountInfo() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.MY_ACCOUNT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                // 将用户信息存储到
                                // Log.e("TAG", "sessionkey--" + LTNApplication.getInstance().getSessionKey());
                                Gson gson = new Gson();
                                JSONObject accountInfoObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                // Log.e("TAG", jsonObject.toString());
                                LTNApplication.getInstance().getCurrentUser().setAccount(gson.fromJson(accountInfoObj.toString(), User.Account.class));
                                tvUserBalanceValue.setText("" + LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance());
                                tvBirdcoinValue.setText("" + LTNApplication.getInstance().getCurrentUser().getAccount().getBirdCoin());

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                // Log.e("TAG", resultCode + "+++" + resultMsg);
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

                    }
                });

    }

    Handler handler = new Handler();


    public void getPurchaseRecord() {

        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        //   mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.CURRENT_PAGE, "" + 0);  // 首页
        mReqParams.add(LTNConstants.PAGE_SIZE, "" + LTNConstants.PAGE_COUNT); // 页面
        mReqParams.add(LTNConstants.PRO_Id, productID);

        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.PURCHASE_RECORD_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                JSONArray recordArray = resultObj.getJSONArray(LTNConstants.PRODUCT.PURCHASE_HISTORY_LIST);
                                Gson gson = new Gson();
                                recordList = gson.fromJson(recordArray.toString(), new TypeToken<ArrayList<PurchaseRecord>>() {
                                }.getType());
                                if (recordList != null && recordList.size() > 0) {
                                    PurchaseRecord pr = (PurchaseRecord) recordList.get(0);
                                    if (TextUtils.isEmpty(pr.getOrderDate())) {
                                        return;
                                    }
                                    tvRecordValue.setText(com.wash.daoliu.utility.TextUtils.replaceStarToString(pr.getUserName()) + " 投资 "
                                    );
                                } else {
                                    // TODO: 确认没有购买记录的时候怎么改
                                    tvRecordValue.setText("----");
                                }

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(ProductDetailActivity.this,
                                        resultMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException je) {
                        }
                    }


                });
    }


    public void getPurductDetail() {

        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add("id", productID);

        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.PURCHASE_DETAIL_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                Gson gson = new Gson();
                                mProduct = gson.fromJson(resultObj.toString(), Product.class);
                                //  productID = mProduct.getProductId(); // 产品ID

                                productRemainAmount = mProduct.getProductRemainAmount(); // 产品余额
                                productTotalAmount = mProduct.getProductTotalAmount(); // 产品总额
                                productDeadline = mProduct.getProductDeadline();      // 截止时间
                                repaymentType = mProduct.getRepaymentType();  // 还款方式
                                staRateDate = mProduct.getStaRateDate();   //起息日期
                                productDeadlineUnit = mProduct.getDeadlineUnit();//投资期限单位
                                raiseEndDate = mProduct.getRaiseEndDate();   //截止时间
                                annualIncome = mProduct.getAnnualIncome(); // 年收益率
                                productName = mProduct.getProductName();   // 产品名称
                                detailUrl = mProduct.getDetailUrl();   // 产品详情页面
                                productType = mProduct.getProductType();  // 产品类型
                                productTag = mProduct.getProductTag(); //产品标签
                                staInvestAmount = mProduct.getStaInvestAmount(); //起投金额
                                annualIncomText = mProduct.getAnnualIncomeText(); // 产品格式
                                status = mProduct.getProductStatus(); //产品装

                                initData();

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(ProductDetailActivity.this,
                                        resultMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException je) {
                        }
                    }


                });
    }

    public void initView() {
        pbBuyProgress = (com.wash.daoliu.view.MyProgressBar) findViewById(R.id.pb_buyProgress);
        tvProductAnnualIncome = (TextView) findViewById(R.id.tv_amount_income_value); // 1
        tvProductDeadline = (TextView) findViewById(R.id.tv_deadline_value);
        tvProductName = (TextView) findViewById(R.id.tv_product_name);
        tvProductRemainAmount = (TextView) findViewById(R.id.tv_remain_amount_value);

        // 9 product tags
        productTagView = (ProductTagView) findViewById(R.id.tag_view);
        tvRepaymentType = (TextView) findViewById(R.id.tv_repayment_type_value);
        // 13 single limit amount
        etOrderAmountValue = (EditText) findViewById(R.id.et_st_amount_value);

        // 在此增加每次投资的 hint
        // 更改文字状态
        etOrderAmountValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnBuy.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 14
        tvStaInvestAmount = (TextView) findViewById(R.id.tv_sta_invest_amount_value);
        tvStaRateDate = (TextView) findViewById(R.id.tv_sta_rate_date_value);
        ivProductImage = (ImageView) findViewById(R.id.img_show);
        tvProgressValue = (TextView) findViewById(R.id.tv_progress_value);
        tvDetailDeadlineValue = (TextView) findViewById(R.id.tv_detail_deadline_value);
        tv_deadlin_day = (TextView) findViewById(R.id.tv_deadlin_day);

        rlDetail = (RelativeLayout) findViewById(R.id.rl7);
        rlDetail.setOnClickListener(this);

        tvUserBalanceValue = (TextView) findViewById(R.id.tv_userbalance_value);
        tvBirdcoinValue = (TextView) findViewById(R.id.tv_birdcoin_value);
        tvRecordValue = (TextView) findViewById(R.id.tv_record_value);
        rlRecord = (RelativeLayout) findViewById(R.id.rl8);
        llNotLogin = (LinearLayout) findViewById(R.id.ll10);
        btnBuy = (Button) findViewById(R.id.btn_buy);
        View view = findViewById(R.id.title_layout);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnBuy.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);


        rlBuy = (RelativeLayout) findViewById(R.id.bottom_layout);


        rlRecord.setOnClickListener(this);

        findViewById(R.id.tv_detail_deadline_value).setOnClickListener(this);
        ivReturn = (ImageView) findViewById(R.id.back_btn);
        ivReturn.setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("详情");


    }

    public void initData() {
        tv_deadlin_day.setText(productDeadlineUnit);

        if (productType.equals(LTNConstants.PRODUCT.TYPE_XSB)) {
            productTagView.setTag1(getString(R.string.xs_hint), "新手");
            etOrderAmountValue.setHint("投资金额" + staInvestAmount + "~10000元");
        } else {
            productTagView.setTag(productTag, productType);
            etOrderAmountValue.setHint("起投金额" + staInvestAmount + "元");
        }
        tvStaInvestAmount.setText("" + com.wash.daoliu.utility.TextUtils.formatDoubleValueWithUnit(productTotalAmount));
        SpannableString styledText = new SpannableString(annualIncomText);
        String[] textArray = annualIncomText.split("-");
        if (textArray.length > 1) { // 采用SpannableString格式
            //    textArray[0]
            styledText.setSpan(new TextAppearanceSpan(this, R.style.text_style1), 0, textArray[0].length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(this, R.style.text_style2), textArray[0].length() - 1, textArray[0].length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(this, R.style.text_style1), textArray[0].length(), textArray[0].length() + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(this, R.style.text_style1), textArray[0].length() + 1, annualIncomText.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(this, R.style.text_style2), annualIncomText.length() - 1, annualIncomText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            // 判断最后是否有%,没有的话,加上一个
            if (!annualIncomText.endsWith("%")) {
                annualIncomText = annualIncomText + "%";
                styledText = new SpannableString(annualIncomText);
            }
            styledText.setSpan(new TextAppearanceSpan(this, R.style.text_style1), 0, annualIncomText.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            styledText.setSpan(new TextAppearanceSpan(this, R.style.text_style2), annualIncomText.length() - 1, annualIncomText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvProductAnnualIncome.setText(styledText, TextView.BufferType.SPANNABLE); //不同的内容设置不同的大小


        tvProductDeadline.setText("" + productDeadline);
        tvProductRemainAmount.setText("" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(productRemainAmount) + "元");

        if (repaymentType != null && repaymentType.equals(LTNConstants.PRODUCT.REPAYMENT_YCXHK)) {
            tvRepaymentType.setText(LTNConstants.PRODUCT.REPAYMENT_YCXHK_CODE);
        }

        // tvStaRateDate.setText(staRateDate);

        if (staRateDate.length() > 10) {
            tvStaRateDate.setText(staRateDate.substring(0, 10));
        } else {
            tvStaRateDate.setText(com.wash.daoliu.utility.TextUtils.convertDateToFromat1(staRateDate));
        }


        // 用户已经注册,再显示
        if (LTNApplication.getInstance().getSessionKey() != null) {
            tvUserBalanceValue.setText("" + LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance() + "元,");
            tvBirdcoinValue.setText("" + LTNApplication.getInstance().getCurrentUser().getAccount().getBirdCoin() + "鸟币");
        }

        int progress = (int) ((1.0 - productRemainAmount / productTotalAmount) * pbBuyProgress.getMax());
        pbBuyProgress.setProgress(progress);

        double dProgress = (double) ((1.0 - productRemainAmount / productTotalAmount) * 100);
        tvProgressValue.setText(com.wash.daoliu.utility.TextUtils.formatDoubleValue(dProgress) + "%");

        // 截止时间
        if (raiseEndDate.length() > 10) {
            tvDetailDeadlineValue.setText(raiseEndDate.substring(0, 10) + "截止");
        } else {
            tvDetailDeadlineValue.setText(com.wash.daoliu.utility.TextUtils.convertDateToFromat1(raiseEndDate) + "截止");
        }
        tv_deadlin_day.setText(productDeadlineUnit);
        tvDetailDeadlineValue.setOnClickListener(baseOnClickListener);
        tvProductName.setText(productName);

        // productRemainAmount
        if (productRemainAmount == 0) {
            rlBuy.setVisibility(View.INVISIBLE);
        }

        if (productRemainAmount < staInvestAmount) {
            etOrderAmountValue.setHint("起投金额" + productRemainAmount + "元");
        }

        if (LTNApplication.getInstance().getSessionKey() == null) {
            rlBuy.setVisibility(View.GONE);
            if (status.equals(LTNConstants.PRODUCT.STATUS_JS) || status.equals(LTNConstants.PRODUCT.STATUS_HKZ) ||
                    status.equals(LTNConstants.PRODUCT.STATUS_YHK)) {
                llNotLogin.setVisibility(View.GONE);
            } else {
                llNotLogin.setVisibility(View.VISIBLE);
            }
        } else {
            if (status.equals(LTNConstants.PRODUCT.STATUS_JS) || status.equals(LTNConstants.PRODUCT.STATUS_HKZ) ||
                    status.equals(LTNConstants.PRODUCT.STATUS_YHK)) {
                rlBuy.setVisibility(View.GONE);
                llNotLogin.setVisibility(View.GONE);
            } else {
                rlBuy.setVisibility(View.VISIBLE);
                llNotLogin.setVisibility(View.GONE);
            }
        }
        if (productType.equals(LTNConstants.PRODUCT.TYPE_XSB)) {
            findViewById(R.id.tv_birdcoin).setVisibility(View.GONE);
            findViewById(R.id.tv_birdcoin_value).setVisibility(View.GONE);
        }
        // 获取购买记录
        getPurchaseRecord();

        if (LTNApplication.getInstance().getSessionKey() != null) {
            // 获取账户信息
            UserUtils.getAccountInfo();
            // 获取用户信息
            UserUtils.getUserInfo();
        }
        showPopWindow();
    }


    @Override
    public void onClick(View v) {
//        super.onBackPressed();
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_buy:
                // 跳转到购买页面
                preBuy();
                break;
            case R.id.rl8:
                intent = new Intent(this, ProductBuyRecordDetailActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString(LTNConstants.PRO_Id, productID);
                intent.putExtras(bundle1);
                startActivity(intent);
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.tv_detail_deadline_value:
                // 跳转到详情页面
                if (detailUrl != null) {
                    // 如果有有详情页面,则可以跳转到url
                    // 在新的webpage中打开一个url
                    intent = new Intent(ProductDetailActivity.this, LTNWebPageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.HOST + detailUrl);
                    bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, productName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.btn_login:
                // 跳转到登录页面
                // TODO: 带入 产品ID
                if (TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
                    intent = new Intent(this, LoginActivity.class);
                    Bundle b = getIntent().getExtras();
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_PRODUCT);
                    intent.putExtras(b);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;
            case R.id.btn_register:
                // 跳转到注册页面
                // TODO: 带入 产品ID
                intent = new Intent(this, RegisterActivity.class);
                Bundle b = getIntent().getExtras();
                b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_PRODUCT);
                intent.putExtras(b);
                startActivity(intent);

                break;


        }
    }


    private void preBuy() {
        orderAmount = etOrderAmountValue.getText().toString().trim();
        if (TextUtils.isEmpty(orderAmount) || Integer.parseInt(orderAmount) == 0) {
            Toast.makeText(this, "请先输入投资金额", Toast.LENGTH_SHORT).show();
            return;
        }
        if (productType.equals(LTNConstants.PRODUCT.TYPE_XSB)) {
            if (productRemainAmount < staInvestAmount) {
                if (Double.parseDouble(orderAmount) != productRemainAmount) {  // 为空; TODO: 增加判断逻辑
                    Toast.makeText(ProductDetailActivity.this, "剩余金额已小于起投金额，投资金额须等于剩余金额", Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                if (Integer.parseInt(orderAmount) < staInvestAmount||Integer.parseInt(orderAmount)>10000) {  // 为空; TODO: 增加判断逻辑
                    Toast.makeText(ProductDetailActivity.this, "新手标投资金额须在100元-10000元", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Double.parseDouble(orderAmount) > productRemainAmount){
                    Toast.makeText(ProductDetailActivity.this, "输入的金额不能大于剩余金额", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else {
            // 产品剩余金额小于起投金额， 必须一次性购买
            if (productRemainAmount < staInvestAmount) {
                if (Double.parseDouble(orderAmount) != productRemainAmount) {  // 为空; TODO: 增加判断逻辑
                    Toast.makeText(ProductDetailActivity.this, "剩余金额已小于起投金额，投资金额须等于剩余金额", Toast.LENGTH_LONG).show();
                    return;
                }
            } else if (Integer.parseInt(orderAmount) < staInvestAmount) {  // 为空; TODO: 增加判断逻辑
                Toast.makeText(ProductDetailActivity.this, "输入的金额不能小于起投金额", Toast.LENGTH_SHORT).show();
                return;
            } else if (Double.parseDouble(orderAmount) > productRemainAmount) { // 输入金额不能大于剩余金额
                Toast.makeText(ProductDetailActivity.this, "输入的金额不能大于剩余金额", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 判断余额是否不足
        User user = LTNApplication.getInstance().getCurrentUser();
        if (user.getUserInfo().getUserName() == null) {  //没有实名认证
//            ViewUtils.showWarnDialog(ProductDetailActivity.this, getString(R.string.realname_text), getString(R.String),
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(ProductDetailActivity.this, UserAuthActivity.class);
//                            // 传值过去
//                            startActivity(intent);
//                        }
//                    });
            Utils.showRealNameWarn(ProductDetailActivity.this);

            return;
        } else if (user.getUserInfo().getBankAuthStatus() != null
                && user.getUserInfo().getBankAuthStatus().equals("0")) {  //没有绑卡
//            ViewUtils.showWarnDialog(ProductDetailActivity.this, "您没有绑定银行卡,请去先绑定银行卡", "去充值", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(ProductDetailActivity.this, BindBankCardActivity.class);
//                    // 传值过去
//                    startActivity(intent);
//                }
//            });
            Utils.showBindBanWarn(ProductDetailActivity.this);
            return;

        } else if (user.getAccount().getUsableBalance() < Integer.parseInt(orderAmount)) { //余额不足,去充值
            ViewUtils.showWarnDialog2(ProductDetailActivity.this, "您的余额不足,请去先充值", "去充值", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductDetailActivity.this, DepositActivity.class);
                    // 传值过去
                    startActivity(intent);
                }
            }, null);
            return;
        }


        // 首先判断是否已经实名认证 ""
//        if (Integer.parseInt(orderAmount) < staInvestAmount)
//
//        {  // 为空; TODO: 增加判断逻辑
//            Toast.makeText(ProductDetailActivity.this, "输入的金额不能小于起投金额", Toast.LENGTH_SHORT).show();
//            return;
//        }


        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.ORDER_AMOUNT, orderAmount);

        mReqParams.add(LTNConstants.PRO_Id, productID);

        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().

                post(LTNConstants.ACCESS_URL.PRODUCT_PREBUY_URL, mReqParams,
                        new JsonHttpResponseHandler() {
                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                                try {
                                    // Log.d(TAG, jsonObject.toString());
                                    String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                                    // 验证码错误
                                    if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                        JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                        double birdCoinAmount = resultObj.getDouble(LTNConstants.BIRD_COIN);
                                        double expectedRevenue = resultObj.getDouble(LTNConstants.EXPECTED_REVENUE);

                                        // 解析coupon
                                        JSONArray couponArray = resultObj.getJSONArray(LTNConstants.COUPONS);
                                        ArrayList<Coupon> couponList = new ArrayList<Coupon>();
                                        Gson gson = new Gson();
                                        couponList = gson.fromJson(couponArray.toString(), new TypeToken<ArrayList<Coupon>>() {
                                        }.getType());


                                        String expireDate = resultObj.getString(LTNConstants.PRODUCT_EXPIRE_DATE);

                                        // 跳转到确认购买页面
                                        Intent mIntent = new Intent(ProductDetailActivity.this, ProductBuyActivity.class);
                                        Bundle bundle = new Bundle();


                                        bundle.putString(LTNConstants.PRO_Id, productID);
                                        if (productType.equals(LTNConstants.PRODUCT.TYPE_XSB)) {
                                            bundle.putDouble(LTNConstants.BIRD_COIN, 0);    // 鸟币
                                            bundle.putString(LTNConstants.PRACELABLE_COUPONS, "");  // 传输 返现券列表
                                        } else {
                                            bundle.putDouble(LTNConstants.BIRD_COIN, birdCoinAmount);    // 鸟币
                                            bundle.putString(LTNConstants.PRACELABLE_COUPONS, couponArray.toString());  // 传输 返现券列表
                                        }
                                        bundle.putDouble(LTNConstants.EXPECTED_REVENUE, expectedRevenue);    // 预期收益
                                        bundle.putString(LTNConstants.PRODUCT_EXPIRE_DATE, expireDate);    // 产品到期时间
                                        bundle.putString(LTNConstants.ORDER_AMOUNT, orderAmount);

                                        mIntent.putExtras(bundle);
                                        startActivity(mIntent);

                                    } else {
                                        String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                        Toast.makeText(ProductDetailActivity.this,
                                                resultMsg, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException je) {
                                }
                            }


                        }

                );


    }


    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();
        Intent mIntent;
        switch (viewId) {

            case R.id.tv_detail_deadline_value:
                // 跳转到详情页面
                if (detailUrl != null) {
                    // 如果有有详情页面,则可以跳转到url
                    // 在新的webpage中打开一个url
                    Intent intent = new Intent(ProductDetailActivity.this, LTNWebPageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.HOST + detailUrl);
                    bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, productName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.btn_login:
                // 跳转到登录页面
                // TODO: 带入 产品ID
                if (TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    Bundle b = getIntent().getExtras();
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_PRODUCT);
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
                b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_PRODUCT);
                intent.putExtras(b);
                startActivity(intent);

                break;

            case R.id.rl8:
                // 跳转到购买记录页面
                mIntent = new Intent(ProductDetailActivity.this, ProductBuyRecordDetailActivity.class);
                startActivity(mIntent);
                break;

            default:
                break;

        }
    }

    public void gotoDetail() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (LTNApplication.getInstance().getSessionKey() != null)
            getAccountInfo();
    }

}
