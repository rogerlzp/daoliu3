package com.wash.daoliu.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.adapter.CouponAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.library.PullToRefreshBase;
import com.wash.daoliu.library.PullToRefreshListView;
import com.wash.daoliu.model.Coupon;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/12/29.
 */
public class CouponsActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView>,View.OnClickListener{

    private static final String TAG = CouponsActivity.class.getSimpleName();
    private int page = 0;
    private CouponAdapter mAdapter = null;
    PullToRefreshListView plvCoupons;
    private ArrayList<Coupon> mCoupons = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupons);
        initView();
        onPullDownToRefresh(plvCoupons);
        if(getIntent().getBooleanExtra(LTNConstants.IS_REGISTER,false)){
            Utils.showRealNameWarn(this);
        }
    }
    public void initView() {
        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView)findViewById(R.id.title)).setText("理财金券");
        plvCoupons = (PullToRefreshListView)findViewById(R.id.lv_coupons);
        plvCoupons.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ListView listView = plvCoupons.getRefreshableView();
        listView.setDivider(ContextCompat.getDrawable(this, R.drawable.transparent));
        listView.setDividerHeight(ViewUtils.dip2px(this, 10));
        listView.setSelector(ContextCompat.getDrawable(this,R.drawable.transparent));
        plvCoupons.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 0;
        getData(true);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//        page++;
//        getData(false);
    }

    //增加listview later


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
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

        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.COUPON_LIST, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            //   HashMap<String, Object> mProduct = new HashMap<String, Object>();

                            if( mCoupons==null){
                                mCoupons = new ArrayList<Coupon>();
                            }
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                               // Log.e("TAG",jsonObject.toString());
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.COUPONS);
                                ArrayList<Coupon> coupons = new Gson().fromJson(resultArray.toString(), new TypeToken<ArrayList<Coupon>>() {
                                }.getType());
                                if (coupons != null) {
                                    if(isPullDownRefresh){
                                        mCoupons.clear();
                                    }
                                    mCoupons.addAll(coupons);
                                    if (mAdapter == null) {
                                        mAdapter = new CouponAdapter(CouponsActivity.this, mCoupons);
                                        plvCoupons.setAdapter(mAdapter);
                                    } else {
                                        mAdapter.updateCoupons(mCoupons);
                                    }
                                }
                                if(mCoupons.size()==0){
                                    plvCoupons.setVisibility(View.INVISIBLE);
                                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                }else{
                                    plvCoupons.setVisibility(View.VISIBLE);
                                    findViewById(R.id.empty_view).setVisibility(View.INVISIBLE);
                                }
                            }else{
                               // Log.e("TAG", jsonObject.optString(LTNConstants.RESULT_MESSAGE));
                            }
                        } catch (JSONException je) {
                           // Log.e("TAG",je.toString());
                        }
                        plvCoupons.onRefreshComplete();
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        plvCoupons.onRefreshComplete();
                    }
                });

    }

}
