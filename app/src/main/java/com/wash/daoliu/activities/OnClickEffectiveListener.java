package com.wash.daoliu.activities;

import android.view.View;

/**
 * 防止用户在1秒之内重复点击事件
 * Created by bobo on 2015/6/24.
 */
public abstract class OnClickEffectiveListener implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 1000;

    private long lastClickTime = 0;

    @Override
    public void onClick(View view) {

//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
//            lastClickTime = currentTime;
            onClickEffective(view);
//        }
    }

    public abstract void onClickEffective(View view);

}
