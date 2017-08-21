package com.wash.daoliu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.adapter.MyInvestListAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshListView;
import com.wash.daoliu.model.MyInvest;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.EmptyView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bobo on 2015/12/30.
 */
public class MyInvestListFragment extends BaseFragment implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2{
//    private PullToRefreshScrollView mBalanceList = null;
    private PullToRefreshListView mBalanceList = null;
    private MyInvestListAdapter mAdapter = null;
    private ArrayList<MyInvest> invests = null;
    private int page = 0;
    private EmptyView emptyView = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invest_list,null);
        initView(view);
        return view;
    }

    @Override
    protected void lazyLoad() {

    }

    private void initView(View view){
        emptyView = (EmptyView) view.findViewById(R.id.empty_view);
        mBalanceList = (PullToRefreshListView) view.findViewById(R.id.balance_list);
        mBalanceList.setOnRefreshListener(this);
        ListView mListView = mBalanceList.getRefreshableView();
        mListView.setDivider(ContextCompat.getDrawable(getActivity(), R.drawable.divide_normal));
        mListView.setDividerHeight(ViewUtils.dip2px(getActivity(),10));
        mBalanceList.setMode(PullToRefreshBase.Mode.BOTH);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onPullDownToRefresh(mBalanceList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:

                break;
        }
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page=0;
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

    public void getData(final boolean isPullDownRefresh) {
        // TODO: use GSON later
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.CURRENT_PAGE, String.valueOf(page));
        mReqParams.add(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT));
        mReqParams.add(LTNConstants.STATUS,getArguments().getString(LTNConstants.STATUS));

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.MY_INVEST_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                if(invests==null){
                                    invests = new ArrayList<MyInvest>();
                                }
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.INVESTMENTS);
                                ArrayList<MyInvest> myInvests =
                                        new Gson().fromJson(resultArray.toString(), new TypeToken<ArrayList<MyInvest>>() {
                                }.getType());
                                if (myInvests == null) {
                                    return;
                                }
                                if(isPullDownRefresh){
                                    invests.clear();
                                }
                                invests.addAll(myInvests);
                                if (mAdapter == null) {
                                    mAdapter = new MyInvestListAdapter(getActivity(),invests);
                                    mBalanceList.setAdapter(mAdapter);
                                } else {
                                    mAdapter.setData(invests);
                                }
                                if(invests.size()==0){
                                    mBalanceList.setVisibility(View.INVISIBLE);
                                    emptyView.setVisibility(View.VISIBLE);
                                }else{
                                    mBalanceList.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.INVISIBLE);
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
}
