package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.fragment.WithdrawPostDialogFragment;
import com.wash.daoliu.fragment.WithdrawPreDialogFragment;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/24.
 */
public class WithDrawActivity extends BaseActivity implements View.OnClickListener, WithdrawPreDialogFragment.BackListener, WithdrawPostDialogFragment.PostWithdrawListener {

    private static final String TAG = WithDrawActivity.class.getSimpleName();

    EditText etWithdrawAmount;
    Button btnWithdraw;
    TextView tvWithdrawNote, tvWithdrawGetValue, tvHandlingChargeValue, tvBirdcoinValue;
    ImageView ivReturn;

    public static final String PRE_WITHDRAW_NOTE = "pre_withdraw_note";
    public static final String POST_WITHDRAW_NOTE = "post_withdraw_note";

    private int freeCounter = 0;  // 每月免提机会
    private String withdraw_note; // 提现说明
    private double withdrawGetValue = 0.0; // 实际到账
    private double handling_charge_value = 0.0; // 手续费

    private double use_birdcoin_value = 0.0; // 用鸟币
    private boolean canWithdraw = false;

    String bankNo = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw);

        Bundle bundle = getIntent().getBundleExtra(LTNConstants.WITHDRAW_HINT_VALUE);
        freeCounter = Integer.parseInt(LTNApplication.getInstance().getCurrentUser().getAccount().getFreeCounter());
        // update 免费次数
        initView();

        initData();
    }

    public void initView() {
        View view = findViewById(R.id.title_layout);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        ((TextView) findViewById(R.id.title)).setText("提现");
        tvBirdcoinValue = (TextView) findViewById(R.id.tv_birdcoin_value);

        tvHandlingChargeValue = (TextView) findViewById(R.id.tv_handling_charge_value);
        tvWithdrawGetValue = (TextView) findViewById(R.id.tv_withdraw_get_value);
        etWithdrawAmount = (EditText) findViewById(R.id.tv_withdraw_value);
        btnWithdraw = (Button) findViewById(R.id.btn_withdraw);
        btnWithdraw.setOnClickListener(baseOnClickListener);

        ivReturn = (ImageView) findViewById(R.id.back_btn);
        ivReturn.setOnClickListener(this);

        tvWithdrawNote = (TextView) findViewById(R.id.tv_withdraw_hint_note);

        // 更新
        if (freeCounter >= 0) {
       //     tvWithdrawNote.setText(String.format(getResources().getString(R.string.with_draw_note), freeCounter));
        }

        // 可用余额
        ((TextView) findViewById(R.id.tv_usable_balance_value)).setText(String.valueOf(LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance()) + "元");

        //银行 + 卡号后四位
        bankNo = LTNApplication.getInstance().getCurrentUser().getUserInfo().getBankNo();
        ((TextView) findViewById(R.id.tv_bank_value)).setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getBelongBank()
                + "   尾号 " + bankNo.substring(bankNo.length() - 4, bankNo.length()));

        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnWithdraw.setEnabled(s.length() > 0);
                if (s.length() > 0) {
                    if (handling_charge_value == 2) {
                        if (LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance() - Double.parseDouble(s.toString().trim()) >= 2) {
                            withdrawGetValue = Double.parseDouble(s.toString().trim());
                            // 从余额中扣除,实际到账金额不变
                        } else {
                            // 需要从实际到账金额中扣除手续费
                            withdrawGetValue = Double.parseDouble(s.toString().trim()) -
                                    (2 - (LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance() - Double.parseDouble(s.toString().trim())));

                        }
                    } else {
                        // 免提机会
                        // 鸟币> 2
                        withdrawGetValue = Double.parseDouble(s.toString().trim());
                    }
                    tvWithdrawGetValue.setText(withdrawGetValue + "元");
                } else {
                    tvWithdrawGetValue.setText("0元");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        etWithdrawAmount.addTextChangedListener(mTextWatcher);
        // 设置长度
        // etWithdrawAmount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
        //        String.valueOf(LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance()).length())});


    }

    /**
     * 判断规则
     * 1. 有免费次数, 鸟币> 2 时候,可以提现
     * 2. 没有免费次数,鸟币 >= 2 时候,可以体现
     */
    public void initData() {
       // tvWithdrawNote.setText(String.format(getResources().getString(R.string.with_draw_note), freeCounter));

        //    tvWithdrawNote.setText("提现说明：您当月还有" + freeCounter + "次免费提现机会，默认在没有免费提现机会时用鸟币抵扣，没有鸟币，提现扣2元人民币/次。手续费优先从账户剩余的可用余额中扣除，若不够，则从到账金额中扣除，可能会使到账金额小于提现金额。");

        // 判断免费次数
        if (LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance() <= 0) {
            tvHandlingChargeValue.setText("0.00元");
            tvBirdcoinValue.setText("");
            btnWithdraw.setEnabled(false);
            canWithdraw = false;
            return;
        }
        if (freeCounter > 0) { // 可以免费,手续费为0
            tvHandlingChargeValue.setText("0.00元");
            tvBirdcoinValue.setText("");
            handling_charge_value = 0;
            use_birdcoin_value = 0;
            canWithdraw = true;

        } else if (LTNApplication.getInstance().getCurrentUser().getAccount().getBirdCoin() >= 2) { // 可以用鸟币抵扣
            tvBirdcoinValue.setText("用2鸟币抵扣手续费");
            tvHandlingChargeValue.setText("0.00元");
            handling_charge_value = 0;
            use_birdcoin_value = 2;
            canWithdraw = true;
        } else if (LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance() > 2) { // 不能免费,不能用鸟币抵扣,余额> 2 则
            tvBirdcoinValue.setText("");
            tvHandlingChargeValue.setText("2.00元");
            handling_charge_value = 2;
            use_birdcoin_value = 0;
            //如果是余额> 2, 则到账金额 在改变一下.

            canWithdraw = true;
        } else { //不能提现,建议下个月再来
            tvBirdcoinValue.setText("");
            tvHandlingChargeValue.setText("2.00");
            handling_charge_value = 0;
            btnWithdraw.setEnabled(false);
            canWithdraw = false;
        }

        // 设置手续费用,鸟币
        // tvWithdrawNote.setText(getResources().getString(R.string.with_draw_note).replaceAll("X", String.valueOf(freeCounter)));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LTNConstants.WITHDRAW_RESULT_SUCCESS: // 从充值成功后跳转过来
                // 更新到账页面
                //     tvWithdrawGetValue.setText("" + withdrawGetValue);

                // 更新余额
                ((TextView) findViewById(R.id.tv_usable_balance_value))
                        .setText(String.valueOf(LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance() - withdrawGetValue - handling_charge_value));

                WithdrawPostDialogFragment dialog = new WithdrawPostDialogFragment();
                Bundle bundle = new Bundle();
                withdraw_note =
                        "提现资金将会汇到您尾号为 " + bankNo.substring(bankNo.length() - 4, bankNo.length()) + " 的银行卡内，请注意查收。如有问题，请拨打客服热线:400-999-9980。";

                bundle.putString(POST_WITHDRAW_NOTE, withdraw_note);
                dialog.setArguments(bundle);
                dialog.show(WithDrawActivity.this.getFragmentManager(), "");
        }

    }

    /**
     *
     */
    public void withdraw() {

        String changeAmount = etWithdrawAmount.getText().toString().trim();
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.ORDER_AMOUNT, changeAmount);
        mReqParams.add(LTNConstants.BIRD_COIN, String.valueOf(use_birdcoin_value));
        mReqParams.add(LTNConstants.BUCKLE, LTNConstants.HANDLING_CHARGE_VALUE_SYSTEM);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.WITHDRAW_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            // Log.d(TAG, jsonObject.toString());
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                String url = (String) dataObj.getString(LTNConstants.RETURN_URL);
                                // 在新的webpage中打开一个url
                                Intent intent = new Intent(WithDrawActivity.this, LTNWebPageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(LTNWebPageActivity.BUNDLE_URL, url);
                                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "联动优势");
                                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_DEPOSIT);
                                intent.putExtras(bundle);
                                //   startActivity(intent);
                                // 登台跳转回来
                                startActivityForResult(intent, LTNConstants.FROM_WEBVIEW_INTERFACE);

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);

                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }
                    }
                });

    }

    @Override
    public void onBackListener() {
        withdraw();
    }

    @Override
    public void onPostListener() {
        // recharge();
        // 跳转到原来的页面,即提现页面.
        //
        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                //  获取验证码;
                super.onBackPressed();
                break;
        }
    }


    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_withdraw:
                // 弹出提示对话框
                if (LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance() <= 0) {
                    Toast.makeText(WithDrawActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etWithdrawAmount.getText().toString().trim() == null) {
                    Toast.makeText(WithDrawActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
                    return;
                }


                //    withdrawGetValue = Double.parseDouble(etWithdrawAmount.getText().toString().trim()) + use_birdcoin_value - handling_charge_value;

                //TODO: 输入的时候优化
                if (withdrawGetValue > LTNApplication.getInstance().getCurrentUser().getAccount().getUsableBalance()) {
                    Toast.makeText(WithDrawActivity.this, "提现金额不能大于当前余额", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!canWithdraw) {
                    Toast.makeText(WithDrawActivity.this, "余额不足抵扣手续费,请下月再试!", Toast.LENGTH_SHORT).show();
                    return;
                }


                WithdrawPreDialogFragment dialog = new WithdrawPreDialogFragment();
                Bundle bundle = new Bundle();

                if (freeCounter == 0) {
                    withdraw_note = "实际到账" + withdrawGetValue + "元，您当月还有" + freeCounter + "次免费提现机会，此次提现手续费" + handling_charge_value + "元，确认提现吗？";

                } else if (use_birdcoin_value == 2) {
                    withdraw_note = "实际到账" + withdrawGetValue + "元，您当月还有" + freeCounter + "次免费提现机会，此次提现扣除提现手续费" + use_birdcoin_value + "鸟币，确认提现吗？";

                } else {
                    withdraw_note = "实际到账" + withdrawGetValue + "元，您当月还有" + freeCounter + "次免费提现机会，此次提现扣除提现手续费" + handling_charge_value + "元，确认提现吗？";

                }
                bundle.putString(PRE_WITHDRAW_NOTE, withdraw_note);
                dialog.setArguments(bundle);
                dialog.show(WithDrawActivity.this.getFragmentManager(), "");

                break;
        }
    }


}
