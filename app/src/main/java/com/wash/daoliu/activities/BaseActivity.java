package com.wash.daoliu.activities;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tendcloud.tenddata.TCAgent;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.User;
import com.wash.daoliu.utility.ActivityUtils;
import com.wash.daoliu.utility.LogUtils;



/**
 * Created by rogerlzp on 15/11/23.
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener {


    public BaseOnClickEffectiveListener baseOnClickListener;
    public User user;

    //   protected  ImageLoader mImageLoader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstance().pushActivity(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        baseOnClickListener = new BaseOnClickEffectiveListener();

        user = LTNApplication.getInstance().getCurrentUser();
        // 添加SessionKey验证
//        if (Utils.isNetworkConnected(this)) {
//            Utils.checkSession(this);
//        }

    }

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(this, layoutResID, null);
        ObjectAnimator.ofFloat(view, "alpha", 0.0F, 1.0F)
                .setDuration(1000)
                .start();
        super.setContentView(view);
    }

    private class BaseOnClickEffectiveListener extends OnClickEffectiveListener {

        @Override
        public void onClickEffective(View view) {
            clickEffective(view);
        }
    }

    public void clickEffective(View view) {
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Talking data
        TCAgent.onResume(this);

        // jpush
     //   JPushInterface.onResume(this);

        if (!TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
            if (LTNApplication.getInstance() != null && LTNApplication.getInstance().isShowGesture()) {
                LTNApplication.getInstance().setIsShowGesture(false);
                LogUtils.e(""+(System.currentTimeMillis() - LTNApplication.getInstance().getBackground_time()));
                if (LTNApplication.getInstance().getBackground_time()!=0&&(System.currentTimeMillis() - LTNApplication.getInstance().getBackground_time() > 30 * 1000)) {
                    if (LTNApplication.getInstance().getGesturePassword() != null) {
                        Intent intent = new Intent(this, GestureVerifyActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                }
                LTNApplication.getInstance().setBackground_time(0);
            }
        }
//        Bundle b = getIntent().getExtras();
//        if (b != null && b.getBoolean(LTNConstants.IS_REGISTER, false)) {
//            ViewUtils.showWarnDialog(this, getString(R.string.realname_text), getString(R.string.next),
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(BaseActivity.this, UserAuthActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Talking data
        TCAgent.onPause(this);

        // jpush
        //JPushInterface.onPause(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private ProgressDialog progressDialog;

    public void showLoadingProgressDialog(Context context, String dialogText) {
        this.showProgressDialog(context, dialogText);
    }

    public void showProgressDialog(Context context, CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setCancelable(true);
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

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
