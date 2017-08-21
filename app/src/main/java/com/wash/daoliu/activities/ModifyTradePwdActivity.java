package com.wash.daoliu.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wash.daoliu.R;

/**
 * Created by rogerlzp on 16/1/7.
 */
public class ModifyTradePwdActivity extends BaseActivity implements View.OnClickListener {

    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    Intent sentIntent = new Intent(SENT_SMS_ACTION);

    EditText etPwdOrigin, etPwdNew;
    String pwdOrigin;
    String pwdNew;

    Button btn_modify_trade_pwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_trade_pwd);
        initView();
    }


    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("修改支付密码");
        ((Button) findViewById(R.id.btn_modify_trade_pwd)).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);

        etPwdOrigin = (EditText) findViewById(R.id.et_original_pwd);
        etPwdNew = (EditText) findViewById(R.id.et_new_pwd);

        btn_modify_trade_pwd = (Button) findViewById(R.id.btn_modify_trade_pwd);

        etPwdOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_modify_trade_pwd.setEnabled(s.length() > 0 && etPwdNew.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPwdNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_modify_trade_pwd.setEnabled(s.length() > 0 && etPwdOrigin.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void send(String message) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", message);
        sendIntent.setType("vnd.android-dir/mms-sms");
    }

    public void doSendSMSTo(String phoneNumber, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify_trade_pwd:
                pwdOrigin = etPwdOrigin.getText().toString().trim();
                pwdNew = etPwdNew.getText().toString().trim();
                if (pwdOrigin.length() != 6) {
                    Toast.makeText(this, R.string.get_correct_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwdNew.length() != 6) {
                    Toast.makeText(this, R.string.get_correct_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                doSendSMSTo("10690569687", "GGMM#" + pwdOrigin + "#" + pwdNew);
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

}
