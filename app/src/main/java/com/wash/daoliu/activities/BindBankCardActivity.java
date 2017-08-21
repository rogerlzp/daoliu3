package com.wash.daoliu.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.MyEditText2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/18.
 */
public class BindBankCardActivity extends BaseActivity implements MyEditText2.OnEditTextChangedListener, View.OnClickListener, BindCardDialogFragment.BindcardSuccessListener, ChooseBankDialogFragment.OnChooseBank {
    private static final String TAG = BindBankCardActivity.class.getSimpleName();


    MyEditText2 etCardId;

    TextView tvBankName;
    EditText tvPersonName;
    Button btnBind;
    StringBuffer sb = new StringBuffer();

    String mBankName = null;
    BindCardDialogFragment dialog;

    TextView tv_bindcard_note4;
    //


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bindbankcard);
        initView();
        getBankNames();

    }

    @Override
    public void onEditTextChanged(boolean flag) {
        btnBind.setEnabled(flag && (mBankName != null));
    }

    public void getUserInfo() {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.MY_USER_INFO_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                // 将用户信息存储到
                                Gson gson = new Gson();
                                JSONObject accountInfoObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                LTNApplication.getInstance().getCurrentUser().setUserInfo(gson.fromJson(accountInfoObj.toString(), User.UserInfo.class));
                                User user = LTNApplication.getInstance().getCurrentUser();
                                if (user != null) {
                                    if (user.getUserInfo().getUserName() != null) {
                                        tvPersonName.setText(user.getUserInfo().getUserName());
                                        tvPersonName.setEnabled(false);
                                    }
                                    // 已经绑卡
                                    if (user.getUserInfo().getBankAuthStatus() != null
                                            && user.getUserInfo().getBankAuthStatus().equals("1")) {
                                        etCardId.setText(user.getUserInfo().getBankNo());
                                        tvBankName.setText(user.getUserInfo().getBelongBank());
                                        etCardId.setClickable(false);
                                        etCardId.setFocusable(false);
                                    } else {
                                        tvBankName.setOnClickListener(BindBankCardActivity.this);
                                    }
                                    // 如果有用户名,则显示用户名

//                                if (LTNApplication.getInstance().getCurrentUser().getUserInfo().getUserName() != null) {
//                                    tvPersonName.setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getUserName());
//                                    tvPersonName.setEnabled(false);
//                                }

                                }

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);

                            }
                        } catch (JSONException je) {
                        }
                    }
                });

    }

    public void initView() {

        ((TextView) findViewById(R.id.title)).setText("银行卡设置");
        tvBankName = (TextView) findViewById(R.id.tv_bank_value);
        etCardId = (MyEditText2) findViewById(R.id.et_bankcard);
        btnBind = (Button) findViewById(R.id.btn_bind);
        btnBind.setOnClickListener(baseOnClickListener);
        tvPersonName = ((EditText) this.findViewById(R.id.tv_person_value));
        tvBankName.setFocusable(true);
        tvBankName.setFocusableInTouchMode(true);
        tvBankName.requestFocus();
        etCardId.setBankCardTypeOn(); // bankcard 形式
        etCardId.setMaxLength(23);    // 银行卡最大长度为19

        findViewById(R.id.back_btn).setOnClickListener(this);

        etCardId.setOnEditTextChanged(this);


        tv_bindcard_note4 = (TextView) findViewById(R.id.tv_bindcard_note4);

        SpannableString spannableString = new SpannableString(LTNConstants.PHONE_NUMBER);
        PhoneClickableSpan clickableSpan = new PhoneClickableSpan(this);
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_bindcard_note4.setText("如遇其他问题，可拨打服务热线\n");
        tv_bindcard_note4.append(spannableString);
        tv_bindcard_note4.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LTNConstants.BIND_CARD_RESULT_SUCCESS: // 从绑卡后跳转过来
                //更新 用户名信息, 后面可以直接查到
//                UserUtils.getAccountInfo();
//                UserUtils.getUserInfo();

                // 更新当前页面信息
//                etCardId.setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getBankNo());
//                tvBankName.setText(LTNApplication.getInstance().getCurrentUser().getUserInfo().getBelongBank());
//                etCardId.setClickable(false);
//                etCardId.setFocusable(false);

                dialog = new BindCardDialogFragment();
                dialog.show(BindBankCardActivity.this.getFragmentManager(), "");

        }

    }

    public void bindBankCard() {
        String mBankCard = etCardId.getText().toString().trim().replaceAll(" ", "");
        if (mBankCard.length() < 16) {
            Toast.makeText(BindBankCardActivity.this, "请填写银行卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO:选择银行名称

        if (TextUtils.isEmpty(mBankName)) {
            Toast.makeText(BindBankCardActivity.this, "请选择银行", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.ACCOUNT_CARDID, mBankCard);
        mReqParams.add(LTNConstants.BELONG_BANK, mBankName);

        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        showLoadingProgressDialog(BindBankCardActivity.this, "正在绑卡中...");
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.BIND_BANK_CARD_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        dismissProgressDialog();
                        try {
                            // Log.d(TAG, jsonObject.toString());
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                String url = (String) dataObj.getString(LTNConstants.RETURN_URL);
                                // 在新的webpage中打开一个url
                                Intent intent = new Intent(BindBankCardActivity.this, LTNWebPageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(LTNWebPageActivity.BUNDLE_URL, url);
                                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "联动优势");
                                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, LTNConstants.FROM_WEBVIEW_INTERFACE);

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);

                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(BindBankCardActivity.this, R.string.ERROR_CODE_1, Toast.LENGTH_SHORT);
                        dismissProgressDialog();
                    }
                });

    }


    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_bind:
                // 开通账号页面
                bindBankCard();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.tv_bank_value:
                Utils.hideKeyboard(v);
                ChooseBankDialogFragment fragment = new ChooseBankDialogFragment();
                fragment.show(getSupportFragmentManager(), "choosebank");
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

        if (!TextUtils.isEmpty(user.getUserInfo().getUserName())) {
            tvPersonName.setText(user.getUserInfo().getUserName());
            tvPersonName.setClickable(false);
            tvPersonName.setFocusable(false);
        } else {
            getUserInfo();
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

        btnBind.setEnabled(etCardId.getText().toString().length() > 0 && (mBankName != null));
    }

    @Override
    public void onBindcardSucceedCancel() {
        // 返回到原来的连接
        //  LTNApplication.getInstance().getCurrentUser().getUserInfo().setUserName(userName);
        //  LTNApplication.getInstance().getCurrentUser().getUserInfo().setCardId(cardId);
        //  Intent mIntent = new Intent(UserAuthActivity.this, UserHasAuthedActivity.class);
        // startActivity(mIntent);
        finish();
    }

    @Override
    public void onBindcardSucceedNext() {
        // 跳转到下一步,并结束
        Intent mIntent = new Intent(BindBankCardActivity.this, DepositActivity.class);
//                getActivity().startActivity(mIntent);
        startActivity(mIntent);
        finish();
    }


    public void getBankNames() {
        final String bankJson = LTNApplication.getInstance().getBankList();
        initBankList(bankJson);
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.BANK_LIST_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            // Log.d(TAG, jsonObject.toString());
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                String bankListJson = dataObj.optJSONArray(LTNConstants.LIST).toString();
                                if (!bankListJson.equals(bankJson)) {
                                    LTNApplication.getInstance().setBankList(bankListJson);
                                    initBankList(bankListJson);
                                }
                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(BindBankCardActivity.this, R.string.ERROR_CODE_1, Toast.LENGTH_SHORT);
                    }
                });

    }

    private void initBankList(String bankJson) {
        if (!TextUtils.isEmpty(bankJson)) {
            Gson gson = new Gson();
            ArrayList<BankItem> bankItems = gson.fromJson(bankJson, new TypeToken<ArrayList<BankItem>>() {
            }.getType());
            for (int i = 0; i < bankItems.size(); i++) {
                sb.append(bankItems.get(i).bankName);
                sb.append("、");
            }
            String str = sb.toString();
            if (str.length() > 0) {
                str = str.substring(0, str.length() - 1);
            }
            ((TextView) findViewById(R.id.tv_bindcard_note)).setText(String.format(getResources().getString(R.string.bindcard_note), str));
        }
    }

    class PhoneClickableSpan extends ClickableSpan {
        private Context context;

        public PhoneClickableSpan(Context _context) {
            this.context = _context;
        }

        @Override
        public void onClick(View widget) {
            ViewUtils.showCallDialog(context, LTNConstants.PHONE_NUMBER);
        }

    }

}
