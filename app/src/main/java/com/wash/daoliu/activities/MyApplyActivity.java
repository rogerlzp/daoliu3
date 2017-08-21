package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.fragment.ApplyListFragment;
import com.wash.daoliu.fragment.BaseFragment;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengpingli on 2017/6/25.
 */

public class MyApplyActivity extends BaseActivity {

    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private PagerSlidingTabStrip tabs = null;
    private ViewPager mPager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        initView();
    }

    public void initView() {

        ((TextView)findViewById(R.id.title)).setText("我的申请");

        ApplyListFragment tbzListFragment = new ApplyListFragment();
        Bundle b1 = new Bundle();
        b1.putString(LTNConstants.OPERATION_TYPE, LTNConstants.OPERATION_APPLY);
        tbzListFragment.setArguments(b1);
        fragments.add(tbzListFragment);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.homePager);
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(new MyApplyActivity.MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(mPager);
        tabs.setIndicatorColor(ContextCompat.getColor(this, R.color.tab_color));
        tabs.setTextSize(16);

        findViewById(R.id.back_btn).setOnClickListener(this);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private final String[] TITLES = {"已申请"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getPageIconResId(int position) {
            return R.drawable.transparent_background;
        }

        @Override
        public int getPageTextColor(int position) {
            // TODO Auto-generated method stub
            if (mPager.getCurrentItem() == position) {
                return ContextCompat.getColor(MyApplyActivity.this, R.color.button_color);
            }
            return 0xff666666;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }
    }


}
