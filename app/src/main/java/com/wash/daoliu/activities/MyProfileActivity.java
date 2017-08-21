package com.wash.daoliu.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.CloanUserRecord;
import com.wash.daoliu.model.User;
import com.wash.daoliu.model.UserProfile;
import com.wash.daoliu.net.ReqCallBack;
import com.wash.daoliu.net.WCOKHttpClient;
import com.wash.daoliu.utility.EducationEnum;
import com.wash.daoliu.utility.IncomeEnum;
import com.wash.daoliu.utility.JobEnum;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.StringUtils;
import com.wash.daoliu.utility.TextUtils;
import com.wash.daoliu.view.UISwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by zhengpingli on 2017/6/26.
 */

public class MyProfileActivity extends BaseActivity {

    TextView tv_username, tv_education, tv_job, tv_income;
    EditText et_username;
    EditText et_idcard;

    UISwitchButton toggleButton;

    UserProfile userProfile;
    String education = "";
    String job = "";
    String income = "";
    int hasCreditCard = 0; // 没有信用卡


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initView();
        initData();
    }

    public void initView() {
        ((TextView) findViewById(R.id.title)).setText(R.string.my_profile);


        //获取焦点
        View view = findViewById(R.id.title);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        et_username = (EditText) findViewById(R.id.et_username);
        et_idcard = (EditText) findViewById(R.id.et_idcard);
        tv_education = (TextView) findViewById(R.id.tv_education);
        tv_job = (TextView) findViewById(R.id.tv_job);
        tv_income = (TextView) findViewById(R.id.tv_income);
        findViewById(R.id.tv_education).setOnClickListener(this);
        findViewById(R.id.tv_job).setOnClickListener(this);
        findViewById(R.id.tv_income).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);

        toggleButton = (UISwitchButton) findViewById(R.id.toggleButton);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hasCreditCard = isChecked ? 1 : 0;
            }
        });
    }


    public void initData() {


        updateProfileData();

    }

    public void updateProfileData() {
        UserProfile userProfile = User.getUserInstance().getUserProfile();
        if (!StringUtils.isNullOrEmpty(userProfile.getUserName())) {
            et_username.setText(userProfile.getUserName());
        }
        et_username.setText(StringUtils.isNullOrEmpty(userProfile.getUserName()) ? getResources().getString(R.string.input_your_name) : userProfile.getUserName());
        et_idcard.setText(StringUtils.isNullOrEmpty(userProfile.getIdcard()) ? getResources().getString(R.string.idcard_hint) : userProfile.getIdcard());
        tv_education.setText(StringUtils.isNullOrEmpty(userProfile.getEducation()) ?
                getResources().getString(R.string.education_hint) :
                EducationEnum.getEducationEnumByEnName(userProfile.getEducation()).getZhName());
        tv_job.setText(StringUtils.isNullOrEmpty(userProfile.getJob()) ?
                getResources().getString(R.string.job_hint) :
                JobEnum.getJobEnumByEnName(userProfile.getJob()).getZhName());
        tv_income.setText(StringUtils.isNullOrEmpty(userProfile.getIncome()) ?
                getResources().getString(R.string.income_hint) :
                IncomeEnum.getIncomeEnumByEnName(userProfile.getIncome()).getZhName());
        toggleButton.setChecked(userProfile.getHasCreditCard() == 1 ? true : false);
    }


    @Override
    public void onClick(View v) {
        Intent nIntent;
        switch (v.getId()) {
            case R.id.back_btn:
                //  获取验证码;
                finish();
                break;
            case R.id.btn_save:
                //  获取验证码;
                updateProfile();
                break;
            case R.id.tv_education:

                OptionPicker picker = new OptionPicker(this, getResources().getStringArray(R.array.education_array));

                picker.setCanceledOnTouchOutside(false);
                picker.setDividerRatio(WheelView.DividerConfig.FILL);
                picker.setShadowColor(Color.RED, 40);
                picker.setSelectedIndex(1);
                picker.setCycleDisable(true);
                picker.setTextSize(16);
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        tv_education.setText(item);
                        education = getResources().getStringArray(R.array.education_value_array)[index];
                    }
                });
                picker.show();
                break;
            case R.id.tv_job:
                OptionPicker jobPicker = new OptionPicker(this, getResources().getStringArray(R.array.job_array));
                jobPicker.setCanceledOnTouchOutside(false);
                jobPicker.setDividerRatio(WheelView.DividerConfig.FILL);
                jobPicker.setShadowColor(Color.RED, 40);
                jobPicker.setSelectedIndex(1);
                jobPicker.setCycleDisable(true);
                jobPicker.setTextSize(16);
                jobPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        tv_job.setText(item);
                        job = getResources().getStringArray(R.array.job_value_array)[index];
                    }
                });
                jobPicker.show();
                break;
            case R.id.tv_income:
                OptionPicker incomePicker = new OptionPicker(this, getResources().getStringArray(R.array.income_array));
                incomePicker.setCanceledOnTouchOutside(false);
                incomePicker.setDividerRatio(WheelView.DividerConfig.FILL);
                incomePicker.setShadowColor(Color.RED, 40);
                incomePicker.setSelectedIndex(1);
                incomePicker.setCycleDisable(true);
                incomePicker.setTextSize(16);
                incomePicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        tv_income.setText(item);
                        income = getResources().getStringArray(R.array.income_value_array)[index];
                    }
                });
                incomePicker.show();
                break;


        }
    }

    public void getProfile() {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        WCOKHttpClient.getOkHttpClient(this).requestAsyn(LTNConstants.ACCESS_URL.GET_USER_PROFILE_URL,
                WCOKHttpClient.TYPE_GET, mReqParams,
                new ReqCallBack<JSONObject>() {
                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                userProfile =
                                        new Gson().fromJson(dataObj.toString(), UserProfile.class);
                                if (userProfile != null) {
                                    User.getUserInstance().setUserProfile(userProfile);
                                    updateProfileData();

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

    public static HashMap<String, String> jsonToMap(String jsonString) {
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonString, type);
    }

    public void updateProfile() {
        HashMap<String, String> mReqParams = new HashMap();
        mReqParams.put(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        mReqParams.put(LTNConstants.SESSION_KEY, LTNApplication.getInstance().getSessionKey());

        UserProfile userProfile = User.getUserInstance().getUserProfile();
        userProfile.setEducation(education);
        userProfile.setJob(job);
        userProfile.setIncome(income);
        userProfile.setHasCreditCard(hasCreditCard);
        userProfile.setUserName(et_username.getText().toString().trim());
        userProfile.setIdcard(et_idcard.getText().toString().trim());
        userProfile.setClientType(LTNConstants.CLIENT_TYPE_MOBILE);
        userProfile.setSessionKey(LTNApplication.getInstance().getSessionKey());

        WCOKHttpClient.getOkHttpClient(this).requestPostAsyn(LTNConstants.ACCESS_URL.UPDATE_USER_PROFILE_URL, mReqParams,
                new Gson().toJson(userProfile).toString(),
                new ReqCallBack<JSONObject>() {
                    @Override
                    public void onReqSuccess(JSONObject jsonObject) {
                        try {
                            if (jsonObject.optInt(LTNConstants.RESULT_CODE) == 0) {
                                JSONObject dataObj = (JSONObject) jsonObject.get(LTNConstants.DATA);

                                UserProfile userProfileNew =
                                        new Gson().fromJson(dataObj.toString(), UserProfile.class);
                                if (userProfileNew != null) {
                                    User.getUserInstance().setUserProfile(userProfileNew);
                                    updateProfileData();
                                    showToast(getResources().getString(R.string.profile_completed));
                                    MyProfileActivity.this.finish();
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

    @Override
    public void onResume() {
        super.onResume();
        getProfile();
    }
}