package com.wash.daoliu.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.fragment.BaseFragment;
import com.wash.daoliu.fragment.ParterEarningsFragment;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rogerlzp on 15/12/30.
 */
public class ParterEarnings extends BaseActivity implements View.OnClickListener {


    private static final String TAG = ParterEarnings.class.getSimpleName();
    private PagerSlidingTabStrip tabs = null;
    private ViewPager mPager = null;
    private boolean isBaidu = false;
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();

    @SuppressLint("ResourceAsColor")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_invest);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title)).setText("累计奖励");
        findViewById(R.id.back_btn).setOnClickListener(this);
        ParterEarningsFragment tbzListFragment = new ParterEarningsFragment();
        Bundle b1 = new Bundle();
        b1.putString(LTNConstants.TYPE, "0");
        tbzListFragment.setArguments(b1);
        fragments.add(tbzListFragment);

        ParterEarningsFragment cyzListFragment = new ParterEarningsFragment();
        Bundle b2 = new Bundle();
        b2.putString(LTNConstants.TYPE, "1");
        cyzListFragment.setArguments(b2);
        fragments.add(cyzListFragment);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.homePager);
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(mPager);
        tabs.setIndicatorColor(getResources().getColor(R.color.tab_color));
        tabs.setTextSize(16);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private final String[] TITLES = {"我邀请的好友", "好友邀请的好友"};

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
                return ContextCompat.getColor(ParterEarnings.this, R.color.button_color);
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


