package com.wash.daoliu.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.wash.daoliu.R;

/**
 * Created by zhengpingli on 2017/8/19.
 */

public class LoadingDialog extends Dialog {
    private TextView detail_tv;
    private RotateAnimation mAnim;

    public LoadingDialog(Context context) {
        super(context, R.style.MyDialogStyle);
        init();
    }

    public LoadingDialog(Context context, int isTrans) {
        super(context, isTrans);
        init();
    }

    private void init() {
        setContentView(R.layout.loading);
        detail_tv = (TextView) findViewById(R.id.loading_text);
    }

    private void initAnim() {
        // mAnim = new RotateAnimation(360, 0, Animation.RESTART, 0.5f,
        // Animation.RESTART, 0.5f);
        mAnim = new RotateAnimation(0, 360, Animation.RESTART, 0.5f,
                Animation.RESTART, 0.5f);
        mAnim.setDuration(2000);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
    }

    @Override
    public void show() {// 在要用到的地方调用这个方法
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
//		mAnim.cancel();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            detail_tv.setText("正在加载");
        } else {
            detail_tv.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }

    public static void dismissDialog(LoadingDialog loadingDialog) {
        if (null == loadingDialog) {
            return;
        }
        loadingDialog.cancel();
    }
}