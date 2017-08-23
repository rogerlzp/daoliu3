package com.wash.daoliu.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.makeramen.roundedimageview.RoundedImageView;
import com.wash.daoliu.R;
import com.wash.daoliu.imageloader.ImageLoaderProxy;
import com.wash.daoliu.utility.ViewUtils;

/**
 * Created by zhengpingli on 2017/8/19.
 */

public class AdPopUpWindow extends PopupWindow {

    Context mContext;
    private View mConentView;


    private float mShowAlpha = 0.6f;
    private Drawable mBackgroundDrawable;

    private ImageView iv_ad;
    private ImageView iv_close;
    private String imageViewUrl;


    WindowManager.LayoutParams layoutParams;
    private int mHeight = 400;
    private int mWidth = 200;

    //观察者模式
    public interface OnAdClickedListener {
        void onAdClicked();
    }

    private OnAdClickedListener listener;

    public void setOnClickedListener(OnAdClickedListener listener) {
        this.listener = listener;
    }

    public AdPopUpWindow(final Activity context, String imageViewUrl, int height, int width, WindowManager.LayoutParams layoutParams) {
        this.mContext = context;
        this.imageViewUrl = imageViewUrl;
        this.layoutParams = layoutParams;
        mHeight = height;
        mWidth = width;
        initBasePopupWindow();

        setContentView();
    }

    /**
     * 初始化BasePopupWindow的一些信息
     */
    private void initBasePopupWindow() {
        //       setAnimationStyle(R.style.AppTheme);
        setHeight(ViewUtils.dip2px(mContext, mHeight));
        setWidth(ViewUtils.dip2px(mContext, mWidth));
        setOutsideTouchable(true);  //默认设置outside点击无响应
        setFocusable(true);


    }


    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
        if (touchable) {
            if (mBackgroundDrawable == null) {
                mBackgroundDrawable = new ColorDrawable(0xffffff);
            }
            super.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            super.setBackgroundDrawable(null);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        mBackgroundDrawable = background;
        setOutsideTouchable(isOutsideTouchable());
    }

    //  @Override
    public void setContentView() {

        mConentView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow, null);

        iv_ad = (ImageView) mConentView.findViewById(R.id.iv_ad);
        iv_close = (ImageView) mConentView.findViewById(R.id.iv_close);
        ImageLoaderProxy.getInstance().displayRoundedImage(mContext, this.imageViewUrl, iv_ad, R.drawable.icon_placeholder);

        iv_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAdClicked();
                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mConentView != null) {
            mConentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            super.setContentView(mConentView);
            addKeyListener(mConentView);
        }


    }

    /**
     * 为窗体添加outside点击事件
     */
    private void addKeyListener(View contentView) {
        if (contentView != null) {
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            contentView.setOnKeyListener(new View.OnKeyListener() {

                //   @Override
                public boolean onKey(View view, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            dismiss();
                            return true;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }


    /**
     * 控制窗口背景的不透明度
     */
    private void setWindowBackgroundAlpha(float alpha, boolean changed) {
        Window window = ((Activity) getContext()).getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
        if (changed) {
            ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

    }


    public Context getContext() {
        return mContext;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        showAnimator().start();
    }

    @Override
    public void dismiss() {
        dismissAnimator().start();
        super.dismiss();


        // dismissAnimator().end();

        //   setBackgroundDrawable(null);

        // set back
        //   setAnimationStyle(0);
//        Window window = ((Activity) getContext()).getWindow();
//        window.setAttributes(this.layoutParams);
    }

    /**
     * 窗口显示，窗口背景透明度渐变动画
     */
    private ValueAnimator showAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mShowAlpha);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha, true);
            }
        });
        animator.setDuration(360);
        return animator;
    }

    /**
     * 窗口隐藏，窗口背景透明度渐变动画
     */
    private ValueAnimator dismissAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(mShowAlpha, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha, false);
            }
        });
        animator.setDuration(320);
        return animator;
    }


}
