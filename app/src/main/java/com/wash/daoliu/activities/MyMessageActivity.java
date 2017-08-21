package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wash.daoliu.R;

/**
 * Created by zhengpingli on 2017/6/25.
 */

public class MyMessageActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymessage);
        initView();
    }

    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("我的消息");
        findViewById(R.id.back_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent nIntent;
        switch (v.getId()) {
            case R.id.back_btn:
                //  获取验证码;
                finish();
                break;
        }
    }
}
