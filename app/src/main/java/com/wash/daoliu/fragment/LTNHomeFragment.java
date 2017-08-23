package com.wash.daoliu.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.CloanDetailActivity;
import com.wash.daoliu.activities.LTNWebPageActivity;
import com.wash.daoliu.activities.MainActivity;
import com.wash.daoliu.activities.ShopDetailActivity;
import com.wash.daoliu.adapter.CloanGridAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.imageloader.ImageLoaderProxy;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshScrollView;
import com.wash.daoliu.model.Banner;
import com.wash.daoliu.model.Cloan;
import com.wash.daoliu.model.Shop;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.AdPopUpWindow;
import com.wash.daoliu.view.BannerView;
import com.wash.daoliu.view.GridSpacingItemDecoration;
import com.wash.daoliu.view.ProductTagView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;


/**
 * 首页
 */
public class LTNHomeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView>,
        CloanGridAdapter.OnRecyclerViewItemClickListener, PopupWindow.OnDismissListener {
    private View rootView;
    private BannerView bannerView = null;
    private PullToRefreshScrollView mPullToRefreshScrollView = null;
    private ArrayList<Banner> bannerList = new ArrayList<Banner>();
    private ArrayList<Cloan> mCloanList = new ArrayList<Cloan>();
    private GridLayoutManager mLayoutManager;
    private CloanGridAdapter mCloanGridAdapter;

    WindowManager.LayoutParams layoutParams;
    private String adLinkUrl = null;
    private String adImageUrl = null;

    int adHeight = 320;
    int adWidth = 280;

    AdPopUpWindow adPopUpWindow;

    ProgressDialog mProgressdialog;
    private Context mContext;

    private Activity mContext2;

    CloanGridAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    AdPopUpWindow.OnAdClickedListener onAdClickedListener = new AdPopUpWindow.OnAdClickedListener() {
        @Override
        public void onAdClicked() {

            if (!TextUtils.isEmpty(adLinkUrl)) {
                //       addRecord(LTNConstants.OPERATION_APPLY, cloan);
                Intent intent = new Intent(mContext, LTNWebPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(LTNWebPageActivity.BUNDLE_URL, adLinkUrl);
                // bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, cloan.getCompany() + "-" + cloan.getCloanName());
                //  bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                if (adPopUpWindow != null && adPopUpWindow.isShowing()) {
                    adPopUpWindow.dismiss();
                }

            }
        }
    };

    RecyclerView rv_cloan;
    SwipeRefreshLayout srl_cloan;

    // 产品详情

    private int page = 0;

    boolean isHidden = false;

    // boolean afterClickAd = false;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_home, null);
        initView();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtils.calculateScreenSize(getActivity());
        Utils.checkVersion(getActivity());
        mContext = this.getContext();

        mContext2 = this.getActivity();
        Window window = ((Activity) getContext()).getWindow();
        layoutParams = window.getAttributes();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 && resultCode == Activity.RESULT_OK) {
            if (!isHidden) {
                getData();
                getBanner();
                //getAd();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (!hidden) {
            getData();
            getBanner();
            //   getAd();
        }
    }

    public void showDialog() {
        if (mProgressdialog == null || !mProgressdialog.isShowing()) {
            mProgressdialog = new ProgressDialog(this.getActivity());
            mProgressdialog.setMessage("正在加载数据");
            mProgressdialog.setIndeterminate(true);
            mProgressdialog.setCancelable(true);
            mProgressdialog.show();
        }
    }

    public void dismissDialog() {
        if (mProgressdialog != null || !mProgressdialog.isShowing()) {
            mProgressdialog.dismiss();
        }
    }

    public void getBanner() {

        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);

        WCOKHttpClient.getOkHttpClient(getActivity()).requestAsyn(LTNConstants.ACCESS_URL.GET_BANNER_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {

                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                Gson gson = new Gson();
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                JSONArray bannerArray = resultObj.getJSONArray(LTNConstants.BANNERS);
                                if (bannerList == null || (bannerList != null && bannerList.size() == 0)) {
                                    bannerList = gson.fromJson(bannerArray.toString(), new TypeToken<ArrayList<Banner>>() {
                                    }.getType());
                                    if (bannerList != null) {
                                        bannerView.initAdv(bannerList);
                                    }
                                }

                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                if (!android.text.TextUtils.isEmpty(resultMsg)) {
                                    Toast.makeText(getActivity(), resultMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (
                                JSONException je) {
                            // Log.d(FRAGMENT_TAG, je.getMessage());
                        }
                        if (mPullToRefreshScrollView.isRefreshing()) {
                            mPullToRefreshScrollView.onRefreshComplete();
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                        if (mPullToRefreshScrollView.isRefreshing()) {
                            mPullToRefreshScrollView.onRefreshComplete();
                        }
                    }

                });

    }

    public void getAd() {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);

        WCOKHttpClient.getOkHttpClient(getActivity()).requestAsyn(LTNConstants.ACCESS_URL.GET_AD_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {

                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                if (resultObj.optInt(LTNConstants.IS_SHOW_AD) == 1) {
                                    //弹出广告

                                    adImageUrl = resultObj.optString(LTNConstants.IMAGE_URL, null);
                                    adLinkUrl = resultObj.optString(LTNConstants.LINK_URL, null);

                                    showAd();
                                }


                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                if (!android.text.TextUtils.isEmpty(resultMsg)) {
                                    Toast.makeText(getActivity(), resultMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (
                                JSONException je) {
                            // Log.d(FRAGMENT_TAG, je.getMessage());
                        }
                        if (mPullToRefreshScrollView.isRefreshing()) {
                            mPullToRefreshScrollView.onRefreshComplete();
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        if (mPullToRefreshScrollView.isRefreshing()) {
                            mPullToRefreshScrollView.onRefreshComplete();
                        }
                    }

                });

    }

    public void getData() {

        //     showLoadingProgressDialog(this.getActivity(), "正在加载数据...");

        showDialog();

        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);

        mReqParams.put(LTNConstants.CURRENT_PAGE, String.valueOf(page));
        mReqParams.put(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT));

        if (!android.text.TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
            mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        }

        WCOKHttpClient.getOkHttpClient(getActivity()).requestAsyn(LTNConstants.ACCESS_URL.CLOAN_LIST_URL, WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        dismissDialog();
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                Gson gson = new Gson();
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                JSONArray cloanArray = resultObj.getJSONArray(LTNConstants.CLOAN_LIST);

                                if (mCloanList == null || (mCloanList != null && mCloanList.size() == 0)) {

                                    Type cloanType = new TypeToken<ArrayList<Cloan>>() {
                                    }.getType();
                                    mCloanList = gson.fromJson(cloanArray.toString(), cloanType);
                                    if (mCloanList != null) {
                                        mCloanGridAdapter.setCloans(mCloanList);
                                    }
                                }


                            } else {
                                String resultMsg = jsonObject.getString(LTNConstants.RESULT_MESSAGE);
                                if (!android.text.TextUtils.isEmpty(resultMsg)) {
                                    Toast.makeText(getActivity(), resultMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (
                                JSONException je) {
                            // Log.d(FRAGMENT_TAG, je.getMessage());
                        }
                        if (mPullToRefreshScrollView.isRefreshing())

                        {
                            mPullToRefreshScrollView.onRefreshComplete();
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        //      dismissProgressDialog();
                        if (mPullToRefreshScrollView.isRefreshing()) {
                            mPullToRefreshScrollView.onRefreshComplete();
                        }
                    }

                });

    }

    private void setData() {
        if (getActivity() == null) {
            return;
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (bannerView != null) {
            bannerView.bannerStopPlay();
        }
        if (adPopUpWindow != null && adPopUpWindow.isShowing()) {
            adPopUpWindow.dismiss();
        }
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerView != null) {
            bannerView.bannerStartPlay();
        }
        if (!isHidden) {
            getData();
            getBanner();

            if (LTNApplication.getInstance().isShowAd()) {
                getAd();
                LTNApplication.getInstance().setShowAd(false);
            }
        }
    }


    public void initView() {


        ((TextView) rootView.findViewById(R.id.title)).setText(getString(R.string.tab_main));
        rootView.findViewById(R.id.back_btn).setVisibility(View.GONE);
        //   rootView.findViewById(R.id.right_menu_commit).setVisibility(View.GONE);
        //  rootView.findViewById(R.id.right_menu_button).setVisibility(View.GONE);

        bannerView = (BannerView) rootView.findViewById(R.id.id_viewflow);
        mPullToRefreshScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.pull_to_refresh_scrollview);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        rv_cloan = (RecyclerView) rootView.findViewById(R.id.rv_cloan);

        mLayoutManager = new GridLayoutManager(this.getActivity(), 4, GridLayoutManager.VERTICAL, false);//设置为一个3列的纵向网格布局

        rv_cloan.setLayoutManager(mLayoutManager);
        // srl_cloan = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_cloan);
        //调整SwipeRefreshLayout的位置
        // srl_cloan.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        int spanCount = 3;
        int spacing = 2;
        boolean includeEdge = false;
        rv_cloan.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        mCloanGridAdapter = new CloanGridAdapter(this.getContext(), mCloanList);
        mCloanGridAdapter.setOnItemClickListener(this);
        rv_cloan.setAdapter(mCloanGridAdapter);


    }

    private void setListener() {
        //swipeRefreshLayout刷新监听
        srl_cloan.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }
        });
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }

    @Override
    public void onItemClick(View view, Cloan cloan) {
//        Intent intent = new Intent(mContext, CloanDetailActivity.class);
//
//        intent.putExtra(LTNConstants.CLOAN, new Gson().toJson(cloan));
//        startActivity(intent);

        if (!TextUtils.isEmpty(cloan.getH5link())) {
            addRecord(LTNConstants.OPERATION_APPLY, cloan);
            Intent intent = new Intent(this.getActivity(), LTNWebPageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(LTNWebPageActivity.BUNDLE_URL, cloan.getH5link());
            bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, cloan.getCompany() + "-" + cloan.getCloanName());
            //  bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }

    }

    @Override
    public void onItemLongClick(View view) {

    }

    public void addRecord(String operationType, Cloan currentCloan) {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        if (!TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
            mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        }
        mReqParams.put(LTNConstants.CLOAN_NO, currentCloan.getCloanNo());
        mReqParams.put(LTNConstants.OPERATION_TYPE, operationType);

        WCOKHttpClient.getOkHttpClient(this.getContext()).requestAsyn(LTNConstants.ACCESS_URL.ADD_USER_CLOAN_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {
                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);
                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                // nothing
                            } else { //TODO

                            }
                        } catch (
                                JSONException je) {
                            // Log.d(FRAGMENT_TAG, je.getMessage());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                    }
                });
    }

    public void showAd() {

        //  window.setAttributes(layoutParams);

        // adPopUpWindow = new AdPopUpWindow(mContext2, adImageUrl, adHeight, adWidth);
        adPopUpWindow = new AdPopUpWindow(mContext2, adImageUrl, adHeight, adWidth, layoutParams);
       // adPopUpWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));
        adPopUpWindow.setOnClickedListener(onAdClickedListener);
        adPopUpWindow.showAtLocation(this.getView(), Gravity.CENTER, 0, 0);
      //  adPopUpWindow.setOnDismissListener(this);
//        PopupWindow popupWindow = new PopupWindow();
//
//        View view = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(
//                R.layout.guide_one, null);
//        popupWindow.setContentView(view);
//
//        popupWindow.setWidth(ViewUtils.dip2px(mContext, 200)); // 设置SelectPicPopupWindow弹出窗体的宽
//        popupWindow.setHeight(ViewUtils.dip2px(mContext, 500)); // 设置SelectPicPopupWindow弹出窗体的高
//
//        popupWindow.setBackgroundDrawable(new ColorDrawable(0xffffff));
//
//        popupWindow.showAtLocation(this.getView(), Gravity.CENTER, 0, 0);
//
//       // popupWindow.showAsDropDown(this.getView());
////        Window window = ((Activity) getContext()).getWindow();
////        WindowManager.LayoutParams layoutParams = window.getAttributes();
////        layoutParams.alpha = 0.1f;
////        window.setAttributes(layoutParams);
//
//        WindowManager.LayoutParams lp = mContext2.getWindow().getAttributes();
//        lp.alpha = 0.2f;
//        mContext2.getWindow().setAttributes(lp);
//
//        mContext2.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


    }


    @Override
    public void onDismiss() {
        Window window = ((Activity) getContext()).getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //  Window window = ((Activity) getContext()).getWindow();
        window.setAttributes(layoutParams);
        adPopUpWindow.dismiss();
    }
}