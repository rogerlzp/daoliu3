package com.wash.daoliu.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.fragment.BaseFragment;
import com.wash.daoliu.fragment.IncomeListFragment;
import com.wash.daoliu.view.PagerSlidingTabStrip;

public class IncomeDetailActivity extends BaseActivity implements OnClickListener{

	public static final int TYPE_DEATIL = 0; 
	public static final String REFRESH_HOME_DATA = "refreshHomeData";
	private PagerSlidingTabStrip tabs = null;
	private ViewPager mPager = null;
	private boolean isBaidu = false;
	private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_income_detail);
		initView();
	}

	private void initView() {
		((TextView)findViewById(R.id.title)).setText("收益明细");
		findViewById(R.id.back_btn).setOnClickListener(this);

		IncomeListFragment unIncomeListFaragment = new IncomeListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", 0);
		unIncomeListFaragment.setArguments(bundle);
		fragments.add(unIncomeListFaragment);

		IncomeListFragment incomeListFragment = new IncomeListFragment();
		Bundle bundle1 = new Bundle();
		bundle1.putInt("type", 1);
		incomeListFragment.setArguments(bundle1);
		fragments.add(incomeListFragment);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mPager = (ViewPager) findViewById(R.id.homePager);
		mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(mPager);
		tabs.setIndicatorColor(getResources().getColor(R.color.button_color));
		tabs.setTextSize(16);
	}
	public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{

		private final String[] TITLES = { "待收收益", "已收收益"};
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
			if(mPager.getCurrentItem()==position){
				return ContextCompat.getColor(IncomeDetailActivity.this,R.color.button_color);
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

	private long mExitTime;


}
