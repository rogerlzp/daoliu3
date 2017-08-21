package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.Car;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zhengpingli on 2017/4/2.
 */

public class CarActivity extends BaseActivity implements View.OnClickListener {


    RelativeLayout rl_emptycar;
    LinearLayout ll_car;
    Car car;
    Button btn_add_car;
    TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        initView();
    }

    public void initView() {

        rl_emptycar = (RelativeLayout) findViewById(R.id.rl_emptycar);
        ll_car = (LinearLayout) findViewById(R.id.ll_car);
        btn_add_car = (Button) findViewById(R.id.btn_add_car);
        btn_add_car.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText("我的爱车");

        initData();
    }

    public void initData() {
        if (user.carList.size() == 0) {
            rl_emptycar.setVisibility(View.VISIBLE);
            ll_car.setVisibility(View.GONE);
        } else {
            ll_car.setVisibility(View.VISIBLE);
            rl_emptycar.setVisibility(View.GONE);

            car = user.getCarList().get(0);
            if (car != null) {

            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_add_car:
                Intent intent = new Intent(CarActivity.this, CarAddActivity.class);
                startActivityForResult(intent, LTNConstants.ADD_CAR);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LTNConstants.ADD_CAR_SUCCESS: // 从添加车辆信息后跳转过来，需要更新用户信息
                //更新 用户名信息, 后面可以直接查到
                updateCarInfo();
        }

    }


    public void updateCarInfo() {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        showLoadingProgressDialog(CarActivity.this, "正在更新中...");
        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.CAR_LIST_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        dismissProgressDialog();
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                // 将用户信息存储到
                                Gson gson = new Gson();
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                JSONObject carObj = resultObj.getJSONObject(LTNConstants.CAR);
                                Car car = gson.fromJson(carObj.toString(), Car.class);
                                if (car != null) {
                                    LTNApplication.getInstance().getCurrentUser().getCarList().add(car);

                                    initData();
                                }


                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);

                            }
                        } catch (JSONException je) {
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                        dismissProgressDialog();

                    }
                });

    }
}