package com.wash.daoliu.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.fragment.BaseFragment;
import com.wash.daoliu.imageloader.ImageLoaderProxy;

import com.wash.daoliu.model.Cloan;

import com.wash.daoliu.model.CloanApplyStep;
import com.wash.daoliu.model.CloanUserRecord;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.StringUtils;
import com.wash.daoliu.view.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhengpingli on 2017/6/24.
 */

public class CloanDetailActivity extends BaseActivity {

    public static final String TAG = AnxintouActivity.class.getSimpleName();

    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private PagerSlidingTabStrip tabs = null;

    private Cloan currentCloan;
    private ViewPager mPager = null;

    TextView tv_cloan_name, tv_apply_customer, tv_condition1, tv_desc1, tv_desc;
    TextView tv_rate_range, tv_rate_unit, tv_date_range, tv_range_unit, tv_loan_range;
    ImageView iv_logo;

    Button btn_save;

    LinearLayout ll_process;

    ArrayList<CloanApplyStep> cloanApplyStepArrayList = new ArrayList<CloanApplyStep>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloan_detail);

        String cloanJson = getIntent().getStringExtra(LTNConstants.CLOAN);
        currentCloan = new Gson().fromJson(cloanJson, Cloan.class);

        initView();
        initData();

        if (!TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
            addRecord(LTNConstants.OPERATION_VISIT);
        }

    }

    private void initView() {
        //  if(StringUtils.isNullOrEmpty(currentCloan.getCloanName()));
        ((TextView) findViewById(R.id.title)).setText(StringUtils.isNullOrEmpty(currentCloan.getCloanName()) ? "融时贷-极速下款" : currentCloan.getCloanName());
        findViewById(R.id.back_btn).setOnClickListener(this);
        tv_cloan_name = (TextView) findViewById(R.id.tv_cloan_name);
        tv_apply_customer = (TextView) findViewById(R.id.tv_apply_customer);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        tv_condition1 = (TextView) findViewById(R.id.tv_condition1);

        tv_desc1 = (TextView) findViewById(R.id.tv_desc1);

        findViewById(R.id.back_btn).setOnClickListener(this);

        ll_process = (LinearLayout) findViewById(R.id.ll_process);

        tv_rate_range = (TextView) findViewById(R.id.tv_rate_range);
        tv_rate_unit = (TextView) findViewById(R.id.tv_rate_unit);
        tv_date_range = (TextView) findViewById(R.id.tv_date_range);
        tv_range_unit = (TextView) findViewById(R.id.tv_range_unit);
        tv_loan_range = (TextView) findViewById(R.id.tv_loan_range);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

    }


    public void initData() {

        if (!StringUtils.isNullOrEmpty(currentCloan.getLoanRange())) {
            tv_loan_range.setText(currentCloan.getLoanRange() + "元");
        }
        if (!StringUtils.isNullOrEmpty(currentCloan.getDateRange())) {
            tv_date_range.setText(currentCloan.getDateRange() + "天");
        }

        if (currentCloan.getDayRate() != 0) {
            tv_rate_unit.setText("利率范围(天)");
//            tv_rate_range.setText(StringUtils.double2String(currentCloan.getDayRate() * 100) + "%");
            tv_rate_range.setText(currentCloan.getRateRange());
        } else if (currentCloan.getMonthRate() != 0) {
            tv_rate_unit.setText("利率范围(月)");
            //  tv_rate_range.setText(StringUtils.double2String(currentCloan.getMonthRate() * 100) + "%");
            tv_rate_range.setText(currentCloan.getRateRange());
        }

        if (!StringUtils.isNullOrEmpty(currentCloan.getDescription())) {
            tv_desc.setText(currentCloan.getDescription());
        }

        if (!StringUtils.isNullOrEmpty(currentCloan.getCloanName())) {
            tv_cloan_name.setText(currentCloan.getCompany() + "-" + currentCloan.getCloanName());
        }
        tv_apply_customer.setText("" + currentCloan.getApplyCustomer());
        if (!StringUtils.isNullOrEmpty(currentCloan.getApplyCondition())) {
            StringBuffer sb = new StringBuffer();
            String[] strArr = currentCloan.getApplyCondition().split(";");

            boolean firstFlag = true;
            int i = 1;
            for (String str1 : strArr) {
                if (str1.length() > 1) {  //有效字符串
                    if (!firstFlag) {
                        sb.append("\n");
                    }
                    sb.append(i + "." + str1);
                    firstFlag = false;
                }
                i++;
            }
            tv_condition1.setText(sb.toString());
        }

        if (!StringUtils.isNullOrEmpty(currentCloan.getApplyDescription())) {
            StringBuffer sb = new StringBuffer();
            String[] strArr = currentCloan.getApplyDescription().split(";");

            boolean firstFlag = true;
            int i = 1;
            for (String str1 : strArr) {
                if (str1.length() > 1) {  //有效字符串
                    if (!firstFlag) {
                        sb.append("\n");
                    }
                    sb.append(i + "." + str1);
                    firstFlag = false;
                }
                i++;
            }
            tv_desc1.setText(sb.toString());
        }


        ImageLoaderProxy.getInstance().displayImage(this, currentCloan.getCloanLogo(), iv_logo, R.drawable.ic_image_holder);

        getApplyStep();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_save:
                //用户没有登录，需要先登录
                if (TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {

                    Intent intent = new Intent(this, LoginActivity.class);
                    Bundle b = getIntent().getExtras();
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_CLOAN_PRODUCT);
                    intent.putExtras(b);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    return;
                }

                addRecord(LTNConstants.OPERATION_APPLY);
                if (!TextUtils.isEmpty(currentCloan.getH5link())) {
                    Intent intent = new Intent(this, LTNWebPageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(LTNWebPageActivity.BUNDLE_URL, currentCloan.getH5link());
                    bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, currentCloan.getCompany() + "-" + currentCloan.getCloanName());
                    bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
                    intent.putExtras(bundle);
                    this.startActivity(intent);
                }

                break;
        }

    }


    public void addRecord(String operationType) {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());
        mReqParams.put(LTNConstants.CLOAN_NO, currentCloan.getCloanNo());
        mReqParams.put(LTNConstants.OPERATION_TYPE, operationType);

        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.ADD_USER_CLOAN_URL,
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


    public void getApplyStep() {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.CLOAN_NO, currentCloan.getCloanNo());

        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.CLOAN_STEP_LIST_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {
                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                if (cloanApplyStepArrayList == null) {
                                    cloanApplyStepArrayList = new ArrayList<CloanApplyStep>();
                                }
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                JSONArray resultArray = (JSONArray) dataObj.get(LTNConstants.CLOAN_STEP_LIST);
                                ArrayList<CloanApplyStep> myInvests =
                                        new Gson().fromJson(resultArray.toString(), new TypeToken<ArrayList<CloanApplyStep>>() {
                                        }.getType());
                                if (myInvests != null) {
                                    cloanApplyStepArrayList = myInvests;
                                    drawCloanStep();
                                }
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


    private void drawCloanStep() {
        boolean firstImage = true;
        if (cloanApplyStepArrayList != null && cloanApplyStepArrayList.size() != 0) {
            for (CloanApplyStep cloanApplyStep : cloanApplyStepArrayList) {
                if (!firstImage) {
                    drawImage();
                }
                drawText(cloanApplyStep.stepName, cloanApplyStep.enStepName);
                firstImage = false;

            }
        }
    }

    private void drawText(String textData, String enStep) {
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 2);

        TextView t1 = new TextView(this);
        t1.setLayoutParams(textLayoutParams);
        t1.setCompoundDrawablePadding(8);
        t1.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        t1.setText(textData);

        Drawable drawable = setDrawable(enStep);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        t1.setCompoundDrawables(null, drawable, null, null);
        t1.setTextSize(10);
        ll_process.addView(t1, textLayoutParams);
    }

    private Drawable setDrawable(String textData) {
        Drawable drawable;
        switch (textData) {
            case "rlsb": //人脸识别
                drawable = getResources().getDrawable(R.drawable.icon_rlsb);
                break;
            case "sfrz": //身份认证
                drawable = getResources().getDrawable(R.drawable.icon_sfrz);
                break;
            case "sjrz": //手机认证
                drawable = getResources().getDrawable(R.drawable.icon_sjrz);
                break;
            case "xykzd": //信用卡账单
                drawable = getResources().getDrawable(R.drawable.icon_xykzd);
                break;
            case "tbrz": //淘宝认证
                drawable = getResources().getDrawable(R.drawable.icon_tbrz);
                break;
            case "yhkrz": //银行卡认证
                drawable = getResources().getDrawable(R.drawable.icon_yhkrz);
                break;

            case "gsxx": //公司信息
                drawable = getResources().getDrawable(R.drawable.icon_gsxx);
                break;
            case "jbxx": //基本信息
                drawable = getResources().getDrawable(R.drawable.icon_jbxx);
                break;
            case "zmsq": //芝麻授权
                drawable = getResources().getDrawable(R.drawable.icon_zmsq);
                break;

            case "yyssq": //运营商授权
                drawable = getResources().getDrawable(R.drawable.icon_yyssq);
                break;
            case "lxrxx": //联系人信息
                drawable = getResources().getDrawable(R.drawable.icon_lxrxx);
                break;

            default:
                drawable = getResources().getDrawable(R.drawable.ic_image_holder);
                break;


        }
        return drawable;

    }

    private void drawImage() {

        LinearLayout.LayoutParams iconLayoutParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        ImageView t1 = new ImageView(this);
        t1.setLayoutParams(iconLayoutParams);
        t1.setImageResource(R.drawable.icon_arrow);
        ll_process.addView(t1, iconLayoutParams);

    }

}
