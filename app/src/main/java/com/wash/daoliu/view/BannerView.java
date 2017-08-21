package com.wash.daoliu.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.LTNWebPageActivity;
import com.wash.daoliu.adapter.BannerViewAdapter;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.Banner;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.ViewUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BannerView extends LinearLayout {
    private ViewPager adViewPager;
    private ViewGroup group;
    //	private List<ImageView> bannerViewList = new ArrayList<ImageView>();
    private int pageCount;
    private Timer bannerTimer;
    private Handler handler;
    private BannerTimerTask bannerTimerTask;
    private ArrayList<Banner> squareadvs = new ArrayList<Banner>();
    private ImageView[] imageViews;
    private Context context;
    private int startCount = 40000;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.banner_view, this);
        initView(context);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                adViewPager.setCurrentItem(msg.what, !(msg.what == startCount * squareadvs.size()));
            }
        };
        bannerTimer = new Timer();
    }

    private void initView(final Context context) {
        RelativeLayout layout = (RelativeLayout) this
                .findViewById(R.id.view_pager_content);
        adViewPager = (ViewPager) this.findViewById(R.id.viewPager1);
        group = (ViewGroup) findViewById(R.id.iv_image);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        LayoutParams params = (LayoutParams) layout
                .getLayoutParams();
        params.height = (int) (((double) dm.widthPixels / (double) 640 * (double) 252));
        layout.setLayoutParams(params);
    }

    public void setBannerLayoutParams(int height) {
        // TODO Auto-generated method stub
        RelativeLayout layout = (RelativeLayout) this
                .findViewById(R.id.view_pager_content);
        adViewPager = (ViewPager) this.findViewById(R.id.viewPager1);
        group = (ViewGroup) findViewById(R.id.iv_image);
        LayoutParams params = (LayoutParams) layout
                .getLayoutParams();
        params.height = height;
        layout.setLayoutParams(params);
    }

    public void bannerStartPlay() {
        if (squareadvs.size() == 0) {
            return;
        }
        if (bannerTimer != null) {
            if (bannerTimerTask != null)
                bannerTimerTask.cancel();
            bannerTimerTask = new BannerTimerTask();
            bannerTimer.schedule(bannerTimerTask, 5000, 5000);
        }
    }

    public void bannerStopPlay() {
        if (squareadvs.size() == 0) {
            return;
        }
        if (bannerTimerTask != null)
            bannerTimerTask.cancel();
    }

    class BannerTimerTask extends TimerTask {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message msg = new Message();
            if (squareadvs.size() <= 1)
                return;
            int currentIndex = adViewPager.getCurrentItem();
            if (currentIndex == Integer.MAX_VALUE - 1)
                msg.what = startCount * squareadvs.size();
            else
                msg.what = currentIndex + 1;

            handler.sendMessage(msg);
        }

    }

    public void initAdv(ArrayList<Banner> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            squareadvs.clear();
//			bannerViewList.clear();
            group.removeAllViews();
            squareadvs.addAll(arrayList);
            ImageView imageView;
//			for (int i = 0; i < squareadvs.size(); i++) {
//				imageView = new ImageView(LTNApplication.getInstance());
//				imageView.setScaleType(ScaleType.FIT_XY);
//				//    					imageView.setImageResource(R.drawable.banner);
//				final Banner banner  = squareadvs.get(i);
//				imageView.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						if(!TextUtils.isEmpty(banner.linkUrl)){
//							Intent intent = new Intent(context, LTNWebPageActivity.class);
//							Bundle bundle = new Bundle();
//							bundle.putString(LTNWebPageActivity.BUNDLE_URL,banner.linkUrl);
//							bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "");
//							bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
//							intent.putExtra(LTNConstants.BUNDLE_OBJ, bundle);
//							context.startActivity(intent);
//						}
//					}
//				});
//
//				bannerViewList.add(imageView);
//			}

            pageCount = squareadvs.size();
            if (pageCount == 1) {
                group.setVisibility(View.GONE);
            } else {
                group.setVisibility(View.VISIBLE);
            }
            imageViews = new ImageView[pageCount];
            for (int i = 0; i < pageCount; i++) {
                LayoutParams margin = new LayoutParams(
                        ViewUtils.dip2px(LTNApplication.getInstance(), 10),
                        ViewUtils.dip2px(LTNApplication.getInstance(), 10));
                margin.setMargins(ViewUtils.dip2px(LTNApplication.getInstance(), 10), 0, 0, 0);
                imageView = new ImageView(LTNApplication.getInstance());
                imageView.setLayoutParams(new LayoutParams(ViewUtils.dip2px(LTNApplication.getInstance(), 15), ViewUtils.dip2px(LTNApplication.getInstance(), 15)));
                imageViews[i] = imageView;
                if (i == 0) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.page_indicator_focused);
                } else {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
                group.addView(imageViews[i], margin);
            }
            BannerViewAdapter adapter = new BannerViewAdapter(LTNApplication.getInstance(), squareadvs, new BannerViewAdapter.OnBannerClick() {
                @Override
                public void onClickBanner(String linkUrl) {
                    if (!TextUtils.isEmpty(linkUrl)) {
                        Intent intent = new Intent(context, LTNWebPageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(LTNWebPageActivity.BUNDLE_URL, linkUrl);
                        bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "");
                        bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }
            });
            adViewPager.setAdapter(adapter);
//			adViewPager.setOnSingleTouchListener(this);
            adViewPager.setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onPageSelected(int arg0) {
                    for (int i = 0; i < imageViews.length; i++) {

                        if (arg0 % squareadvs.size() != i) {
                            imageViews[i]
                                    .setBackgroundResource(R.drawable.page_indicator_unfocused);
                        } else {
                            imageViews[arg0 % squareadvs.size()]
                                    .setBackgroundResource(R.drawable.page_indicator_focused);
                        }
                    }
                }

            });
            adViewPager.setCurrentItem(startCount * squareadvs.size());
            bannerStartPlay();
        } else {
            setVisibility(View.GONE);
        }
    }

