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
import com.wash.daoliu.adapter.AnxintouListAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshListView;
import com.wash.daoliu.model.Anxintou;
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
 * Created by rogerlzp on 16/2/20.
 */
public class AnxintouListFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {

    EmptyView emptyView;
    private AnxintouListAdapter mAdapter = null;
    View view;
    PullToRefreshListView mAnxintouList = null;
    private ArrayList<Anxintou> invests = null;
    private int page = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anxintou_list, null);
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
        // TODO: use GSON later
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.CURRENT_PAGE, String.valueOf(page));
        mReqParams.add(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT));
        mReqParams.add(LTNConstants.ORDER_TYPE, getArguments().getString(LTNConstants.STATUS));

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.ANXINTOU_URL, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                if (invests == null) {
                                    invests = new ArrayList<Anxintou>();
                                }
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.OFFLINE_ORDER_LIST);
                                ArrayList<Anxintou> myInvests =
                                        new Gson().fromJson(resultArray.toString(), new TypeToken<ArrayList<Anxintou>>() {
                                        }.getType());
                                if (myInvests == null) {
                                    return;
                                }
                                if (isPullDownRefresh) {
                                    invests.clear();
                                }
                                invests.addAll(myInvests);
                                if (mAdapter == null) {
                                    mAdapter = new AnxintouListAdapter(getActivity(), invests);
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

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
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

