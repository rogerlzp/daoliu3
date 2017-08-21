package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.utility.ActivityUtils;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;

/**
 * Created by jiajia on 2016/1/22.
 */
public class SetHostActivity extends BaseActivity{
    private Spinner spinner = null;
     public String[] strs = new String[]{"https://www.lingtouniao.com","http://192.168.18.112:8080","http://120.55.184.234/v1"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_host);
        ((TextView)findViewById(R.id.title)).setText("HOST设置，当前是:\n" + LTNConstants.ACCESS_URL.HOST);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strs));
    }
    public void setHost(View view){
        String host = strs[spinner.getSelectedItemPosition()];
//        LTNConstants.ACCESS_URL.HOST = host;
        LTNConstants.ACCESS_URL.resetUrl(host);

        LogUtils.e(LTNConstants.ACCESS_URL.LOGIN_CODE_URL);
                ((TextView) findViewById(R.id.title)).setText("HOST设置，当前是:\n" + LTNConstants.ACCESS_URL.HOST);
        LTNApplication.getInstance().clearUser();
        ActivityUtils.finishAll();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