//	@Override
//	public void onSingleTouch() {
//		// TODO Auto-generated method stub
//		String url = squareadvs.get(adViewPager.getCurrentItem()).getCs_adv_url();
//		if(TextUtils.isEmpty(url)){
//			return;
//		}
//		if(url.startsWith("http")){
//			Intent intent = new Intent(context,WebHasTitleActivity.class);
//			intent.putExtra("url", url);
//			context.startActivity(intent);
//		}else{
//			String type = squareadvs.get(adViewPager.getCurrentItem()).getCs_type();
//			if(!MyApplication.getmSharedPreference().getBoolean("isLogin",false)){
//				Bundle b = new Bundle();
//				if("0".equals(type)){
//					b.putInt("type", Main_HomeFragment.TYPE_BRAND_PRODUCT_DETAIL);
//					b.putString("id", squareadvs.get(adViewPager.getCurrentItem()).getCs_id());
//				}else if("1".equals(type)){
//					b.putInt("type", Main_HomeFragment.TYPE_DEATIL);
//					b.putString("id", squareadvs.get(adViewPager.getCurrentItem()).getCs_id());
//				}
//				onLogin.onLogin(b);
//				return;
//			}
//			if(type!=null){
//				switch (Integer.parseInt(type)) {
//				case 0:
//					Intent intent = new Intent(context, BrandProductDetailActivity.class);
//					intent.putExtra("c_id", squareadvs.get(adViewPager.getCurrentItem()).getCs_id());
//					context.startActivity(intent);
//					break;
//				case 1:
//					intent = new Intent(context, MaterialDetailActivity.class);
//					intent.putExtra("c_id", squareadvs.get(adViewPager.getCurrentItem()).getCs_id());
//					context.startActivity(intent);
//					break;
//					
//				default:
//					break;
//				}
//			}
//		}
//	}
}

