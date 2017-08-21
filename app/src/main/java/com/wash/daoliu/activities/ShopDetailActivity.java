package com.wash.daoliu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wash.daoliu.R;
import com.wash.daoliu.adapter.ShopServiceAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.eventtype.ShopServiceReserveEvent;
import com.wash.daoliu.imageloader.ImageLoaderProxy;
import com.wash.daoliu.model.Shop;
import com.wash.daoliu.model.ShopService;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhengpingli on 2017/3/27.
 */

public class ShopDetailActivity extends BaseActivity implements View.OnClickListener {

    TextView title;
    TextView tv_shop_name;
    RatingBar rb_rating;
    TextView tv_status;
    TextView tv_address;
    TextView tv_distance;
    Button btn_reserve;
    TextView tv_comment_num;
    TextView tv_load_comments;
    ImageView placeholder;

    String shopId, shopName, shopLocation, primaryPic, workTime;
    int waitingTime, distance;
    double latitude, longitude;
    float rating;

    private int page = 0;


    public ShopServiceAdapter mShopServiceAdapter;
    private ArrayList<ShopService> mShopServices = null;
    ProgressDialog mProgressdialog;

    RecyclerView rv_shopservice;
    RecyclerView rv_shopservice2;
    private List<String> mDatas;
    private HomeAdapter mAdapter;

    private Shop mShop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        initView();


        Bundle bundle = getIntent().getExtras();

        String shopJson = getIntent().getStringExtra(LTNConstants.SHOP);
        mShop = new Gson().fromJson(shopJson, Shop.class);

//        shopId = bundle.getString(LTNConstants.SHOP_ID); // 产品ID
//        //    if (bundle.getString(LTNConstants.FROM_JPUSH) == null) {
//        shopName = bundle.getString(LTNConstants.SHOP_NAME); // 产品余额
//        shopLocation = bundle.getString(LTNConstants.SHOP_LOCATION); // 产品余额
//        primaryPic = bundle.getString(LTNConstants.SHOP_PRIMARY_PIC); // 产品余额
//        shopName = bundle.getString(LTNConstants.SHOP_NAME); // 产品余额
//        workTime = bundle.getString(LTNConstants.SHOP_WORK_TIME); // 产品余额
//        waitingTime = bundle.getInt(LTNConstants.WAITING_TIME); // 产品余额
//        distance = bundle.getInt(LTNConstants.SHOP_DISTANCE); // 产品余额
//
//        latitude = bundle.getDouble(LTNConstants.LATITUDE); // 产品余额
//        longitude = bundle.getDouble(LTNConstants.LONGITUDE); // 产品余额
//        rating = bundle.getFloat(LTNConstants.SHOP_RATING); // 产品余额

        shopId = mShop.getShopId();
        shopName = mShop.getShopName();
        shopLocation = mShop.getShopLocation();
        primaryPic = mShop.getPrimaryPic();
        workTime = mShop.getWorkTime();
        waitingTime = mShop.getWaitingTime();
        distance = mShop.getDistance();
        latitude = mShop.getLatidude();
        longitude = mShop.getLongitude();
        rating = mShop.getRating();

        EventBus.getDefault().register(this);

        initData();

