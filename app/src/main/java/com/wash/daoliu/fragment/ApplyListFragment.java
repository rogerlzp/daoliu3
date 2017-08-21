package com.wash.daoliu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.LTNWebPageActivity;
import com.wash.daoliu.adapter.AnxintouListAdapter;
import com.wash.daoliu.adapter.CloanUserListAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshListView;
import com.wash.daoliu.model.Anxintou;
import com.wash.daoliu.model.Car;
import com.wash.daoliu.model.CloanUserRecord;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.EmptyView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zhengpingli on 2017/6/25.
 */

public class ApplyListFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {

    EmptyView emptyView;
    private CloanUserListAdapter mAdapter = null;
    View view;
    PullToRefreshListView mAnxintouList = null;
    private ArrayList<CloanUserRecord> invests = null;
    private Context mContext;
    private int page = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_list, null);
        mContext = this.getContext();
        initView(view);
        return view;

    }

    public void initView(View view) {
        emptyView = (EmptyView) view.findViewById(R.id.empty_view);
        mAnxintouList = (PullToRefreshListView) view.findViewById(R.id.anxintou_list);
        mAnxintouList.setOnRefreshListener(this);
        ListView mListView = mAnxintouList.getRefreshableView();
        mListView.setDivider(ContextCompat.getDrawable(getActivity(), R.drawable.divide_normal));
        mListView.setDividerHeight(ViewUtils.dip2px(getActivity(), 10));

        //TODO: 修改Postion的起标
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CloanUserRecord cloanUserRecord = invests.get(position - 1);
                if (!TextUtils.isEmpty(cloanUserRecord.h5link)) {
                    Intent intent = new Intent(mContext, LTNWebPageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(LTNWebPageActivity.BUNDLE_URL, cloanUserRecord.h5link);
                    bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, cloanUserRecord.company + "-" + cloanUserRecord.cloanName);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }

            }
        });

        mAnxintouList.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onPullDownToRefresh(mAnxintouList);
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

    public void getData(final boolean isPullDownRefresh) {

        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.put(LTNConstants.CURRENT_PAGE, String.valueOf(page));
        mReqParams.put(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT));
        mReqParams.put(LTNConstants.OPERATION_TYPE, getArguments().getString(LTNConstants.OPERATION_TYPE));

        WCOKHttpClient.getOkHttpClient(getContext()).requestAsyn(LTNConstants.ACCESS_URL.USER_CLOAN_LIST_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {

                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                if (invests == null) {
                                    invests = new ArrayList<CloanUserRecord>();
                                }
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.CLOAN_USER_LIST);
                                ArrayList<CloanUserRecord> myInvests =
                                        new Gson().fromJson(resultArray.toString(), new TypeToken<ArrayList<CloanUserRecord>>() {
                                        }.getType());
                                if (myInvests == null) {
                                    return;
                                }
                                if (isPullDownRefresh) {
                                    invests.clear();
                                }
                                invests.addAll(myInvests);
                                if (mAdapter == null) {
                                    mAdapter = new CloanUserListAdapter(getActivity(), invests);
                                    mAnxintouList.setAdapter(mAdapter);
                                } else {
                                    mAdapter.setData(invests);
                                }
                                if (invests.size() == 0) {
                                    mAnxintouList.setVisibility(View.INVISIBLE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else {
                                    mAnxintouList.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.INVISIBLE);
                                }
                            }
                        } catch (JSONException je) {
                        }
                        mAnxintouList.onRefreshComplete();


                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        mAnxintouList.onRefreshComplete();

                    }
                });
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:

                break;
        }
    }

}
