package com.wash.daoliu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wash.daoliu.R;

/**
 * Created by rogerlzp on 16/2/24.
 */
public class BindCardChangeNoticeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = BindCardChangeNoticeActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_change_notice);
        initView();
    }

    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("换卡提醒");
        findViewById(R.id.back_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
