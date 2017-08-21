package com.wash.daoliu.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.CloanDetailActivity;
import com.wash.daoliu.activities.RegisterActivity;
import com.wash.daoliu.adapter.CloanListAdapter;
import com.wash.daoliu.adapter.ShopAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshListView;
import com.wash.daoliu.model.Cloan;

import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.StringUtils;
import com.wash.daoliu.utility.TextUtils;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.wash.daoliu.R;
import com.wash.daoliu.view.LoadingDialogGif;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;


/**
 * 我要理财
 */
public class LTNInvestimentFragment extends BaseFragment implements OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {

    private View rootView;
    private Context mContext;
    PullToRefreshListView lvProduct;

    public CloanListAdapter mCloanAdapter;
    LoadingDialogGif mLoadingDialog;
    private int page = 0;
    // TextView nsp_loan_amount, nsp_loan_date;
    private ArrayList<Cloan> mCloanList = new ArrayList<Cloan>();
    private boolean isPrepared;
    private boolean isCreate = true;

    String loanDate = "", loanAmount = "";
    private int loanDateCounter = 0;

    Button btn_renqi, btn_dae, btn_jsxk, btn_bczx, btn_zyzy, btn_all;


    private ArrayList<String> mTagList = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_cloans, null);
        isPrepared = true;
        initView(rootView, inflater);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app = (LTNApplication) this.getActivity().getApplication();

        mContext = this.getContext();
        mCloanAdapter = new CloanListAdapter(getActivity());
        lvProduct.setAdapter(mCloanAdapter);
        lvProduct.setOnRefreshListener(this);
        ListView listview = lvProduct.getRefreshableView();
        listview.setDivider(ContextCompat.getDrawable(getActivity(), R.drawable.divide_normal));
        listview.setDividerHeight(ViewUtils.dip2px(getActivity(), 10));

        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, CloanDetailActivity.class);
                intent.putExtra(LTNConstants.CLOAN, new Gson().toJson(mCloanList.get(position - 1)));
                startActivity(intent);
            }
        });
        onPullDownToRefresh(lvProduct);

        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isCreate) {
            refreshCurrent(true);
        } else {
            isCreate = false;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if ((!hidden) && !isCreate) {
            refreshCurrent(true);
        } else {
            isCreate = false;
        }
    }

    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible) {
        }
    }

    public void showDialog() {

        mLoadingDialog = new LoadingDialogGif(this.getContext(), "正在加载数据...", R.drawable.loading_big,
                LoadingDialogGif.TYPE_GIF, R.style.MyDialogStyle);

        if (mLoadingDialog == null || !mLoadingDialog.isShowing()) {
            mLoadingDialog = new LoadingDialogGif(this.getContext(),
                    "正在加载数据...", R.drawable.loading_big, LoadingDialogGif.TYPE_GIF);

            mLoadingDialog.show();
        }
    }

    public void dismissDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void initView(View rootView, LayoutInflater inflater) {
        rootView.findViewById(R.id.back_btn).setVisibility(View.GONE);
        ((TextView) rootView.findViewById(R.id.title)).setText(getString(R.string.tab_investiment));
        lvProduct = (PullToRefreshListView) rootView.findViewById(R.id.lv_products);
        lvProduct.setMode(PullToRefreshBase.Mode.BOTH);

//        nsp_loan_amount = (TextView) rootView.findViewById(R.id.nsp_loan_amount);
//        nsp_loan_date = (TextView) rootView.findViewById(R.id.nsp_loan_date);

        btn_renqi = (Button) rootView.findViewById(R.id.btn_renqi);
        btn_dae = (Button) rootView.findViewById(R.id.btn_dae);
        btn_jsxk = (Button) rootView.findViewById(R.id.btn_jsxk);
        btn_bczx = (Button) rootView.findViewById(R.id.btn_bczx);
        btn_zyzy = (Button) rootView.findViewById(R.id.btn_zyzy);
        btn_all = (Button) rootView.findViewById(R.id.btn_all);

        btn_renqi.setOnClickListener(this);
        btn_dae.setOnClickListener(this);
        btn_jsxk.setOnClickListener(this);
        btn_bczx.setOnClickListener(this);
        btn_zyzy.setOnClickListener(this);
        btn_all.setOnClickListener(this);

//        nsp_loan_date.setOnClickListener(this);
//        nsp_loan_amount.setOnClickListener(this);

    }

    public void initData() {
//        ArrayAdapter<CharSequence> loanAmountAdapter = ArrayAdapter.createFromResource(mContext,
//                R.array.loan_amount_array, android.R.layout.simple_spinner_item);
//
//        loanAmountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        nsp_loan_amount.setAdapter(loanAmountAdapter);
//        nsp_loan_amount.setPrompt("测试");
//        nsp_loan_amount.setOnItemSelectedListener(new SpinnerListener());
//
//        ArrayAdapter<CharSequence> loanTimeAdapter = ArrayAdapter.createFromResource(mContext,
//                R.array.loan_time_array, android.R.layout.simple_spinner_item);
//
//        loanTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        nsp_loan_date.setAdapter(loanTimeAdapter);
//        nsp_loan_date.setPrompt("测试");
//        nsp_loan_date.setOnItemSelectedListener(new SpinnerListener2());


    }

    class SpinnerListener implements android.widget.AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            //将选择的元素显示出来
            if (!loanAmount.equals(parent.getItemAtPosition(position).toString())) //选择的值没变化
            {
                loanAmount = parent.getItemAtPosition(position).toString();

                refreshCurrent(true);

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 0;
        getShopsByTag(true);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        page++;
        getShopsByTag(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_renqi:
                setItBgAllToGray(v);
                btn_renqi.setSelected(btn_renqi.isSelected() ? false : true);
                page = 0;
                getShopsByTag(true);
                break;
            case R.id.btn_dae:
                setItBgAllToGray(v);
                btn_dae.setSelected(btn_dae.isSelected() ? false : true);
                page = 0;
                getShopsByTag(true);
                break;
            case R.id.btn_jsxk:
                setItBgAllToGray(v);
                btn_jsxk.setSelected(btn_jsxk.isSelected() ? false : true);
                page = 0;
                getShopsByTag(true);
                break;
            case R.id.btn_bczx:
                setItBgAllToGray(v);
                btn_bczx.setSelected(btn_bczx.isSelected() ? false : true);
                getShopsByTag(true);
                break;
            case R.id.btn_zyzy:
                setItBgAllToGray(v);
                btn_zyzy.setSelected(btn_zyzy.isSelected() ? false : true);
                page = 0;
                getShopsByTag(true);
                break;
            case R.id.btn_all:
                setItBgAllToGray(v);
                btn_all.setSelected(btn_all.isSelected() ? false : true);
                page = 0;
                getShopsByTag(true);
                break;

//            case R.id.nsp_loan_date:
//
//                OptionPicker picker = new OptionPicker(this.getActivity(), getResources().getStringArray(R.array.loan_amount));
//
//                picker.setCanceledOnTouchOutside(false);
//                picker.setDividerRatio(WheelView.DividerConfig.FILL);
//                picker.setShadowColor(Color.RED, 40);
//                picker.setSelectedIndex(1);
//                picker.setCycleDisable(true);
//                picker.setTextSize(16);
//                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
//                    @Override
//                    public void onOptionPicked(int index, String item) {
//                        switch (index) {
//                            case 0:
//                                loanDate = "7";
//                                break;
//                            case 1:
//                                loanDate = "15";
//                                break;
//                            case 2:
//                                loanDate = "30";
//                                break;
//                            case 3:
//                                loanDate = "60";
//                                break;
//                            case 4:
//                                loanDate = "90";
//                                break;
//                            case 5:
//                                loanDate = "180";
//                                break;
//                            case 6:
//                                loanDate = "360";
//                                break;
//                            case 7:
//                                loanDate = "others";
//                                break;
//                        }
////                        nsp_loan_date.setText(loanDate + "天");
//                        //    refreshCurrent(true);
//                    }
//                });
//                picker.show();
//                break;
//
//            case R.id.nsp_loan_amount:
//
//                OptionPicker picker2 = new OptionPicker(this.getActivity(), getResources().getStringArray(R.array.loan_amount_array));
//
//                picker2.setCanceledOnTouchOutside(false);
//                picker2.setDividerRatio(WheelView.DividerConfig.FILL);
//                picker2.setShadowColor(Color.RED, 40);
//                picker2.setSelectedIndex(1);
//                picker2.setCycleDisable(true);
//                picker2.setTextSize(16);
//                picker2.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
//                    @Override
//                    public void onOptionPicked(int index, String item) {
//                        loanAmount = item;
////                        nsp_loan_amount.setText(loanAmount + "元");
//                        //  refreshCurrent(true);
//                    }
//
//
//                });
//
//                picker2.show();
//                break;

        }
    }

    private void setItBgAllToGray(View v) {
        switch (v.getId()) {
            case R.id.btn_renqi:
                btn_dae.setSelected(false);
                btn_jsxk.setSelected(false);
                btn_bczx.setSelected(false);
                btn_zyzy.setSelected(false);
                btn_all.setSelected(false);
                break;
            case R.id.btn_dae:
                btn_renqi.setSelected(false);
                btn_jsxk.setSelected(false);
                btn_bczx.setSelected(false);
                btn_zyzy.setSelected(false);
                btn_all.setSelected(false);
                break;
            case R.id.btn_jsxk:
                btn_renqi.setSelected(false);
                btn_dae.setSelected(false);
                btn_bczx.setSelected(false);
                btn_zyzy.setSelected(false);
                btn_all.setSelected(false);
                break;
            case R.id.btn_bczx:
                btn_dae.setSelected(false);
                btn_jsxk.setSelected(false);
                btn_renqi.setSelected(false);
                btn_zyzy.setSelected(false);
                btn_all.setSelected(false);
                break;
            case R.id.btn_zyzy:
                btn_renqi.setSelected(false);
                btn_jsxk.setSelected(false);
                btn_bczx.setSelected(false);
                btn_dae.setSelected(false);
                btn_all.setSelected(false);
                break;
            case R.id.btn_all:
                btn_renqi.setSelected(false);
                btn_dae.setSelected(false);
                btn_bczx.setSelected(false);
                btn_zyzy.setSelected(false);
                btn_jsxk.setSelected(false);
                break;

        }
    }


    public void getShopsByTag(final boolean isPullDownToRefresh) {

        showDialog();
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.CURRENT_PAGE, "" + page);
        mReqParams.put(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT + page * LTNConstants.PAGE_COUNT));

        StringBuffer sb = new StringBuffer();

        String sessionKey = LTNApplication.getInstance().getSessionKey();
        if (sessionKey != null) {
            mReqParams.put(LTNConstants.SESSION_KEY, sessionKey);
        }
        if (btn_renqi.isSelected()) {
            sb.append("TAG_RENQI" + ";");
        }
        if (btn_dae.isSelected()) {
            sb.append("TAG_DAE" + ";");
        }
        if (btn_jsxk.isSelected()) {
            sb.append("TAG_JSXK" + ";");
        }
        if (btn_bczx.isSelected()) {
            sb.append("TAG_BCZX" + ";");
        }
        if (btn_zyzy.isSelected()) {
            sb.append("TAG_ZYZY" + ";");
        }
        if (btn_all.isSelected()) {
            sb.append("TAG_ALL" + ";");
        }


        mReqParams.put(LTNConstants.TAG_STRING, sb.toString());


        WCOKHttpClient.getOkHttpClient(this.getContext()).requestAsyn(LTNConstants.ACCESS_URL.CLOAN_TAG_LIST_URL, WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        dismissDialog();
                        try {
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {

                                Gson gson = new Gson();
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                JSONArray cloanArray = resultObj.getJSONArray(LTNConstants.CLOAN_LIST);

                                ArrayList<Cloan> cloans = new Gson().fromJson(cloanArray.toString(),
                                        new TypeToken<ArrayList<Cloan>>() {
                                        }.getType());
                                if (cloans != null) {
                                    if (isPullDownToRefresh) {
                                        mCloanList.clear();
                                    }
                                    mCloanList.addAll(cloans);
                                }
                                mCloanAdapter.setCloans(mCloanList);

                            }
                        } catch (JSONException je) {
                            // Log.d(FRAGMENT_TAG, je.getMessage());
                        }
                        lvProduct.onRefreshComplete();
                        upDateRefreshTime();
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        dismissDialog();
                        lvProduct.onRefreshComplete();
                    }
                });

    }


    public void refreshCurrent(final boolean isPullDownToRefresh) {

        showDialog();

        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);

        mReqParams.put(LTNConstants.CURRENT_PAGE, "" + page);
        mReqParams.put(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT + page * LTNConstants.PAGE_COUNT));
        String sessionKey = LTNApplication.getInstance().getSessionKey();
        if (sessionKey != null) {
            mReqParams.put(LTNConstants.SESSION_KEY, sessionKey);
        }

        StringBuffer sb = new StringBuffer();


        if (btn_renqi.isSelected()) {
            sb.append("TAG_RENQI" + ";");
        }
        if (btn_dae.isSelected()) {
            sb.append("TAG_DAE" + ";");
        }
        if (btn_jsxk.isSelected()) {
            sb.append("TAG_JSXK" + ";");
        }
        if (btn_bczx.isSelected()) {
            sb.append("TAG_BCZX" + ";");
        }
        if (btn_zyzy.isSelected()) {
            sb.append("TAG_ZYZY" + ";");
        }
        if (btn_all.isSelected()) {
            sb.append("TAG_ALL" + ";");
        }

        mReqParams.put(LTNConstants.TAG_STRING, sb.toString());

