package com.wash.daoliu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.adapter.BirdCoinListAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshScrollView;
import com.wash.daoliu.model.BirdCoin;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;
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
public class BirdDetailActivity extends BaseActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2{
    private PullToRefreshScrollView mBirdList = null;
    private ListView mListView = null;
    private TextView balance = null;
    private TextView text = null;
    private BirdCoinListAdapter mAdapter = null;
    private ArrayList<BirdCoin> mBirdCoin = null;
    private int page = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_detail);
        initView();
    }
    private void initView(){
        ((TextView)findViewById(R.id.title)).setText("鸟币明细");
        findViewById(R.id.back_btn).setOnClickListener(this);
        mBirdList = (PullToRefreshScrollView) findViewById(R.id.balance_list_scrollview);
        mListView = (ListView) findViewById(R.id.balance_list);
        View view = View.inflate(this,R.layout.balance_head_view,null);
        text = (TextView) view.findViewById(R.id.text);
        text.setText("鸟币总额");
        balance = (TextView) view.findViewById(R.id.balance);
        mListView.addHeaderView(view);
        mBirdList.setOnRefreshListener(this);
        mBirdList.setMode(PullToRefreshBase.Mode.BOTH);
        onPullDownToRefresh(mBirdList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page=0;
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

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.BIRDCOINAMOUNT, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();
                            LogUtils.e("+++" + jsonObject.toString());
                         //   if(jsonObject.optInt(LTNConstants.RESULT_CODE)==0){
                                if(mBirdCoin==null){
                                    mBirdCoin = new ArrayList<BirdCoin>();
                                }
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.BIRD_COINS);
                                String totalBirdCoin = dataObj.optString(LTNConstants.TOTAL_AMOUNT);
                                balance.setText(TextUtils.formatDoubleValue(totalBirdCoin));
                                ArrayList<BirdCoin> birdCoins = new Gson().fromJson(resultArray.toString(),new TypeToken<List<BirdCoin>>(){}.getType());
                                if(birdCoins==null){
                                    return;
                                }
                                if(isPullDownToRefresh){
                                    mBirdCoin.clear();
                                }
                                mBirdCoin.addAll(birdCoins);
                                if(mAdapter==null){
                                    mAdapter = new BirdCoinListAdapter(BirdDetailActivity.this,mBirdCoin);
                                    mListView.setAdapter(mAdapter);
                                }else{
                                    mAdapter.setData(mBirdCoin);
                                }
                                if(mBirdCoin.size()==0){
                                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                    mBirdList.setVisibility(View.INVISIBLE);
                                }else{
                                    findViewById(R.id.empty_view).setVisibility(View.INVISIBLE);
                                    mBirdList.setVisibility(View.VISIBLE);
                                }
                        //    }
                        } catch (JSONException je) {
                        }
                        mBirdList.onRefreshComplete();
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                       // Log.e("TAG","++出错了");
                        mBirdList.onRefreshComplete();
                    }
                });

    }
}
