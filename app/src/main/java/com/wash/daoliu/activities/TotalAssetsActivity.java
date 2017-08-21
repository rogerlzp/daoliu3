package com.wash.daoliu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshScrollView;
import com.wash.daoliu.model.User;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bobo on 2015/12/30.
 */
public class TotalAssetsActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {
    private PullToRefreshScrollView mPullToRefreshScrollView = null;
    private TextView total_asset = null;
    private TextView balance_usable = null;
    private TextView blocked_balance = null;
    private TextView toreceive_principal = null;
    private TextView toreceive_balance = null;
    private TextView current_hold_amount = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_assets);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title)).setText("总资产明细");
        findViewById(R.id.back_btn).setOnClickListener(this);
        total_asset = (TextView) findViewById(R.id.total_asset);
        balance_usable = (TextView) findViewById(R.id.balance_usable);
        blocked_balance = (TextView) findViewById(R.id.blocked_balance);
        toreceive_principal = (TextView) findViewById(R.id.toreceive_principal);
        toreceive_balance = (TextView) findViewById(R.id.toreceive_balance);
        current_hold_amount = (TextView) findViewById(R.id.current_hold_amount);
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_to_refresh_scrollview);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        setData();
        onPullDownToRefresh(mPullToRefreshScrollView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getTotalAssets();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
    }

      /*
        获取Balance
     */

    public void getTotalAssets() {
        // TODO: use GSON later
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.TOTAL_ACCTOUNT_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                User.Account account = new Gson().fromJson(dataObj.toString(), User.Account.class);
                                if (account != null) {
                                    User.getUserInstance().setAccount(account);
                                    setData();
                                }
                            }
                        } catch (JSONException je) {
                        }
                        mPullToRefreshScrollView.onRefreshComplete();
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        mPullToRefreshScrollView.onRefreshComplete();
                    }
                });
    }

    private void setData() {
        if (User.getUserInstance().getAccount() == null) {
            return;
        }
        total_asset.setText(TextUtils.formatDoubleValue(User.getUserInstance().getAccount().getTotalAsset()));
        balance_usable.setText(TextUtils.formatDoubleValue(User.getUserInstance().getAccount().getUsableBalance()));
        blocked_balance.setText(TextUtils.formatDoubleValue(User.getUserInstance().getAccount().getFrozenAmount()));
        toreceive_principal.setText(TextUtils.formatDoubleValue(User.getUserInstance().getAccount().getCollectCapital()));
        toreceive_balance.setText(TextUtils.formatDoubleValue(User.getUserInstance().getAccount().getCollectRevenue()));
        current_hold_amount.setText(TextUtils.formatDoubleValue(User.getUserInstance().getAccount().getCurrentHoldAmount()));

        if (!android.text.TextUtils.isEmpty(User.getUserInstance().getAccount().sxtIsShow)) {
            findViewById(R.id.rl_current).setVisibility(User.getUserInstance().getAccount().sxtIsShow.equals("1") ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Created by zhengpingli on 2017/6/25.
     */

}