//        if (!StringUtils.isNullOrEmpty(loanAmount)) {
//            mReqParams.put(LTNConstants.LOAN_MIN, loanAmount);
//        }
//
//        if (!StringUtils.isNullOrEmpty(loanDate)) {
//            mReqParams.put(LTNConstants.DATE_RANGE_MIN, loanDate);
//        }
        // TODO: 上传location

        WCOKHttpClient.getOkHttpClient().requestAsyn(LTNConstants.ACCESS_URL.CLOAN_TAG_LIST_URL, WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        dismissDialog();

                        try {

                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                if (mCloanList == null) {
                                    mCloanList = new ArrayList<Cloan>();
                                }

                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.CLOAN_LIST);
                                ArrayList<Cloan> cloans = new Gson().fromJson(resultArray.toString(),
                                        new TypeToken<ArrayList<Cloan>>() {
                                        }.getType());
                                if (cloans != null) {
                                    if (isPullDownToRefresh) {
                                        mCloanList.clear();
                                    }
                                    mCloanList.addAll(cloans);
                                }
                                mCloanAdapter.setCloans(mCloanList);

                            }


                            lvProduct.onRefreshComplete();
                            upDateRefreshTime();


                        } catch (JSONException je) {
                            // Log.d(FRAGMENT_TAG, je.getMessage());
                        }

                        lvProduct.onRefreshComplete();
                        upDateRefreshTime();
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        dismissDialog();

                        lvProduct.onRefreshComplete();
                        Utils.isNetworkConnected(getActivity());
                    }
                });


    }

    //更新刷新时间
    private void upDateRefreshTime() {
        String label = DateUtils.formatDateTime(getActivity(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        lvProduct.getLoadingLayoutProxy().setLastUpdatedLabel(label);// 加上时间
    }

}
