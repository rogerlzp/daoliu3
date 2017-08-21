package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tendcloud.tenddata.TCAgent;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.fragment.BindCardDialogFragment;
import com.wash.daoliu.fragment.ChooseBankDialogFragment;
import com.wash.daoliu.model.BankItem;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 16/1/10.
 */
public class BindedBankCardActivity extends BaseActivity implements View.OnClickListener, ChooseBankDialogFragment.OnChooseBank {
    private static final String TAG = BindBankCardActivity.class.getSimpleName();


    TextView etCardId;

    TextView tvBankName;
    TextView tvPersonName;
    TextView tvDepositNopwdValue;
    Button btnBind;
    private TextView operateDepositNoPwdBtn = null;
    String mBankName;
    BindCardDialogFragment dialog;
    // 能看到这个页面,必须是已经使命后的了
    //


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bindedbankcard);
        initView();
        // TODO: 添加银行下拉框


    }

    public void initView() {

        ((TextView) findViewById(R.id.title)).setText(getResources().getText(R.string.bank_info));
        tvBankName = (TextView) findViewById(R.id.tv_bank_value);
        etCardId = (TextView) findViewById(R.id.et_bankcard);
        operateDepositNoPwdBtn = (TextView) findViewById(R.id.opreate_deposit_nopwd);
        //  btnBind = (Button) findViewById(R.id.btn_bind);
        // btnBind.setOnClickListener(this);
        tvPersonName = ((TextView) this.findViewById(R.id.tv_person_value));
        tvDepositNopwdValue = (TextView) this.findViewById(R.id.tv_deposit_nopwd_value);
        //   etCardId.setBankCardTypeOn(); // bankcard 形式
        //   etCardId.setMaxLength(23);    // 银行卡最大长度为19


        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.tv_change_card).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.tv_change_card:
                Intent intent = new Intent(BindedBankCardActivity.this, BankCardChangeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Talking data
        TCAgent.onResume(this);

        // reset data
        User user = LTNApplication.getInstance().getCurrentUser();

        // 已经绑卡
        if (user.getUserInfo().getBankAuthStatus() != null
                && user.getUserInfo().getBankAuthStatus().equals("1")) {
            etCardId.setText(user.getUserInfo().getBankNo());
            tvBankName.setText(user.getUserInfo().getBelongBank());
            etCardId.setClickable(false);
            etCardId.setFocusable(false);
        } else {
            tvBankName.setOnClickListener(this);
        }
        // 如果有用户名,则显示用户名

        if (user.getUserInfo().getUserName() != null) {
            tvPersonName.setText(user.getUserInfo().getUserName());
            tvPersonName.setClickable(false);
            tvPersonName.setFocusable(false);
        }
        initDepositNopwd();
    }
    private void initDepositNopwd(){
        User user = LTNApplication.getInstance().getCurrentUser();
        if (user.getUserInfo().getAgreementCZ().equals("0")) {
            tvDepositNopwdValue.setText(R.string.unopen);
            operateDepositNoPwdBtn.setText(R.string.immediately_opened);
            operateDepositNoPwdBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewUtils.showWarnDialogWithBold(BindedBankCardActivity.this, getString(R.string.hint), getString(R.string.open_deposit_pwd_hint), getString(R.string.immediately_opened_2), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkMianmi();
                        }
                    });
                }
            });
        } else {
            tvDepositNopwdValue.setText(R.string.opened);
            operateDepositNoPwdBtn.setText(R.string.relieve);
            operateDepositNoPwdBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    checkMianmi();
                    ViewUtils.showWarnDialogWithBold2(BindedBankCardActivity.this, getString(R.string.hint), getString(R.string.relieve_deposit_pwd_hint), getString(R.string.relieve_2), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkMianmi1();
                        }
                    });
                }
            });
        }
    }
    public void checkMianmi() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.AGREEMENT_TYPE, LTNConstants.AGREEMENT_TYPE_CZ);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.MIANMI_AGREEMENT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                String url = (String) dataObj.getString(LTNConstants.RETURN_URL);
                                Intent intent = new Intent(BindedBankCardActivity.this, LTNWebPageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(LTNWebPageActivity.BUNDLE_URL, url);
                                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "联动优势");
                                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_MIANMI);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, LTNConstants.FROM_WEBVIEW_INTERFACE);
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                if (!TextUtils.isEmpty(resultMsg))
                                    Toast.makeText(BindedBankCardActivity.this, resultMsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }
                    }
                }

        );
    }
    public void checkMianmi1() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.AGREEMENT_TYPE, LTNConstants.AGREEMENT_TYPE_CZ);
        mReqParams.add(LTNConstants.UNBIND,"1");
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.MIANMI_AGREEMENT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                String url = (String) dataObj.getString(LTNConstants.RETURN_URL);
                                Intent intent = new Intent(BindedBankCardActivity.this, LTNWebPageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(LTNWebPageActivity.BUNDLE_URL, url);
                                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "联动优势");
                                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_MIANMI);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, LTNConstants.FROM_WEBVIEW_INTERFACE);
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                if (!TextUtils.isEmpty(resultMsg))
                                    Toast.makeText(BindedBankCardActivity.this, resultMsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }
                    }
                }

        );
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LTNConstants.GRANT_RESULT_SUCCESS: // 签完免密协议,跳转到预览界面
                // 更新一下userinfo
                User.getUserInstance().getUserInfo().setAgreementCZ(data.getStringExtra(LTNConstants.AGREEMENT_CZ));
                initDepositNopwd();
                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        // Talking data
        TCAgent.onPause(this);
    }

    @Override
    public void chooseBank(BankItem bankItem) {
        mBankName = bankItem.bankName;
        tvBankName.setText(mBankName);

    }
}
