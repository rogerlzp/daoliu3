package com.wash.daoliu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.eventtype.DepositEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by rogerlzp on 16/2/22.
 */
public class DepositSuccessActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DepositSuccessActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_success);

        initView();

    }

    public void initView() {
        findViewById(R.id.btn_deposit).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("充值成功");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_deposit:
                EventBus.getDefault().post(new DepositEvent());
                finish();
                break;
            case R.id.back_btn:
                finish();
                break;
        }

    }

}
