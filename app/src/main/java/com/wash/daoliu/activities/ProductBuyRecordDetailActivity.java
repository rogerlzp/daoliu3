package com.wash.daoliu.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.adapter.ProductBuyListAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshScrollView;
import com.wash.daoliu.model.ProductBuyInfo;
import com.wash.daoliu.model.ProductBuyList;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bobo on 2015/12/30.
 */
public class ProductBuyRecordDetailActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {
    private PullToRefreshScrollView pull_to_refresh_scrollview = null;
    private ListView mListView = null;
    private TextView buy_count = null;
    private ProductBuyListAdapter mAdapter = null;
    private ArrayList<ProductBuyInfo> productBuyInfos = null;
    private int page = 0;
    private String productID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_buy_detail);
        productID = getIntent().getExtras().getString(LTNConstants.PRO_Id);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title)).setText("购买记录");
        findViewById(R.id.back_btn).setOnClickListener(this);
        pull_to_refresh_scrollview = (PullToRefreshScrollView) findViewById(R.id.pull_to_refresh_scrollview);
        mListView = (ListView) findViewById(R.id.buy_list);
        buy_count = (TextView) findViewById(R.id.buy_count);
        View view = View.inflate(this, R.layout.product_buy_list_head_view, null);
        mListView.addHeaderView(view);
        mListView.setVisibility(View.INVISIBLE);

        pull_to_refresh_scrollview.setOnRefreshListener(this);
        pull_to_refresh_scrollview.setMode(PullToRefreshBase.Mode.BOTH);
        onPullDownToRefresh(pull_to_refresh_scrollview);
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
        getData(true);

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page++;
        getData(false);
    }

      /*
        获取Balance
     */

    public void getData(final boolean isPullDownToRefresh) {
        // TODO: use GSON later
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.CURRENT_PAGE, String.valueOf(page));
        mReqParams.add(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT));
        mReqParams.add(LTNConstants.PRO_Id, productID);

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.PURCHASE_RECORD_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                           // Log.e("TAG", "++++" + jsonObject.toString());
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                if (productBuyInfos == null) {
                                    productBuyInfos = new ArrayList<ProductBuyInfo>();
                                }
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                ProductBuyList productBuyList = new Gson().fromJson(dataObj.toString(), ProductBuyList.class);
                                if (productBuyList != null && productBuyList.purchaseHistoryList != null) {

                                    if (isPullDownToRefresh) {
                                        productBuyInfos.clear();
                                    }
                                    productBuyInfos.addAll(productBuyList.purchaseHistoryList);
                                    if (mAdapter == null) {
                                        mAdapter = new ProductBuyListAdapter(ProductBuyRecordDetailActivity.this, productBuyInfos);
                                        mListView.setAdapter(mAdapter);
                                    } else {
                                        mAdapter.setData(productBuyInfos);
                                    }
                                    if (productBuyInfos.size() > 0) {
                                        mListView.setVisibility(View.VISIBLE);
                                        if (!TextUtils.isEmpty(productBuyList.totalCount)) {
                                            buy_count.setVisibility(View.VISIBLE);
                                            buy_count.setText("共" + productBuyList.totalCount + "笔");
                                        }
                                    } else {
                                        buy_count.setVisibility(View.GONE);
                                        mListView.setVisibility(View.GONE);
                                    }
                                }
                                if (productBuyInfos.isEmpty()) {
                                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                    pull_to_refresh_scrollview.setVisibility(View.INVISIBLE);
                                } else {
                                    findViewById(R.id.empty_view).setVisibility(View.INVISIBLE);
                                    pull_to_refresh_scrollview.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException je) {
                            buy_count.setVisibility(View.GONE);
                            mListView.setVisibility(View.GONE);
                        }
                        pull_to_refresh_scrollview.onRefreshComplete();
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pull_to_refresh_scrollview.onRefreshComplete();
                        buy_count.setVisibility(View.GONE);
                        mListView.setVisibility(View.GONE);
                    }
                });

    }


}
