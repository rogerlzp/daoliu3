package com.wash.daoliu.activities;

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
import com.wash.daoliu.fragment.PartnerCountFragment;
import com.wash.daoliu.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class PartnerCountActivity extends BaseActivity implements OnClickListener{

	public static final int TYPE_DEATIL = 0; 
	public static final String REFRESH_HOME_DATA = "refreshHomeData";
	private PagerSlidingTabStrip tabs = null;
	private ViewPager mPager = null;
	private boolean isBaidu = false;
	private List<PartnerCountFragment> fragments = new ArrayList<PartnerCountFragment>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_income_detail);
		initView();
	}
	private void initView() {
		((TextView)findViewById(R.id.title)).setText("好友统计");
		findViewById(R.id.back_btn).setOnClickListener(this);

		PartnerCountFragment friends = new PartnerCountFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", 0);
		friends.setArguments(bundle);
		fragments.add(friends);

		PartnerCountFragment friendss = new PartnerCountFragment();
		Bundle bundle1 = new Bundle();
		bundle1.putInt("type", 1);
		friendss.setArguments(bundle1);
		fragments.add(friendss);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mPager = (ViewPager) findViewById(R.id.homePager);
		mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(mPager);
		tabs.setIndicatorColor(getResources().getColor(R.color.tab_color));
		tabs.setTextSize(16);
//		mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//			@Override
//			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//				fragments.get(position).refresh();
//			}
//
//			@Override
//			public void onPageSelected(int position) {
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int state) {
//
//			}
//		});
	}
	public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{

		private final String[] TITLES = { "我推荐的好友", "好友推荐好友"};
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
				return ContextCompat.getColor(PartnerCountActivity.this, R.color.button_color);
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
