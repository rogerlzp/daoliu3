package com.wash.daoliu.activities;

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
import com.wash.daoliu.fragment.MyInvestListFragment;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rogerlzp on 15/12/30.
 */
public class MyInvestActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = MyInvestActivity.class.getSimpleName();
    private PagerSlidingTabStrip tabs = null;
    private ViewPager mPager = null;
    private boolean isBaidu = false;
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private int currentPage = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_invest);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentPage = bundle.getInt(LTNConstants.CURRENT_PAGE, 0);
        }
        initView();

    }

    private void initView() {
        ((TextView) findViewById(R.id.title)).setText("我的投资");
        findViewById(R.id.back_btn).setOnClickListener(this);

        MyInvestListFragment tbzListFragment = new MyInvestListFragment();
        Bundle b1 = new Bundle();
        b1.putString(LTNConstants.STATUS, "1");
        tbzListFragment.setArguments(b1);
        fragments.add(tbzListFragment);

        MyInvestListFragment cyzListFragment = new MyInvestListFragment();
        Bundle b2 = new Bundle();
        b2.putString(LTNConstants.STATUS, "2");
        cyzListFragment.setArguments(b2);
        fragments.add(cyzListFragment);


        MyInvestListFragment yhkListFragment = new MyInvestListFragment(); //还款中
        Bundle b3 = new Bundle();
        b3.putString(LTNConstants.STATUS, "4");
        yhkListFragment.setArguments(b3);
        fragments.add(yhkListFragment);

        MyInvestListFragment hkzListFragment = new MyInvestListFragment();  //已还款
        Bundle b4 = new Bundle();
        b4.putString(LTNConstants.STATUS, "3");
        hkzListFragment.setArguments(b4);
        fragments.add(hkzListFragment);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.homePager);
        mPager.setOffscreenPageLimit(3);

        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(mPager);

        tabs.setIndicatorColor(ContextCompat.getColor(this, R.color.tab_color));
        tabs.setTextSize(16);
        mPager.setCurrentItem(currentPage);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private final String[] TITLES = {"投标中", "持有中", "还款中", "已还款"};

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
                return ContextCompat.getColor(MyInvestActivity.this, R.color.button_color);
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


