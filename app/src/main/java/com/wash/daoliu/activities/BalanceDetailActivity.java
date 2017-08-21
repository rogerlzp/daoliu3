package com.wash.daoliu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tendcloud.tenddata.TCAgent;
import com.wash.daoliu.R;
import com.wash.daoliu.adapter.BalanceListAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshScrollView;
import com.wash.daoliu.model.Balance;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bobo on 2015/12/30.
 */
public class BalanceDetailActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {
    private PullToRefreshScrollView mBalanceList = null;
    private ListView mListView = null;
    private TextView balance = null;
    private BalanceListAdapter mAdapter = null;
    private ArrayList<Balance> mBalances = null;
    private int page = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_detail);
        initView();
        onPullDownToRefresh(mBalanceList);
    }

    private void initView() {
        ((TextView) findViewById(R.id.title)).setText("余额明细");
        findViewById(R.id.back_btn).setOnClickListener(this);
        mBalanceList = (PullToRefreshScrollView) findViewById(R.id.balance_list_scrollview);
        mListView = (ListView) findViewById(R.id.balance_list);
        View view = View.inflate(this, R.layout.balance_head_view, null);
        balance = (TextView) view.findViewById(R.id.balance);
        mListView.addHeaderView(view);
        mBalanceList.setOnRefreshListener(this);
        mBalanceList.setMode(PullToRefreshBase.Mode.BOTH);
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
        page = 0;
        getBalances(true);

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page++;
        getBalances(false);
    }

      /*
        获取Balance
     */

    public void getBalances(final boolean isPullDownToRefresh) {
        // TODO: use GSON later
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.CURRENT_PAGE, String.valueOf(page));
        mReqParams.add(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT));

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.BALANCE_LIST_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();

                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                if (mBalances == null) {
                                    mBalances = new ArrayList<Balance>();
                                }
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.BALANCE_DETAILS);
                                String balanceUse = dataObj.optString(LTNConstants.BALANCE_USEABLE);
                                balance.setText(TextUtils.formatDoubleValue(balanceUse));
                                ArrayList<Balance> balances = new Gson().fromJson(resultArray.toString(), new TypeToken<List<Balance>>() {
                                }.getType());
                                if (balances == null) {
                                    return;
                                }
                                if (isPullDownToRefresh) {
                                    mBalances.clear();
                                }
                                mBalances.addAll(balances);
                                if (mAdapter == null) {
                                    mAdapter = new BalanceListAdapter(BalanceDetailActivity.this, mBalances);
                                    mListView.setAdapter(mAdapter);
                                } else {
                                    mAdapter.setBalances(mBalances);
                                }
                            }
                        } catch (JSONException je) {
                        }
                        mBalanceList.onRefreshComplete();
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        mBalanceList.onRefreshComplete();
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        // Talking data
        TCAgent.onResume(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        // Talking data
        TCAgent.onPause(this);
    }
}
