package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.fragment.UserAuthDialogFragment;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.MyEditText2;
import com.wash.daoliu.view.TrustView;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/18.
 */
public class UserAuthActivity extends BaseActivity implements MyEditText2.OnEditTextChangedListener, TrustView.OnTrustCheckChanged, View.OnClickListener, UserAuthDialogFragment.UserAuthOKListener, UserAuthDialogFragment.UserAuthCancelListener {

    private static final String TAG = UserAuthActivity.class.getSimpleName();

    private UserAuthDialogFragment.UserAuthCancelListener mUserAuthCancelListener;
    private UserAuthDialogFragment.UserAuthOKListener mUserAuthOKListener;

    private MyEditText2 etRealname;
    private MyEditText2 etId;
    private Button btnAuth;
    private TrustView chkMoney;

    private String userName, identityCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_auth);
        initView();

        // TODO: 如果已经实名了,则不能够再修改了
        //    initData();
    }


    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("实名认证");
        etRealname = (MyEditText2) findViewById(R.id.et_realname);
        etId = (MyEditText2) findViewById(R.id.et_id);
        chkMoney = (TrustView) findViewById(R.id.checkbox_money);

        //  chkMoney.initAgreement("资金托管协议", LTNConstants.ACCESS_URL.H5_AUTH_USER_URL);

        chkMoney.setOnTrustCheckChanged(this);
        chkMoney.setData("我已同意", "《资金托管协议》", LTNConstants.ACCESS_URL.H5_AUTH_USER_URL);

        btnAuth = (Button) findViewById(R.id.btn_auth);
        btnAuth.setOnClickListener(baseOnClickListener);

        this.findViewById(R.id.back_btn).setOnClickListener(this);

        //设置最大长度为18
        etRealname.setMaxLength(10);
        etId.setMaxLength(18);

        etRealname.setOnEditTextChanged(this);
        etId.setOnEditTextChanged(this);

    }

    private void authUser() {
        userName = etRealname.getText().toString().trim();
        // TODO: change the name
        if (android.text.TextUtils.isEmpty(userName)) {
            Toast.makeText(UserAuthActivity.this, "姓名不能为空",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkNameChese(userName)) {
            Toast.makeText(UserAuthActivity.this, "请输入中文姓名",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        identityCode = etId.getText().toString().trim();
        if (!identityCode.matches(LTNConstants.IDCARD_PATTERN)) {
            Toast.makeText(UserAuthActivity.this, "身份号码不正确",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!chkMoney.isAgreeSelected()) {
            Toast.makeText(UserAuthActivity.this, "请同意资金托管协议再开通",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        authUser(LTNConstants.CLIENT_TYPE_MOBILE, userName, identityCode,
                LTNApplication.getInstance().getSessionKey());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;

        }

    }

    /**
     * @param _clientType
     * @param _username
     * @param _identityCode
     * @param _sessionKey
     */
    private void authUser(String _clientType, String _username, String _identityCode, String _sessionKey) {
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.ACCOUNT_USERNAME, _username);
        mReqParams.add(LTNConstants.ACCOUNT_IDENTITYCODE, _identityCode);
        mReqParams.add(LTNConstants.SESSION_KEY, _sessionKey);

        showLoadingProgressDialog(UserAuthActivity.this, "正在认证中...");

        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.AUTH_USER_URL, mReqParams,
                new JsonHttpResponseHandler() {

                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        dismissProgressDialog();
                        try {
                            // Log.d(TAG, jsonObject.toString());
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 正确
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                // 缓存用户信息, 不用再次去获取信息
                                // 不更改状态
//                                etRealname.setText(userName);
//                                etRealname.setText(cardId);
                                LTNApplication.getInstance().getCurrentUser().getUserInfo().setUserName(userName);
                                LTNApplication.getInstance().getCurrentUser().getUserInfo().setCardId(identityCode);
                                LTNApplication.getInstance().getCurrentUser().getUserInfo().setCertification("1");


                                UserAuthDialogFragment dialog = new UserAuthDialogFragment();
                                dialog.show(UserAuthActivity.this.getFragmentManager(), "");

                            } else {
                                dismissProgressDialog();
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                Toast.makeText(UserAuthActivity.this,
                                        resultMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException je) {
                            // Log.d(TAG, je.getMessage());
                        }
                    }
                });

    }


    @Override
    public void clickEffective(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_auth:
                // 开通账号页面
                authUser();
                break;
        }
    }


    @Override
    public void onUserAuthCancel() {
        // 返回到原来的连接
        //  LTNApplication.getInstance().getCurrentUser().getUserInfo().setUserName(userName);
        //  LTNApplication.getInstance().getCurrentUser().getUserInfo().setCardId(cardId);
        //  Intent mIntent = new Intent(UserAuthActivity.this, UserHasAuthedActivity.class);
        // startActivity(mIntent);
        finish();
    }

    @Override
    public void onUserAuthOK() {
        // 跳转到下一步,并结束
//        LTNApplication.getInstance().getCurrentUser().getUserInfo().setUserName(userName);
//        LTNApplication.getInstance().getCurrentUser().getUserInfo().setCardId(identityCode);

        Intent mIntent = new Intent(UserAuthActivity.this, BindBankCardActivity.class);
        startActivity(mIntent);
        finish();
    }

    public boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;

    }


    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */

    public boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    @Override
    public void onEditTextChanged(boolean selected) {
        btnAuth.setEnabled(etRealname.getText().toString().length() > 0 && etId.getText().toString().length() > 0 && chkMoney.isAgreeSelected());
    }

    @Override
    public void onChanged(boolean selected) {
        btnAuth.setEnabled(etRealname.getText().toString().length() > 0 && etId.getText().toString().length() > 0 && chkMoney.isAgreeSelected());
    }

    /**
     * Created by zhengpingli on 2017/6/25.
     */


}
