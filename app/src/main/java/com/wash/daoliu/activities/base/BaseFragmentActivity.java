package com.wash.daoliu.activities.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wash.daoliu.activities.BaseActivity;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.utility.LogUtils;
import com.wash.daoliu.utility.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseFragmentActivity extends BaseActivity {

    public TextView tvTitle;
    public ImageView leftMenuButton, rightMenuButton;
    public Button rightMenuCommit;
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                LogUtils.e("ACTION_CLOSE_SYSTEM_DIALOGS");
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (LTNApplication.getInstance() != null) {
                    LTNApplication.getInstance().setIsShowGesture(true);
                }
//                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
//                    //表示按了home键,程序到了后台
////                    Toast.makeText(getApplicationContext(), "home", 1).show();
//
//
//                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
//                    //表示长按home键,显示最近使用的程序列表
//                }

            }else if(action.equals(Intent.ACTION_SCREEN_OFF)){
                LogUtils.e("ACTION_SCREEN_OFF");
                if (LTNApplication.getInstance() != null) {
                    LTNApplication.getInstance().setIsShowGesture(true);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        //初始化ActionBar
//        restoreActionBar();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeKeyEventReceiver,filter);
//        Utils.checkSession(this);
        Utils.isNetworkConnected(this);
    }


//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    public void restoreActionBar() {
//        ActionBar actionBar = getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//
//        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.MATCH_PARENT,
//                ActionBar.LayoutParams.MATCH_PARENT,
//                Gravity.LEFT);
//        View viewTitleBar = getLayoutInflater().inflate(R.layout.action_bar_title, null);
//        getActionBar().setCustomView(viewTitleBar, lp);
//        //  getActionBar().setDisplayShowHomeEnabled(false);
//        getActionBar().setDisplayShowTitleEnabled(false);
//        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getActionBar().setDisplayShowCustomEnabled(true);
//        tvTitle = (TextView) getActionBar().getCustomView().findViewById(R.id.title);
//        tvTitle.setText(getString(R.string.app_name));
//        leftMenuButton = (ImageView) getActionBar().getCustomView().findViewById(R.id.left_menu_button);
//        rightMenuButton = (ImageView) getActionBar().getCustomView().findViewById(R.id.right_menu_button);
//        rightMenuCommit = (Button) getActionBar().getCustomView().findViewById(R.id.right_menu_commit);
//
//    }


    private ProgressDialog progressDialog;

    public void showLoadingProgressDialog(Context context, String dialogText) {
        this.showProgressDialog(context, dialogText);
    }

    public void showProgressDialog(Context context, CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCanceledOnTouchOutside(false);
        }
        this.progressDialog.setMessage(message);
        if (!this.progressDialog.isShowing()) {
            this.progressDialog.show();
        }

    }

    public void dismissProgressDialog() {
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }
    }

    /**
     * 显示toast
     *
     * @param message
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 将毫秒转换成格式化时间
     *
     * @param time 时间的毫秒数
     * @return 返回按yyyy.MM.dd格式化的时间字符串
     */
    public String transMS2String(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(new Date(time));
    }

    public boolean isExist(File file) {
        return file.exists();
    }


    /**
     * 中间页面加载
     *
     * @param fragment
     */
    public void replaceFragment(int layoutId, android.support.v4.app.Fragment fragment) {
//        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.feed_detail_in, 0, 0, R.anim.feed_detail_out);
        transaction.replace(layoutId, fragment);
        transaction.commit();
    }

    /**
     * 变换显示的fragment，并自动保存fragment之前的状态。
     *
     * @param fragment    即将要显示的fragment
     * @param fragmentOld 即将要隐藏的fragment
     */
    public void showFragmentAndHideOldFragment(android.support.v4.app.Fragment fragment, android.support.v4.app.Fragment fragmentOld) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (null != fragmentOld) {
            transaction.hide(fragmentOld);
        }


//        if (getFragmentManager().findFragmentByTag(LTNHomeFragment.FRAGMENT_TAG) == null) {
//            transaction.add(new LTNHomeFragment(), LTNHomeFragment.FRAGMENT_TAG);
//        }
//        if (getFragmentManager().findFragmentByTag(LTNInvestimentFragment.FRAGMENT_TAG) == null) {
//            transaction.add(new LTNHomeFragment(), LTNInvestimentFragment.FRAGMENT_TAG);
//        }
//        if (getFragmentManager().findFragmentByTag(LTNAccountFragment.FRAGMENT_TAG) == null) {
//            transaction.add(new LTNHomeFragment(), LTNAccountFragment.FRAGMENT_TAG);
//        }
//        if (getFragmentManager().findFragmentByTag(LTNMoreFragment.FRAGMENT_TAG) == null) {
//            transaction.add(new LTNHomeFragment(), LTNMoreFragment.FRAGMENT_TAG);
//        }
/*
        if (getFragmentManager().findFragmentByTag(LTNHomeFragment.FRAGMENT_TAG) == null) {
            transaction.add(new LTNHomeFragment(), LTNHomeFragment.FRAGMENT_TAG);
        }
        if (getFragmentManager().findFragmentByTag(LTNInvestimentFragment.FRAGMENT_TAG) == null) {
            transaction.add(new LTNHomeFragment(), LTNInvestimentFragment.FRAGMENT_TAG);
        }
        if (getFragmentManager().findFragmentByTag(LTNAccountFr agment.FRAGMENT_TAG) == null) {
            transaction.add(new LTNHomeFragment(), LTNAccountFragment.FRAGMENT_TAG);
        }
        if (getFragmentManager().findFragmentByTag(LTNMoreFragment.FRAGMENT_TAG) == null) {
            transaction.add(new LTNHomeFragment(), LTNMoreFragment.FRAGMENT_TAG);
        }
        */

        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Talking data
//        TCAgent.onResume(this);
//        if (LTNApplication.getInstance() != null && LTNApplication.getInstance().isShowGesture()) {
//            LTNApplication.getInstance().setIsShowGesture(false);
//            if (LTNApplication.getInstance().getGesturePassword() != null) {
//                Intent intent = new Intent(this, GestureVerifyActivity.class);
//                startActivity(intent);
//            }
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Talking data
//        TCAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHomeKeyEventReceiver);
    }
}
