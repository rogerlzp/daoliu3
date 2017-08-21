package com.wash.daoliu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.adapter.IncomeListAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshScrollView;
import com.wash.daoliu.model.Income;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.TextUtils;
import com.wash.daoliu.view.EmptyView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bobo on 2015/12/30.
 */
public class IncomeListFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {
    private PullToRefreshScrollView mBalanceList = null;
    private ListView mListView = null;
    private TextView balance = null;
    private IncomeListAdapter mAdapter = null;
    private ArrayList<Income> mIncomes = null;
    private int page = 0;
    private int type = 0;//0-待收收益，1-累计收益 
    private EmptyView emptyView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_list, null);
        initView(view);

        return view;
    }

    @Override
    protected void lazyLoad() {

    }

    private void initView(View view) {
        emptyView = (EmptyView) view.findViewById(R.id.empty_view);
        mBalanceList = (PullToRefreshScrollView) view.findViewById(R.id.balance_list_scrollview);
        mListView = (ListView) view.findViewById(R.id.balance_list);
        View v = View.inflate(getActivity(), R.layout.balance_head_view, null);
        balance = (TextView) v.findViewById(R.id.balance);
        ((TextView) v.findViewById(R.id.text)).setText("总金额");
        mListView.addHeaderView(v);
        mBalanceList.setOnRefreshListener(this);
        mBalanceList.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onPullDownToRefresh(mBalanceList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:

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

    public void getData(final boolean isPullDownRefresh) {
        // TODO: use GSON later
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.CURRENT_PAGE, String.valueOf(page));
        mReqParams.add(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT));
        mReqParams.add(LTNConstants.TYPE, String.valueOf(getArguments().getInt("type", 0)));

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.INCOME_DETAIL_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            if (mIncomes == null) {
                                mIncomes = new ArrayList<Income>();
                            }
                            JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                            JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.INCOME_LIST);
                            balance.setText(TextUtils.formatDoubleValue(dataObj.optString(LTNConstants.INCOME_TOTAL)));
                            ArrayList<Income> incomes = new Gson().fromJson(resultArray.toString(), new TypeToken<List<Income>>() {
                            }.getType());
                            if (incomes == null) {
                                return;
                            }
                            if (isPullDownRefresh) {
                                mIncomes.clear();
                            }
                            mIncomes.addAll(incomes);
                            if (mAdapter == null) {
                                mAdapter = new IncomeListAdapter(getActivity(), mIncomes);
                                mListView.setAdapter(mAdapter);
                            } else {
                                mAdapter.setBalances(mIncomes);
                            }
                            //  }
                        } catch (JSONException je) {
                        }
                        mBalanceList.onRefreshComplete();
                        if (mIncomes.size() == 0) {
                            mBalanceList.setVisibility(View.INVISIBLE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            mBalanceList.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.INVISIBLE);
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        mBalanceList.onRefreshComplete();
                    }
                });

    }
}
