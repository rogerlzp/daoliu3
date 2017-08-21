package com.wash.daoliu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;

/**
 * Created by rogerlzp on 15/12/28.
 */
public class CurrentAccountOutSuccessActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = CurrentAccountOutSuccessActivity.class.getSimpleName();
    private TextView success_info = null;
    Button btnNext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_account_out_success);
        initView();

    }

    public void initView() {
        success_info = (TextView) findViewById(R.id.success_info);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("转出成功");
        try {
            success_info.setText(String.format("%s转出金额%s元您可至“我的账户”查看您的余额", Utils.addOneDay(), getIntent().getStringExtra(LTNConstants.OUT_MONEY)));
        } catch (Exception e) {
            e.printStackTrace();
            success_info.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.back_btn:
            case R.id.btn_next:
                finish();
                break;


        }
    }


}