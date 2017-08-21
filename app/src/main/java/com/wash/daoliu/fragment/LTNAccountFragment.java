package com.wash.daoliu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.AccountInfoActivity;
import com.wash.daoliu.activities.BalanceDetailActivity;
import com.wash.daoliu.activities.BirdDetailActivity;
import com.wash.daoliu.activities.CarActivity;
import com.wash.daoliu.activities.CouponsActivity;
import com.wash.daoliu.activities.MyApplyActivity;
import com.wash.daoliu.activities.MyInvestActivity;
import com.wash.daoliu.activities.MyMessageActivity;
import com.wash.daoliu.activities.MyProfileActivity;
import com.wash.daoliu.activities.MyVisitActivity;
import com.wash.daoliu.activities.PartnerActivity;
import com.wash.daoliu.activities.TotalAssetsActivity;
import com.wash.daoliu.activities.UserAuthActivity;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.Car;
import com.wash.daoliu.model.User;
import com.wash.daoliu.model.UserProfile;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.StringUtils;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * 我的账户
 */
public class LTNAccountFragment extends BaseFragment implements View.OnClickListener {

    private View rootView;
    private boolean isPrepared, isViewShown, isShowMoeny = true;
    private boolean isVisible;

    //添加车辆
    private TextView tv_phone, tv_username;
    private ImageView iv_profile;
    private ImageView ivShowMoney;

    private ImageView iv_redtip;
    RelativeLayout rl_me;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_account, null);
        isPrepared = true;
        initView();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isNetworkConnected(getActivity())) {
            if (LTNApplication.getInstance().getSessionKey() != null) {
                getProfile();
            }
        }
    }

    public void initData() {
        UserProfile userProfile = User.getUserInstance().getUserProfile();
        if (!StringUtils.isNullOrEmpty(User.getUserInstance().getUserPhone())) {
            tv_phone.setText(com.wash.daoliu.utility.TextUtils.replaceStarToString(User.getUserInstance().getUserPhone()));
        }
        if (!StringUtils.isNullOrEmpty(userProfile.getUserName())) {
            tv_username.setText(userProfile.getUserName());
        }
    }

    public void initView() {

        (rootView.findViewById(R.id.back_btn)).setVisibility(View.GONE);
        ((TextView) (rootView.findViewById(R.id.title))).setText(R.string.tab_account);
        rootView.findViewById(R.id.rl_my_account).setOnClickListener(this);
        rootView.findViewById(R.id.rl_my_apply).setOnClickListener(this);
        rootView.findViewById(R.id.rl_my_visit).setOnClickListener(this);
        rootView.findViewById(R.id.rl_my_message).setOnClickListener(this);

        rootView.findViewById(R.id.rl_me).setOnClickListener(this);

        tv_phone = (TextView) rootView.findViewById(R.id.tv_phone);
        tv_username = (TextView) rootView.findViewById(R.id.tv_username);
        iv_profile = (ImageView) rootView.findViewById(R.id.iv_profile);

//        tv_phone.setOnClickListener(this);
//        tv_username.setOnClickListener(this);
//        iv_profile.setOnClickListener(this);

        initData();

    }

    public void getProfile() {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        WCOKHttpClient.getOkHttpClient(this.getContext()).requestAsyn(LTNConstants.ACCESS_URL.GET_USER_PROFILE_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {
                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                UserProfile
                                        userProfile =
                                        new Gson().fromJson(dataObj.toString(), UserProfile.class);
                                if (userProfile != null) {
                                    User.getUserInstance().setUserProfile(userProfile);
                                    initData();

                                }
                            }

                        } catch (JSONException je) {
                            // Log.d(FRAGMENT_TAG, je.getMessage());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                    }
                });
    }


    private void setData() {
        User user = LTNApplication.getInstance().getCurrentUser();
//         = user.getAccount();
//        if (account == null) {
//            return;
//        }

    }


    protected void lazyLoad() {
        if (isPrepared || isVisible) {
            initView();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            // Log.d(FRAGMENT_TAG, "isVisibleToUser: true");
            isVisible = true;
        } else {
            // Log.d(FRAGMENT_TAG, "isVisibleToUser: false");
            isVisible = false;
        }
        // Log.d(TAG, "setUserVisibleHint");
        if (this.getView() != null) {
            isViewShown = true;
            lazyLoad();
            // Log.d(FRAGMENT_TAG, "setUserVisibleHint true");
        } else {
            isViewShown = false;
            // Log.d(FRAGMENT_TAG, "setUserVisibleHint false");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && Utils.isNetworkConnected(getActivity())) {
            getProfile();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_apply://可用鸟币
                Intent intent = new Intent(getActivity(), MyApplyActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_visit://合伙人
                intent = new Intent(getActivity(), MyVisitActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_message://总资产
                intent = new Intent(getActivity(), MyMessageActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_my_account:
                Intent mIntent = new Intent(this.getActivity(), AccountInfoActivity.class);
                startActivity(mIntent);
                break;

            case R.id.rl_me://TODO: 个人账户信息，可以修改昵称和名字
                intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
                break;


        }
    }


}

