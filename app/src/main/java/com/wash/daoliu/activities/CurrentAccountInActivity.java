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

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.CurrentAccount;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.TrustView;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jiajia on 2016/1/20.
 */
public class CurrentAccountInActivity extends BaseActivity implements TrustView.OnTrustCheckChanged {

    private static final String TAG = CurrentAccountInActivity.class.getSimpleName();
    private EditText money_edit = null;
    private TextView current_account = null;
    private Button submit = null;
    private TrustView trustView = null;

    String leastRevenueAmount = "0";
    double currentRemainAmount = 0;
    double availableAmount = 0;
    String account;
    CurrentAccount currentAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_current_account_in);
        leastRevenueAmount = String.valueOf(getIntent().getExtras().getInt(LTNConstants.LEAST_REVENUE_AMOUNT));
        currentRemainAmount = getIntent().getExtras().getDouble(LTNConstants.CURRENT_REMAIN_AMOUNT);
        availableAmount = getIntent().getExtras().getDouble(LTNConstants.AVAILABLE_AMOUNT);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LTNApplication.getInstance().getSessionKey() != null) {
            getCurrentItem();
        }
    }

    private void initView() {
        View view = findViewById(R.id.top_layout);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        trustView = (TrustView) findViewById(R.id.trust_view);
        trustView.setData("我已同意", "《随心投协议》", LTNConstants.ACCESS_URL.H5_CURRENT_AGREEMENT_URL);

        trustView.setOnTrustCheckChanged(this);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        money_edit = (EditText) findViewById(R.id.money_edit);
        current_account = (TextView) findViewById(R.id.current_account);
        current_account.setText("￥" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(availableAmount));
        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText(R.string.current_account_in_title);
        money_edit.setHint("剩余金额:" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(currentRemainAmount));
        money_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submit.setEnabled(s.length() > 0 && trustView.isAgreeSelected());
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
                if (!trustView.isAgreeSelected()) {
                    Toast.makeText(this, "请先同意随心投协议", Toast.LENGTH_SHORT).show();
                    return;
                }
                account = money_edit.getText().toString();
                if (currentRemainAmount < Double.valueOf(account)) {
                    Toast.makeText(this, "购买的活期金额大于剩余金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.valueOf(account)< 1) {
                    Toast.makeText(this, "活期购买金额必须大于1元", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!com.wash.daoliu.utility.TextUtils.checkNum(account)){
                    ViewUtils.showErrorDialogWithTitle(this, getString(R.string.money_error), getString(R.string.money_error_hint), getString(R.string.know), null);
                    return;
                }
                if (checkCanBuy(account)) {
                    User.UserInfo userInfo = User.getUserInstance().getUserInfo();
                    if (userInfo.getAgreementTZ().equals("0")) {
                        gotoSign();
                    } else {
                        jumpToConfirm();
                    }
                }

                break;
        }
    }

    private void jumpToConfirm() {
        Intent intent = new Intent(CurrentAccountInActivity.this, CurrentAccountInConfirmActivity.class);
        intent.putExtra(LTNConstants.CURRENT_IN_ACCOUNT, Double.parseDouble(account));
        intent.putExtra(LTNConstants.CURRENT_IN_ACCOUNT_AFTER, currentAccount.current_hold_amount);
        intent.putExtra(LTNConstants.ANNUAL_INCOME_PERDAY, getIntent().getDoubleExtra(LTNConstants.ANNUAL_INCOME_PERDAY, 0));
        startActivity(intent);
    }

    private void gotoSign() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.AGREEMENT_TYPE, LTNConstants.AGREEMENT_TYPE_TZ);

        showProgressDialog(CurrentAccountInActivity.this, "跳转免密支付中...");
        submit.setEnabled(false);
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.MIANMI_AGREEMENT_URL, mReqParams,
                new JsonHttpResponseHandler() {

                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            dismissProgressDialog();
                            submit.setEnabled(true);
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                // 跳转到确认购买页面
                                String url = (String) resultObj.getString(LTNConstants.RETURN_URL);
                                // 在新的webpage中打开一个url
                                Intent intent = new Intent(CurrentAccountInActivity.this, LTNWebPageActivity.class);
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


                            }
                        } catch (JSONException je) {
                        }
                    }
                });
    }

    public boolean checkCanBuy(String account) {
        boolean flag = true;
        if (Double.valueOf(account) > availableAmount) {
            ViewUtils.showWarnDialog2(CurrentAccountInActivity.this, "您的余额不足,请去先充值", "去充值", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CurrentAccountInActivity.this, DepositActivity.class);
                    // 传值过去
                    Bundle b = new Bundle();
                    b.putBoolean(LTNConstants.CURRENT_DEPOSIT, true);
                    intent.putExtras(b);
                    startActivityForResult(intent, LTNConstants.CURRENT_DEPOSIT_VALUE);
                }
            }, null);
            flag = false;
        }
        return flag;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LTNConstants.PRODUCT_BUY_SUCCESS:  // 从购买成功跳转古来.
            case LTNConstants.CURRENT_BUY_SUCCESS: // 从购买成功后跳转过来
//                Intent mIntent = new Intent(CurrentAccountInActivity.this, CurrentAccountBuySuccessActivity.class);
////                //    mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                Bundle b = new Bundle();
//                b.putString(LTNConstants.AVAILABLE_AMOUNT ,account);
//                mIntent.putExtras(b);
//                startActivity(mIntent);

                // setResult(RESULT_OK);
                finish();
                break;
            case LTNConstants.CURRENT_DEPOSIT_VALUE:
                getCurrentItem();
                break;
            case LTNConstants.GRANT_RESULT_SUCCESS:
                User.getUserInstance().getUserInfo().setAgreementTZ("1");
//                buy(account);
                jumpToConfirm();
                break;

        }

    }

    @Override
    public void onChanged(boolean flag) {
        submit.setEnabled(flag && money_edit.getText().toString().length() > 0);

    }

    public void getCurrentItem() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.CURRENT_AMOUNT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                Gson gson = new Gson();
                                JSONObject accountInfoObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                currentAccount = gson.fromJson(accountInfoObj.toString(), CurrentAccount.class);
                                current_account.setText("￥" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(currentAccount.available_amount));
                                availableAmount = currentAccount.available_amount;

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
