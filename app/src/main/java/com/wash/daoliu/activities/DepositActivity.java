package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.eventtype.DepositEvent;
import com.wash.daoliu.fragment.DepositDialogFragment;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.UISwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/22.
 */
public class DepositActivity extends BaseActivity implements View.OnClickListener, DepositDialogFragment.DepositOKListener {

    private static final String TAG = DepositActivity.class.getSimpleName();

    EditText etOrderAmount;
    Button btnRecharge;
    TextView tvHintValue;
    String changeAmount;
    String deposit_note;

    public static final String DEPOSIT_NOTE = "deposit_note";

    int dailyLimit = 10;
    int onceLimit = 5;

    boolean isFromCurrent = false;

    String agreementFlag = "0";
    private UISwitchButton mUISwitchButton = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit);
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                isFromCurrent = getIntent().getExtras().getBoolean(LTNConstants.CURRENT_DEPOSIT);
            }
        }
        initView();
        getBankLimit();

        EventBus.getDefault().register(this);

    }

    @Subscribe
    public void onEvent(DepositEvent event) {
        finish();
    }

    public void initView() {
        View view = findViewById(R.id.title_layout);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();


        mUISwitchButton = (UISwitchButton) findViewById(R.id.toggleButton);
        mUISwitchButton.setChecked(false);
        mUISwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        agreementFlag = User.getUserInstance().getUserInfo().getAgreementCZ();
        if (!TextUtils.isEmpty(agreementFlag) && agreementFlag.equals("1")) {
            findViewById(R.id.rl_mianmi).setVisibility(View.INVISIBLE);
        }

        etOrderAmount = (EditText) findViewById(R.id.tv_deposit_value);
        tvHintValue = (TextView) findViewById(R.id.tv_hint_value);
        btnRecharge = (Button) findViewById(R.id.btn_deposit);
        btnRecharge.setOnClickListener(baseOnClickListener);
        btnRecharge.setEnabled(false);
        this.findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("充值");

        ((TextView) findViewById(R.id.tv_cardId_value)).setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getBankNo());
        ((TextView) findViewById(R.id.tv_bank_value)).setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getBelongBank());

        etOrderAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnRecharge.setEnabled(s.length() > 0);
                if (s.length() > 0) {
                    ((TextView) findViewById(R.id.tv_deposit_get_value)).setText(s.toString().trim());
                } else {
                    ((TextView) findViewById(R.id.tv_deposit_get_value)).setText("0元");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LTNConstants.DEPOSIT_RESULT_SUCCESS: // 从充值成功后跳转过来
                // 修改页面

                DepositDialogFragment dialog = new DepositDialogFragment();
                Bundle bundle = new Bundle();
                deposit_note = "您的资金托管账户到账" + changeAmount + "元，请注意查看余额变化。";
                bundle.putString(DEPOSIT_NOTE, deposit_note);
                dialog.setArguments(bundle);
                dialog.show(DepositActivity.this.getFragmentManager(), "");
                break;

            case LTNConstants.GRANT_RESULT_SUCCESS: // 签完免密协议,跳转到预览界面
                // 更新一下userinfo
                LTNApplication.getInstance().getCurrentUser().getUserInfo().setAgreementCZ("1");
                findViewById(R.id.rl_mianmi).setVisibility(View.GONE);
                Intent mIntent = new Intent(DepositActivity.this, DepositConfirmActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString(LTNConstants.ORDER_AMOUNT, changeAmount);
                mIntent.putExtras(bundle2);
                startActivity(mIntent);
                break;

            case LTNConstants.DEPOSIT_MIANMI_SUCCESS: // 签完免密协议,跳转到预览界面
                // 更新一下userinfo
                finish();
                break;

        }

    }

    //充值成功回调
    @Override
    public void onDepositOK() {
        if (isFromCurrent) {
            setResult(LTNConstants.CURRENT_DEPOSIT_VALUE);
        }
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     *
     */
    public void recharge() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.ORDER_AMOUNT, changeAmount);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        showLoadingProgressDialog(DepositActivity.this, "充值中...");
        btnRecharge.setEnabled(false);
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.DEPOSIT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        dismissProgressDialog();
                        btnRecharge.setEnabled(true);
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                // 判断是否要跳转
//                                String jumpAfter = (String) dataObj.getString(LTNConstants.JUMP_AFTER_DEPOSIT);
//                                if(!TextUtils.isEmpty(jumpAfter) && jumpAfter.equals("1")) {
//
//                                }
                                String url = (String) dataObj.getString(LTNConstants.RETURN_URL);
                                Intent intent = new Intent(DepositActivity.this, LTNWebPageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(LTNWebPageActivity.BUNDLE_URL, url);
                                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "联动优势");
                                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_DEPOSIT);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, LTNConstants.FROM_WEBVIEW_INTERFACE);
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                if (!TextUtils.isEmpty(resultMsg))
                                    Toast.makeText(DepositActivity.this, resultMsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        dismissProgressDialog();
                        btnRecharge.setEnabled(true);
                    }
                }

        );

    }


    public void checkMianmi() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.AGREEMENT_TYPE, LTNConstants.AGREEMENT_TYPE_CZ);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        showLoadingProgressDialog(DepositActivity.this, "跳转免密支付中...");
        btnRecharge.setEnabled(false);
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.MIANMI_AGREEMENT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            dismissProgressDialog();
                            btnRecharge.setEnabled(true);
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                String url = (String) dataObj.getString(LTNConstants.RETURN_URL);
                                Intent intent = new Intent(DepositActivity.this, LTNWebPageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(LTNWebPageActivity.BUNDLE_URL, url);
                                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "联动优势");
                                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_MIANMI);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, LTNConstants.FROM_WEBVIEW_INTERFACE);
                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                if (!TextUtils.isEmpty(resultMsg))
                                    Toast.makeText(DepositActivity.this, resultMsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        dismissProgressDialog();
                        btnRecharge.setEnabled(true);
                    }
                }

        );
    }

    public void getBankLimit() {

        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.BELONG_BANK, LTNApplication.getInstance().getCurrentUser().getUserInfo().getBelongBank());

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.BANK_LIMIT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            JSONObject dataObj = (JSONObject) jsonObject.optJSONObject(LTNConstants.DATA);
                            dailyLimit = dataObj.optInt(LTNConstants.DAILY_LIMIT);
                            onceLimit = dataObj.optInt(LTNConstants.ONCE_LIMIT);
                            tvHintValue.setText("该银行银行卡单次充值限额" + onceLimit + "万元，单日充值限额" + dailyLimit + "万元。");
                            tvHintValue.setVisibility(View.VISIBLE);
                            // 设置长度
                            etOrderAmount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(String.valueOf(onceLimit * 10000).length())});
                        } catch (Exception e) {
                            e.printStackTrace();
                            tvHintValue.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override

    public void clickEffective(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_deposit:
                // 去充值
                //判断去走免密支付还是支付
                // 已经免密支付
                // 保存充值额度,等待签约免密协议成功后跳转
                changeAmount = etOrderAmount.getText().toString().trim();
                if (TextUtils.isEmpty(changeAmount)) {
                    Toast.makeText(this, "充值金额不能为0", Toast.LENGTH_LONG).show();
                    return;
                } else if (Integer.parseInt(changeAmount) > onceLimit * 10000) {
                    Toast.makeText(this, "单次充值不能超过上限", Toast.LENGTH_LONG).show();
                    return;
                }
                if (User.getUserInstance().getUserInfo().getAgreementCZ().equals("1")) {
                    Intent mInent = new Intent(DepositActivity.this, DepositConfirmActivity.class);
                    Bundle b = new Bundle();
                    b.putString(LTNConstants.ORDER_AMOUNT, changeAmount);
                    mInent.putExtras(b);
                    startActivityForResult(mInent, LTNConstants.FROM_DEPOSIT);
                } else if (mUISwitchButton.isChecked()) {  // 签约
                    checkMianmi();
                } else {      // 充值
                    recharge();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                //  获取验证码;
                finish();
                break;
        }
    }

}
