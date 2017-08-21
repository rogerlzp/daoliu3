package com.wash.daoliu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.fragment.LettersKeyboardDialog;
import com.wash.daoliu.fragment.ProvinceKeyboardDialog;

/**
 * Created by zhengpingli on 2017/3/30.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener, ProvinceKeyboardDialog.OnChooseProvice, LettersKeyboardDialog.OnChooseLetter {

    private TextView test;
    private TextView number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        test = (TextView) findViewById(R.id.test);
        number = (TextView) findViewById(R.id.number);
        test.setOnClickListener(this);
        number.setOnClickListener(this);
//        String actionUrl = "http://121.42.145.216:8080/reserve/add";
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("clientType", "A");
//        hashMap.put("sessionKey", "00e8e8e1f4814b0bc3472c249f75eb0f");
//        hashMap.put("shopId", "100001");
//        hashMap.put("totalAmount", "100");
//
//
//        String reserveProduct =
//
//                "[{\"serviceId\":\"10000\",\"serviceName\":\"afafa\", \"serviceAmount\":\"230\"}," +
//                        "{\"serviceId\":\"10001\",\"serviceName\":\"afa\", \"serviceAmount\":\"30\"}]";
//
//        hashMap.put("reserveProductList", reserveProduct);
//
//
//        Log.d("TestActivity", reserveProduct);
//        WCOKHttpClient.getOkHttpClient(this).requestAsyn(actionUrl, WCOKHttpClient.TYPE_POST_JSON, hashMap,
//                new ReqCallBack<JSONObject>() {
//
//                    @Override
//                    public void onReqSuccess(JSONObject jsonObject) {
//                        Log.d("TestActivity", jsonObject.toString());
//
//
//                    }
//
//                    @Override
//                    public void onReqFailed(String errorMsg) {
//                        Log.d("TestActivity", errorMsg);
//                    }
//
//
//                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test:
                ProvinceKeyboardDialog provinceKeyboardDialog = new ProvinceKeyboardDialog();
                provinceKeyboardDialog.show(getSupportFragmentManager(), "ProvinceKeyboardDialog");
                break;
            case R.id.number:
                LettersKeyboardDialog lettersKeyboardDialog = new LettersKeyboardDialog();
                lettersKeyboardDialog.show(getSupportFragmentManager(), "LettersKeyboardDialog");
                break;
        }
    }

    @Override
    public void onChooseLetter(String provice) {
        if (number.length() < 6)
            number.append(provice);
    }

    @Override
    public void onDeleteLetter() {
        String str = number.getText().toString();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
            number.setText(str);
        }
    }

    @Override
    public void onChooseProvice(String provice) {
        test.setText(provice);
    }
}