     //   initData2();
        //  } else {
        getShopProductDetail();
        // }


    }

    @Subscribe
    public void onEventMainThread(ShopServiceReserveEvent event) {
        Toast.makeText(this, "ShopDetailActivity", Toast.LENGTH_LONG).show();
        this.finish();
    }

    public void initView() {
        title = (TextView) findViewById(R.id.title);
        tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_comment_num = (TextView) findViewById(R.id.tv_comment_num);
        tv_load_comments = (TextView) findViewById(R.id.tv_load_comments);
        rb_rating = (RatingBar) findViewById(R.id.rb_rating);
        btn_reserve = (Button) findViewById(R.id.btn_reserve);
        placeholder = (ImageView) findViewById(R.id.placeholder);

        findViewById(R.id.back_btn).setOnClickListener(this);
        btn_reserve.setOnClickListener(this);

        rv_shopservice = (RecyclerView) findViewById(R.id.rv_shopservice);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_shopservice.setLayoutManager(linearLayoutManager);

        rv_shopservice.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mShopServiceAdapter = new ShopServiceAdapter(this);
        rv_shopservice.setAdapter(mShopServiceAdapter);


    }


    public void initData() {
        title.setText(shopName);
        tv_shop_name.setText(shopName);
        tv_address.setText(shopLocation);

        if (distance % 1000 != 0) {
            tv_distance.setText(distance / 1000 + "公里");
        } else {
            tv_distance.setText(distance + "米");
        }
        rb_rating.setRating(rating);
        ImageLoaderProxy.getInstance().displayImage(this, primaryPic, placeholder, R.drawable.ic_image_holder);
    }

    protected void initData2() {

//        ShopService shopService = new ShopService();
//        shopService.setServiceName("dsafdaff");
//        shopService.setOriginalPrice(123.9);
//        shopService.setTimeConsume(120);
//
//        mShopServices = new ArrayList<>();
//        mShopServices.add(shopService);
//
//        shopService.setServiceName("精细");
//        shopService.setOriginalPrice(13.9);
//        shopService.setTimeConsume(1200);
//        mShopServices.add(shopService);
//
//        mShopServiceAdapter.setShopServices(mShopServices);
//
//
//        mDatas = new ArrayList<String>();
//        for (int i = 'A'; i < 'z'; i++) {
//            mDatas.add("" + (char) i);
//        }
//
//        rv_shopservice2 = (RecyclerView) findViewById(R.id.rv_shopservice2);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rv_shopservice2.setLayoutManager(linearLayoutManager);
//
//        rv_shopservice2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        //  mAdapter = new HomeAdapter();
//        rv_shopservice2.setAdapter(mShopServiceAdapter);
    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ShopDetailActivity.this).inflate(R.layout.item_test, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
            }
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_reserve:
                intent = new Intent(this, ReserveActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString(LTNConstants.SHOP_ID, shopId);
                intent.putExtras(bundle1);
                startActivity(intent);
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_login:
                // 跳转到登录页面
                if (TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
                    intent = new Intent(this, LoginActivity.class);
                    Bundle b = getIntent().getExtras();
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_PRODUCT);
                    intent.putExtras(b);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;
            case R.id.btn_register:
                // 跳转到注册页面
                // TODO: 带入 产品ID
                intent = new Intent(this, RegisterActivity.class);
                Bundle b = getIntent().getExtras();
                b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_PRODUCT);
                intent.putExtras(b);
                startActivity(intent);
                break;


        }
    }

  /*  public void getShopDetail() {

        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SHOP_ID, shopId);
        if (!android.text.TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
            mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        }

        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.SHOPDETAIL_URL, WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            // 验证码错误
                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                JSONObject resultObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                Gson gson = new Gson();
                                mShop = gson.fromJson(resultObj.toString(), Shop.class);
                                //  productID = mProduct.getProductId(); // 产品ID

                                shopLocation = mShop.getShopLocation(); //
                                shopName = mShop.getShopName(); //
                                rating = mShop.getRating();
                                distance = mShop.getDistance();
                                waitingTime = mShop.getWaitingTime();
                                latitude = mShop.getLatidude();
                                longitude = mShop.getLongitude();
                                workTime = mShop.getWorkTime();
                                primaryPic = mShop.getPrimaryPic();

                                initData();
                            } else {
                            }
                        } catch (JSONException je) {
                            LogUtils.e(je.getMessage());

                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e(errorMsg);
                    }
                });
    }


*/

    public void showDialog() {
        if (mProgressdialog == null || !mProgressdialog.isShowing()) {
            mProgressdialog = new ProgressDialog(this);
            mProgressdialog.setMessage("正在加载数据");
            mProgressdialog.setIndeterminate(true);
            mProgressdialog.setCancelable(true);
            mProgressdialog.show();
        }
    }

    public void getShopProductDetail() {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.CURRENT_PAGE, String.valueOf(page));
        mReqParams.put(LTNConstants.PAGE_SIZE, String.valueOf(LTNConstants.PAGE_COUNT));
        mReqParams.put(LTNConstants.SHOP_ID, shopId);

        // TODO: 上传location

        showDialog();
        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.SHOPPRODUCT_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {

                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        if (mProgressdialog.isShowing()) {
                            mProgressdialog.cancel();
                        }
                        try {
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                if (mShopServices == null) {
                                    mShopServices = new ArrayList<ShopService>();
                                }

                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.LIST);
                                ArrayList<ShopService> shopServices = new Gson().fromJson(resultArray.toString(),
                                        new TypeToken<ArrayList<ShopService>>() {
                                        }.getType());
                                if (shopServices != null) {
                                    mShopServices.addAll(shopServices);
                                }
                                mShopServiceAdapter.setShopServices(mShopServices);
                            }
                        } catch (JSONException je) {
                            // Log.d(FRAGMENT_TAG, je.getMessage());
                        }
                        if (mProgressdialog.isShowing()) {
                            mProgressdialog.cancel();

                        }
                        // rv_shopservice.
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d(errorMsg);
                        //     dismissProgressDialog();
                        if (mProgressdialog.isShowing()) {
                            mProgressdialog.cancel();
                        }
                        // lvProduct.onRefreshComplete();
                    }
                });

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}

