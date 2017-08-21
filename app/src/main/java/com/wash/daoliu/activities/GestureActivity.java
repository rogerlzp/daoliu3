package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wash.daoliu.R;

/**
 * Created by rogerlzp on 15/12/28.
 */
public class GestureActivity extends BaseActivity implements View.OnClickListener {
    private Button mBtnSetLock;
    private Button mBtnVerifyLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture);
        setUpView();
        setUpListener();
    }

    private void setUpView() {
        mBtnSetLock = (Button) findViewById(R.id.btn_set_lockpattern);
        mBtnVerifyLock = (Button) findViewById(R.id.btn_verify_lockpattern);
    }

    private void setUpListener() {
        mBtnSetLock.setOnClickListener(this);
        mBtnVerifyLock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_lockpattern:
                startSetLockPattern();
                break;
            case R.id.btn_verify_lockpattern:
                startVerifyLockPattern();
                break;
            default:
                break;
        }
    }

    private void startSetLockPattern() {
        Intent intent = new Intent(GestureActivity.this, GestureEditActivity.class);
        startActivity(intent);
    }

    private void startVerifyLockPattern() {
        Intent intent = new Intent(GestureActivity.this, GestureVerifyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
