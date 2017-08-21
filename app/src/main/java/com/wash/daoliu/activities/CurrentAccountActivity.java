package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
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
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.SeekArc;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jiajia on 2016/1/20.
 */
public class CurrentAccountActivity extends BaseActivity {

    private static final String TAG = CurrentAccountActivity.class.getSimpleName();
    private SeekArc mSeekArc;
    private int seekArcPadding = 60;//半圆进度条距离屏幕边缘 dp
    private CurrentAccount currentAccount = null;
    private TextView tv_userbalance_value = null;//年化收益率
    private TextView tv_income_value = null;//每万元收益
    private TextView total_account = null;//活期持有金额
    private TextView tv_total_income = null;//持有金额累计收益
    private TextView tv_yesterday_income = null;//昨日收益
    private TextView total_out_money = null;//申请中转出金额
    private TextView tv_nhsy = null;
    private int CONTENT_HEITH = 70;

    int leastRevenueAmount = 0;

    String per_million_income;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_current_account);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LTNApplication.getInstance().getSessionKey() != null) {
            getCurrentItem();
        }

        // setData();

    }

    private void initView() {
        mSeekArc = (SeekArc) findViewById(R.id.seekArc);
        tv_userbalance_value = (TextView) findViewById(R.id.tv_userbalance_value);
        tv_income_value = (TextView) findViewById(R.id.tv_income_value);
        total_account = (TextView) findViewById(R.id.total_account);
        tv_total_income = (TextView) findViewById(R.id.tv_total_income);
        tv_yesterday_income = (TextView) findViewById(R.id.tv_yesterday_income);
        total_out_money = (TextView) findViewById(R.id.total_out_money);
        tv_nhsy = (TextView) findViewById(R.id.tv_nhsy);
        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText(R.string.current_account_title);
        mSeekArc.setArcWidth(5);
        mSeekArc.setProgressWidth(8);
        mSeekArc.setRoundedEdges(false);
        mSeekArc.setClockwise(true);
        mSeekArc.setTouchInSide(false);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int seekHeight = dm.widthPixels - ViewUtils.dip2px(this, seekArcPadding * 2);
        mSeekArc.getLayoutParams().height = seekHeight;
        findViewById(R.id.btn_out).setOnClickListener(this);
        findViewById(R.id.btn_in).setOnClickListener(this);
        int marginTop = seekHeight / 2 - ViewUtils.dip2px(this, CONTENT_HEITH);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_nhsy.getLayoutParams();
        params.topMargin = marginTop;

        findViewById(R.id.ll_tzqx).setOnClickListener(this);// 点击进入累计收益页面

        // 设置已经看过活期了
        LTNApplication.getInstance().setCheckedCurrent();

    }

    private void setData() {
        // currentAccount   = new CurrentAccount();
        if (currentAccount != null) {
            tv_userbalance_value.setText(currentAccount.annual_income_rate);
            tv_income_value.setText(currentAccount.per_million_income);
            total_account.setText("￥" + com.wash.daoliu.utility.TextUtils.formatDoubleValue(currentAccount.current_hold_amount));
            tv_total_income.setText(currentAccount.total_income);
            tv_yesterday_income.setText(currentAccount.lastday_income);
            try {
                if ((!TextUtils.isEmpty(currentAccount.applying_extract_amount)) && Double.parseDouble(currentAccount.applying_extract_amount.replace("元", "").trim()) > 0) {
                    total_out_money.setText(String.format(getString(R.string.out_money), currentAccount.applying_extract_amount));
                    total_out_money.setVisibility(View.VISIBLE);
                } else {
                    total_out_money.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                total_out_money.setVisibility(View.INVISIBLE);
            }
            double percent = (currentAccount.current_total_amount - currentAccount.current_remain_amount) / currentAccount.current_total_amount;
            int per = (int) (percent * 10000);
            mSeekArc.setProgress(per);
            findViewById(R.id.btn_out).setEnabled(currentAccount.current_hold_amount > 0);
            //   findViewById(R.id.btn_in).setEnabled(currentAccount.current_remain_amount > 0);


            per_million_income = currentAccount.per_million_income;
            if (per_million_income.startsWith("￥")) {
                per_million_income = per_million_income.substring(1, per_million_income.length());
            }
            try {
                String[] perArray = per_million_income.split("/");
                if (perArray.length > 0) {
                    per_million_income = perArray[0];
                }
            } catch (Exception e) {
                LogUtils.e("+++" + e.getMessage());
            }


            leastRevenueAmount = (int) (100 / Double.valueOf(per_million_income));

            // 如果已经满标,则显示出提示俩,否则隐藏,默认隐藏
            if (currentAccount.current_remain_amount <= 0) {
                findViewById(R.id.btn_in).setBackgroundResource(R.drawable.button_radius_in);
                //       findViewById(R.id.btn_in).setBackground(getDrawable(R.drawable.button_radius_in));
                //.setBackgroundColor(getResources().getColor(R.color.label_grey1));
            } else {
                findViewById(R.id.btn_in).setBackgroundResource(R.drawable.button_radius);

//                findViewById(R.id.btn_in).setBackground(getDrawable(R.drawable.button_radius));
                //       findViewById(R.id.btn_in).setBackgroundColor(getResources().getColor(R.color.button_color));
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            if (LTNApplication.getInstance().getSessionKey() != null) {
                getCurrentItem();
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_out://转出
                Intent intent = new Intent(this, CurrentAccountOutActivity.class);
                Bundle b = new Bundle();
                b.putDouble(LTNConstants.CURRENT_HOLD_AMOUNT, currentAccount.current_hold_amount);
                b.putInt(LTNConstants.LEAST_REVENUE_AMOUNT, leastRevenueAmount);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.btn_in://转入
                User user = LTNApplication.getInstance().getCurrentUser();
                if (user == null) {
                    return;
                }
                if (user.getUserInfo() == null) {
                    return;
                }
                if(currentAccount==null){
                    return;
                }
                if (Utils.checkIsBindBank(CurrentAccountActivity.this,user.getUserInfo())) {    // 已经绑卡
                    if (currentAccount.current_remain_amount <= 0) { // 弹出提示
                        Toast.makeText(CurrentAccountActivity.this, getResources().getText(R.string.current_is_over), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    intent = new Intent(this, CurrentAccountInActivity.class);
                    Bundle b1 = new Bundle();
                    b1.putDouble(LTNConstants.CURRENT_REMAIN_AMOUNT, currentAccount.current_remain_amount);
                    b1.putInt(LTNConstants.LEAST_REVENUE_AMOUNT, leastRevenueAmount);
                    intent.putExtra(LTNConstants.AVAILABLE_AMOUNT, currentAccount.available_amount);
                    intent.putExtra(LTNConstants.ANNUAL_INCOME_PERDAY, Double.valueOf(per_million_income));
                    intent.putExtras(b1);
                    startActivityForResult(intent, 1001);
                }
                break;
            case R.id.ll_tzqx://进入累计收益页面
                intent = new Intent(this, CurrentAccountListDetailActivity.class);
                startActivity(intent);
                break;
        }
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
                                setData();
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
