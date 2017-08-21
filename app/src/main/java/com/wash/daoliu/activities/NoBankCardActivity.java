package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wash.daoliu.R;
import com.wash.daoliu.model.User;
import com.wash.daoliu.utility.Utils;

/**
 * Created by rogerlzp on 16/1/3.
 */
public class NoBankCardActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = NoBankCardActivity.class.getSimpleName();

    Button btnBind;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_bankcard);

        initView();
    }


    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("银行卡设置");
        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.btn_bindcard).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_bindcard:
                User.UserInfo userInfo = User.getUserInstance().getUserInfo();
                if (userInfo == null) {
                    Toast.makeText(this, R.string.login_first, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userInfo.getUserName())) {
//                    ViewUtils.showWarnDialog2(this, getString(R.string.realname_text), getString(R.string.user_auth_now),getString(R.string.user_auth_cancel),
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(NoBankCardActivity.this, UserAuthActivity.class);
////                            intent.putExtra(Constant.USER_AUTH_FROM,Constant.FROM_BIND_BANK_CARD);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            },null);
                    Utils.showRealNameWarn(this);
                } else {
                    // 开通账号页面
                    Intent mIntent = new Intent(NoBankCardActivity.this, BindBankCardActivity.class);
                    startActivity(mIntent);
                    finish();
                }

                break;
            case R.id.back_btn:
                //super.onBackPressed();
                finish();
                break;
            default:
                break;
        }
    }

}
