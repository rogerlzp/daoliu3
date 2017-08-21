package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LoginUtils;

/**
 * Created by zhengpingli on 2017/4/5.
 */

public class ReserveSuccessActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_success);


        initView();

    }

    public void initView() {
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText(getResources().getString(R.string.reserve_success));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
            case R.id.btn_confirm:

                //

                // 跳转到我的账户
                Bundle b = getIntent().getExtras();
                if (b == null) {
                    b = new Bundle();
                    Intent intent = new Intent(ReserveSuccessActivity.this, MainActivity.class);

                    b.putBoolean(LTNConstants.IS_FROM_RESERVE, true);
                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    //从其他页面条转过来
                    b.putBoolean(LTNConstants.IS_RESERVE, true);
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.FROM_RESERVE_SUCCESS);
                    LoginUtils.loginJump(ReserveSuccessActivity.this, b);
                    setResult(RESULT_OK);
                    finish();
                }
                break;

        }

    }

}

