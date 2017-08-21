package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.utility.LTNConstants;

/**
 * Created by rogerlzp on 16/1/25.
 */
public class CurrentAccountBuySuccessActivity extends BaseActivity implements View.OnClickListener {
    String account = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_account_buy_success);

        account = getIntent().getExtras().getString(LTNConstants.AVAILABLE_AMOUNT);
        initView();

    }

    private void initView() {

        String day1 = com.wash.daoliu.utility.Utils.addOneDay(0);
        String day2 = com.wash.daoliu.utility.Utils.addOneDay(1);

        ((TextView) findViewById(R.id.tv_buy_success)).setText(String.format(getResources().getText(R.string.current_buy_success).toString(), account));

        if (day1 != null) {
            ((TextView) findViewById(R.id.tv_calculate_revenue)).setText(String.format(getResources().getText(R.string.calculate_revenue).toString(), day1));
        }
        if (day2 != null) {
            ((TextView) findViewById(R.id.tv_get_revenue)).setText(String.format(getResources().getText(R.string.get_revenue).toString(), day2));
        }
        ((TextView) findViewById(R.id.title)).setText("转入成功");

        findViewById(R.id.submit).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
            case R.id.back_btn:
                Intent intent = new Intent(this,CurrentAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this,CurrentAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
