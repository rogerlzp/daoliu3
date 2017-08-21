package com.wash.daoliu.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.GuideActivity;
import com.wash.daoliu.activities.LoginActivity;
import com.wash.daoliu.activities.MainActivity;
import com.wash.daoliu.application.LTNApplication;

import java.util.List;

/**
 * Created by rogerlzp on 15/11/23.
 */
public class GuideViewPagerAdapter extends PagerAdapter {
    // 界面列表
    private List<View> views;
    private GuideActivity activity;

    public GuideViewPagerAdapter(List<View> views, GuideActivity activity) {
        this.views = views;
        this.activity = activity;
    }

    // 销毁arg1位置的界面
    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));
    }

    @Override
    public void finishUpdate(ViewGroup arg0) {
    }

    // 获得当前界面数
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    // 初始化arg1位置的界面
    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        ((ViewPager) arg0).addView(views.get(arg1), 0);
        if (arg1 == views.size() - 1) {
            Button mStartImageButton = (Button) arg0.findViewById(R.id.iv_start);
            mStartImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    LTNApplication.getInstance().setIsFirstLogin();
                    enterMainActivity();
                }

            });
        }
        return views.get(arg1);
    }

    private void enterMainActivity() {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();

    }

    private void enterLoginActivity() {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.finish();
    }

    // 判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup arg0) {
    }
}
