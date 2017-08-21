package com.wash.daoliu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.MyEditText2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zhengpingli on 2017/4/2.
 */

/**
 * TODO: 快速实现添加车辆，后续需要优化
 */

public class CarAddActivity extends BaseActivity implements MyEditText2.OnEditTextChangedListener, View.OnClickListener {


    MyEditText2 et_plate;
    MyEditText2 et_brandtype;
    MyEditText2 et_color;
    Button btn_save;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        initView();
    }

    public void initView() {

        ((TextView) findViewById(R.id.title)).setText("增加车辆");

        et_plate = (MyEditText2) findViewById(R.id.et_plate);
        et_brandtype = (MyEditText2) findViewById(R.id.et_brandtype);
        et_color = (MyEditText2) findViewById(R.id.et_color);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        findViewById(R.id.back_btn).setOnClickListener(this);
        et_plate.setOnEditTextChanged(this);
        et_brandtype.setOnEditTextChanged(this);
        et_color.setOnEditTextChanged(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_save:
                addCar();
                break;
        }
    }

    @Override
    public void onEditTextChanged(boolean flag) {
        btn_save.setEnabled(flag && (et_plate.getText().toString().trim() != null)
                && (et_brandtype.getText().toString().trim() != null)
                && (et_color.getText().toString().trim() != null));
    }

    public void addCar() {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.put(LTNConstants.CAR_PLATE, et_plate.getText().toString());
        mReqParams.put(LTNConstants.CAR_BARND, et_brandtype.getText().toString());
        mReqParams.put(LTNConstants.CAR_TYPE, "small");
        mReqParams.put(LTNConstants.CAR_BARNDTYPE, et_brandtype.getText().toString());
        mReqParams.put(LTNConstants.CAR_COLOR, et_color.getText().toString());

        showLoadingProgressDialog(CarAddActivity.this, "正在保存中...");
        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.CAR_ADD_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        dismissProgressDialog();
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {

                                Toast.makeText(CarAddActivity.this, "添加成功", Toast.LENGTH_SHORT);
                                setResult(LTNConstants.ADD_CAR_SUCCESS);
                                finish();

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);

                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Toast.makeText(CarAddActivity.this, R.string.ERROR_CODE_1, Toast.LENGTH_SHORT);
                        dismissProgressDialog();
                    }

                });


    }
}
