package com.wash.daoliu.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.eventtype.ShopServiceReserveEvent;
import com.wash.daoliu.model.ShopService;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;
import com.wash.daoliu.utility.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zhengpingli on 2017/4/3.
 */

public class ReserveActivity extends BaseActivity implements View.OnClickListener {

    private TimePickerView pvTime, pvCustomTime;
    TextView title;

    TextView tv_service_time; // 服务时长
    TextView tv_service_date; // 服务时间
    TextView tv_shop, tv_service;


    String shopId, shopName, serviceId, serviceName;

    ShopService shopService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        initView();

        String shopServiceJson = getIntent().getStringExtra(LTNConstants.SHOP_SERVICE);
        shopService = new Gson().fromJson(shopServiceJson, ShopService.class);

        initData();
    }

    public void initData() {
        if (shopService != null) {
            tv_shop.setText(shopService.getShopName());
            tv_service.setText(shopService.getServiceName());

            title = (TextView) findViewById(R.id.title);
            tv_service_date = (TextView) findViewById(R.id.tv_service_date);
            tv_shop = (TextView) findViewById(R.id.tv_shop);
            tv_service = (TextView) findViewById(R.id.tv_service);
            tv_service_time = (TextView) findViewById(R.id.tv_service_time);
            tv_service_date.setOnClickListener(this);
            initPicker();
        }
    }


    public void initView() {
        title = (TextView) findViewById(R.id.title);
        tv_service_date = (TextView) findViewById(R.id.tv_service_date);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        tv_service = (TextView) findViewById(R.id.tv_service);
        tv_service_time = (TextView) findViewById(R.id.tv_service_time);
        tv_service_date.setOnClickListener(this);
        findViewById(R.id.btn_reserve).setOnClickListener(this);
        initPicker();
    }

    public void initPicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);

        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv_service_date.setText(TextUtils.getTime(date));
            }
        })
              //  .setType(TimePickerView.class.MONTH_DAY_HOUR_MIN)
                .setCancelText("Cancel")//取消按钮文字
                .setSubmitText("Sure")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("Title")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isDialog(true)//是否显示为对话框样式
                .build();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_service_date:
                pvTime.show();
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_reserve:
                reserveService();
                break;
        }
    }

    public void reserveService() {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.put(LTNConstants.SHOP_ID, "" + shopService.getShopId());

        mReqParams.put(LTNConstants.TOTAL_AMOUNT, "" + shopService.getOriginalPrice());
        mReqParams.put(LTNConstants.RESERVE_BEGINTIME, "" + tv_service_date.getText());

        //检查
        mReqParams.put(LTNConstants.RESERVED_PRODUCT_LIST + "[0]." + "serviceId",
                String.valueOf(shopService.getServiceId()));


        showLoadingProgressDialog(ReserveActivity.this, "提交中...");
        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.RESERVE_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        dismissProgressDialog();
                        // 跳转到成功列表，提示后续操作
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {

                                Toast.makeText(ReserveActivity.this, "成功", Toast.LENGTH_SHORT);

                                Intent intent = new Intent(ReserveActivity.this, ReserveSuccessActivity.class);
                                //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                EventBus.getDefault().post(new ShopServiceReserveEvent());
                                startActivity(intent);

                                //结束该页面
                                finish();

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                LogUtils.d(resultMsg);
                            }
                        } catch (JSONException je) {
                            LogUtils.d(je.getMessage());
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        dismissProgressDialog();
                        LogUtils.d(errorMsg);
                    }


                });
    }
}
