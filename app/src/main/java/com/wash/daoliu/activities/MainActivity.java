package com.wash.daoliu.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.tendcloud.tenddata.TCAgent;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.base.BaseFragmentActivity;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.fragment.LTNAccountFragment;
import com.wash.daoliu.fragment.LTNHomeFragment;
import com.wash.daoliu.fragment.LTNInvestimentFragment;
import com.wash.daoliu.fragment.LTNMoreFragment;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.ActivityUtils;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.MyImageButtonWithText;

/**
 * 主页面，加载5个tab和很多fragment的页面
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    private MyImageButtonWithText idHome;
    private MyImageButtonWithText idWorkOrder;
    private MyImageButtonWithText idScan;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constant.LoginParams.LOGIN_TO_MYACCOUNT)) {
                itClick(2);
//                if (intent.getBooleanExtra(LTNConstants.IS_REGISTER, false)) {
//                    Utils.showRealNameWarn(MainActivity.this);
//                }
            } else if (action.equals(Constant.LoginParams.LOGIN_TO_PRODUCT_LIST)) {
                itClick(1);
            }
        }
    };
    public int currentSelected = 0;

    private Fragment fragment;

    public static int currentItem = 0;

    private Fragment fragmentOld;

    private LTNHomeFragment homeFragment;
    private LTNInvestimentFragment investimentFragment;
    private LTNAccountFragment accountFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        itClick(0);
        registerBroadcastReceiver();
        //初始化拿到MainActivity实例，用于SessionKey过期跳转
        LTNHttpClient.getLTNHttpClient(this);
//        Utils.checkVersion(this);
        if (getIntent().getBooleanExtra(LTNConstants.IS_REGISTER, false)) {
            Utils.showRealNameWarn(this);
        }
        if (getIntent().getBooleanExtra(LTNConstants.FROM_FORGET_PASSWORD, false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        if (getIntent().getBooleanExtra(LTNConstants.IS_FROM_RESERVE, false)) {
            //跳转到我的账户页面
            itClick(2);
        }


        if (getIntent().getBooleanExtra(LTNConstants.SESSION_KEY_TIME_OUT, false)) {
            ViewUtils.showWarnDialog(MainActivity.this, "您的登录环境已经过期，请重新登录", "登录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        if(intent.getBooleanExtra(LTNConstants.SESSION_KEY_TIME_OUT,false)){
//            Intent i = new Intent(this,LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(i);
//        }
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.LoginParams.LOGIN_TO_MYACCOUNT);
        intentFilter.addAction(Constant.LoginParams.LOGIN_TO_PRODUCT_LIST);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    /**
     * 初始化组件
     */
    private void initView() {
        idHome = (MyImageButtonWithText) findViewById(R.id.id_home);
        idWorkOrder = (MyImageButtonWithText) findViewById(R.id.id_workorder);
        idScan = (MyImageButtonWithText) findViewById(R.id.id_scan);
        idHome.setOnClickListener(this);
        idWorkOrder.setOnClickListener(this);
        idScan.setOnClickListener(this);

    }

    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.id_home:
                if (currentSelected != 0) {
                    itClick(0);
                }
                break;
            case R.id.id_workorder:
                if (currentSelected != 1) {
                    itClick(1);
                }
                break;
            case R.id.id_scan:
                if (currentSelected != 2) {
                    if (TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
                        Intent intent = new Intent(this, LoginActivity.class);
                        Bundle b = new Bundle();
                        b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_MYACCOUNT);
                        intent.putExtras(b);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    } else {
                        itClick(2);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void itClick(int position) {

        fragmentOld = fragment;
        currentSelected = position;
        currentItem = position;
        setItBgColor(currentSelected);

        switch (position) {
            case 0:
                if (null == homeFragment) {
                    homeFragment = new LTNHomeFragment();
                    initAddFragment(R.id.lay_content_container, homeFragment);
                }
                fragment = homeFragment;
                break;
            case 1:
                if (null == investimentFragment) {
                    investimentFragment = new LTNInvestimentFragment();
                    initAddFragment(R.id.lay_content_container, investimentFragment);
                }
                fragment = investimentFragment;
                break;
            case 2:
                if (null == accountFragment) {
                    accountFragment = new LTNAccountFragment();
                    initAddFragment(R.id.lay_content_container, accountFragment);
                }
                fragment = accountFragment;
                break;
            default:
                break;
        }
        showFragmentAndHideOldFragment(fragment, fragmentOld);
    }

    private void setItBgAllToGray() {
        idHome.setImageResource(R.drawable.tab_home_normal);
        idWorkOrder.setImageResource(R.drawable.tab_list_normal);
        idScan.setImageResource(R.drawable.tab_account_normal);

        idHome.setTextViewTextColor(ContextCompat.getColor(this, R.color.text_gray_6));
        idWorkOrder.setTextViewTextColor(ContextCompat.getColor(this, R.color.text_gray_6));
        idScan.setTextViewTextColor(ContextCompat.getColor(this, R.color.text_gray_6));

    }

    private void setItBgColor(int currentSelected) {

        setItBgAllToGray();
        switch (currentSelected) {
            case 0:
                idHome.setImageResource(R.drawable.tab_home_selected);
                idHome.setTextViewTextColor(ContextCompat.getColor(this, R.color.main_yellow_color));
                break;
            case 1:
                idWorkOrder.setImageResource(R.drawable.tab_list_selected);
                idWorkOrder.setTextViewTextColor(ContextCompat.getColor(this, R.color.main_yellow_color));
                break;
            case 2:
                idScan.setImageResource(R.drawable.tab_account_selected);
                idScan.setTextViewTextColor(ContextCompat.getColor(this, R.color.main_yellow_color));
                break;
            default:
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * 初始化添加fragment
     */
    public void initAddFragment(int layoutId, Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layoutId, fragment);
        transaction.commitAllowingStateLoss();
    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getIntent().getBooleanExtra("isFromHaoCai", false)) {
                setResult(RESULT_OK);
                finish();
                return true;
            }
            if ((System.currentTimeMillis() - mExitTime) > 2000) {

                Toast.makeText(this, "再按一次退出神马贷款", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                ActivityUtils.finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LTNConstants.ZAJIDAN_SUCCESS: // 从充值成功后跳转过来
                // 跳转到URL界面
                itClick(0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Talking data
        TCAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Talking data
        TCAgent.onPause(this);
    }
}
