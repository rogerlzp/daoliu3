package com.wash.daoliu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.imageloader.ImageLoaderProxy;
import com.wash.daoliu.model.Banner;

public class BannerViewAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<Banner> squareadvs;
	private OnBannerClick onBannerClick = null;
	public BannerViewAdapter(Context context, ArrayList<Banner> squareadvs,OnBannerClick onBannerClick) {
		this.context = context;
		this.onBannerClick = onBannerClick;
		this.squareadvs = squareadvs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	public ImageView instantiateItem(View arg0, final int arg1) {
		ImageView imageView = new ImageView(LTNApplication.getInstance());
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		//    					imageView.setImageResource(R.drawable.banner);
		final Banner banner  = squareadvs.get(arg1%squareadvs.size());
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBannerClick.onClickBanner(banner.linkUrl);
			}
		});
		ImageLoaderProxy.getInstance().displayImage(context, squareadvs.get(arg1 % squareadvs.size()).bannerUrl, imageView, R.drawable.divide_normal);
		((ViewPager) arg0).addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}
	public interface OnBannerClick{
		void onClickBanner(String linkUrl);
	}
}
