package com.wash.daoliu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.adapter.PartnerAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshScrollView;
import com.wash.daoliu.model.FriendItem;
import com.wash.daoliu.model.FriendStatistics;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bobo on 2015/12/30.
 */
public class PartnerCountFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ScrollView> {
    private PullToRefreshScrollView mPullToRefreshScrollView = null;
    private int pagetype = 0;//0- "我推荐的好友", 1-"好友推荐好友"
    private TextView registeredNum, realNameNum, investNum;
    private ListView mList = null;
    private ArrayList<FriendItem> mFriendItems = null;
    private PartnerAdapter mAdater = null;
    private int page = 0;
    private TextView dateTitle, mobileTitle;
    private View friendLayout = null;
    private TextView type_title = null;
    private String[] types = {"ZC", "SM", "TZ"};//ZC 已注册的好友  SM 已实名的好友 TZ 已投资的好友
    private String[] typeNames = {"已注册好友", "已实名好友", "已投资好友"};//ZC 已注册的好友  SM 已实名的好友 TZ 已投资的好友
    private int currentType = 0;
    private RelativeLayout top_1, top_2, top_3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partner_count, null);
        initView(view);
        return view;
    }

    @Override
    protected void lazyLoad() {

    }

    private void initView(View view) {
        mFriendItems = new ArrayList<FriendItem>();
        mList = (ListView) view.findViewById(R.id.partner_list);
        type_title = (TextView) view.findViewById(R.id.type_title);
        registeredNum = (TextView) view.findViewById(R.id.registeredNum);
        realNameNum = (TextView) view.findViewById(R.id.realNameNum);
        investNum = (TextView) view.findViewById(R.id.investNum);
        dateTitle = (TextView) view.findViewById(R.id.date);
        mobileTitle = (TextView) view.findViewById(R.id.mobile);
        dateTitle.setText("注册日期");
        mobileTitle.setText("好友手机");
        mPullToRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.balance_list);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        pagetype = getArguments().getInt("type", 0);
        mPullToRefreshScrollView.setMode(pagetype == 0 ? PullToRefreshBase.Mode.BOTH : PullToRefreshBase.Mode.PULL_FROM_START);
        friendLayout = view.findViewById(R.id.friend_recommand_layout);
        if (pagetype == 0) {
            view.findViewById(R.id.registered_layout).setOnClickListener(this);
            view.findViewById(R.id.realName_layout).setOnClickListener(this);
            view.findViewById(R.id.invest_layout).setOnClickListener(this);
            top_1 = (RelativeLayout) view.findViewById(R.id.top_1);
            top_2 = (RelativeLayout) view.findViewById(R.id.top_2);
            top_3 = (RelativeLayout) view.findViewById(R.id.top_3);
            setVisible();
        }
    }

    private void setVisible() {
        switch (currentType) {
            case 0:
                top_1.setVisibility(View.VISIBLE);
                top_2.setVisibility(View.INVISIBLE);
                top_3.setVisibility(View.INVISIBLE);
                dateTitle.setVisibility(View.VISIBLE);
                break;
            case 1:
                top_2.setVisibility(View.VISIBLE);
                top_1.setVisibility(View.INVISIBLE);
                top_3.setVisibility(View.INVISIBLE);
                dateTitle.setVisibility(View.INVISIBLE);
                break;
            case 2:
                top_3.setVisibility(View.VISIBLE);
                top_2.setVisibility(View.INVISIBLE);
                top_1.setVisibility(View.INVISIBLE);
                dateTitle.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }

    public void refresh() {
        onPullDownToRefresh(mPullToRefreshScrollView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registered_layout:
                if (currentType != 0) {
                    currentType = 0;
                }
                break;
            case R.id.realName_layout:
                if (currentType != 1) {
                    currentType = 1;
                }
                break;
            case R.id.invest_layout:
                if (currentType != 2) {
                    currentType = 2;
                }
                break;
        }
        setVisible();
        type_title.setText(typeNames[currentType]);
        refresh();
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

    public void getData(final boolean isPullDownToRrefresh
    ) {
        // TODO: use GSON later
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.add(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.add(LTNConstants.PAGE_SIZE, "" + LTNConstants.PAGE_COUNT);
        mReqParams.add(LTNConstants.CURRENT_PAGE, "" + page);
        mReqParams.add(LTNConstants.TYPE, types[currentType]);

        LTNHttpClient.getLTNHttpClient().post(LTNConstants.ACCESS_URL.FRIENDS_COUNT, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();

                            // Log.e("TAG",type+"===ddd"+jsonObject.toString());
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {

                                Gson gson = new Gson();
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                FriendStatistics friendStatistics = null;
                                if (pagetype == 0) {
                                    friendStatistics = gson.fromJson(dataObj.optJSONObject("friends").toString(), FriendStatistics.class);
                                    ArrayList<FriendItem> friendItems = gson.fromJson(dataObj.optJSONArray("userFriendlist").toString(), new TypeToken<ArrayList<FriendItem>>() {
                                    }.getType());
                                    if (isPullDownToRrefresh) {
                                        mFriendItems.clear();
                                    }
                                    if (friendItems != null&&friendItems.size()>0) {
                                        mFriendItems.addAll(friendItems);
                                    }
                                    if (mAdater == null) {
                                        mAdater = new PartnerAdapter(getActivity(), mFriendItems, currentType);
                                        mList.setAdapter(mAdater);
                                    } else {
                                        mAdater.update(mFriendItems, currentType);
                                    }
                                    friendLayout.setVisibility(mFriendItems.isEmpty() ? View.GONE : View.VISIBLE);
                                } else {
                                    friendLayout.setVisibility(View.GONE);
                                    friendStatistics = gson.fromJson(dataObj.optJSONObject("friendss").toString(), FriendStatistics.class);
                                }
                                registeredNum.setText(friendStatistics.registeredNum);
                                realNameNum.setText(friendStatistics.realNameNum);
                                investNum.setText(friendStatistics.investNum);
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
}
