package com.wash.daoliu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.utility.ViewUtils;

/**
 * 自定义ImageButton，模拟ImageButton，并在其下方显示文字
 * 提供Button的部分接口
 * Created by bobo on 2015/1/25.
 */
@SuppressLint("ResourceAsColor")
public class MyImageButtonText extends LinearLayout {

    // ----------------private attribute-----------------------------
    private ImageView mButtonImage = null;
    private TextView mButtonText = null;

    private TextView dotText = null;

    public MyImageButtonText(Context context, int ivbg, int textResId) {
        super(context);
        mButtonImage = new ImageView(context);
        mButtonText = new TextView(context);

        float scale = context.getResources().getDisplayMetrics().density;

        int width = 60;
        int height = 60;
        if (scale < 2) {
            width = ViewUtils.dip2px(context, 50);
            height = ViewUtils.dip2px(context, 50);
        } else {
            width = ViewUtils.dip2px(context, 60);
            height = ViewUtils.dip2px(context, 60);
        }

        RelativeLayout parentLayout = new RelativeLayout(context);
        parentLayout.setLayoutParams(new LayoutParams(width, height));

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setBackgroundResource(ivbg);
        relativeLayout.setLayoutParams(new LayoutParams(width, height));

        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rl.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

    //    setImageResource(imageResId);
//        mButtonImage.setPadding(0, 0, 0, 0);
        mButtonImage.setLayoutParams(rl);
        relativeLayout.addView(mButtonImage);
        parentLayout.addView(relativeLayout);

        dotText = new TextView(context);
        RelativeLayout.LayoutParams dotLp = new RelativeLayout.LayoutParams(ViewUtils.dip2px(context, 10), ViewUtils.dip2px(context, 10));
        dotLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        dotText.setLayoutParams(dotLp);
        dotText.setBackgroundResource(R.drawable.stroke_circle_red_bg);
        setDotTextVisible(View.GONE);
        parentLayout.addView(dotText);

        setText(textResId);
        mButtonText.setPadding(0, 0, 0, 0);
        setTextsize(12.0f);
        setTextColor(R.color.gray_1);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 0);
        mButtonText.setLayoutParams(lp);

        //设置本布局的属性
        setClickable(true);  //可点击
        setFocusable(true);  //可聚焦
//        setBackgroundResource(R.drawable.ent_button_selector);  //布局才用普通按钮的背景
        setOrientation(LinearLayout.VERTICAL);  //横向布局
        setGravity(Gravity.CENTER);
        setPadding(0, 0, 0, 10);

        //首先添加Image，然后才添加Text
        //添加顺序将会影响布局效果
        addView(parentLayout);
        addView(mButtonText);

        LayoutParams rootLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(rootLp);

    }

    public MyImageButtonText(Context context, int ivbg, int imageResId, int textResId) {
        super(context);
        mButtonImage = new ImageView(context);
        mButtonText = new TextView(context);

        float scale = context.getResources().getDisplayMetrics().density;

        int width = 60;
        int height = 60;
        if (scale < 2) {
            width = ViewUtils.dip2px(context, 50);
            height = ViewUtils.dip2px(context, 50);
        } else {
            width = ViewUtils.dip2px(context, 60);
            height = ViewUtils.dip2px(context, 60);
        }

        RelativeLayout parentLayout = new RelativeLayout(context);
        parentLayout.setLayoutParams(new LayoutParams(width, height));

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setBackgroundResource(ivbg);
        relativeLayout.setLayoutParams(new LayoutParams(width, height));

        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rl.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        setImageResource(imageResId);
//        mButtonImage.setPadding(0, 0, 0, 0);
        mButtonImage.setLayoutParams(rl);
        relativeLayout.addView(mButtonImage);
        parentLayout.addView(relativeLayout);

        dotText = new TextView(context);
        RelativeLayout.LayoutParams dotLp = new RelativeLayout.LayoutParams(ViewUtils.dip2px(context, 10), ViewUtils.dip2px(context, 10));
        dotLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        dotText.setLayoutParams(dotLp);
        dotText.setBackgroundResource(R.drawable.stroke_circle_red_bg);
        setDotTextVisible(View.GONE);
        parentLayout.addView(dotText);

        setText(textResId);
        mButtonText.setPadding(0, 0, 0, 0);
        setTextsize(12.0f);
        setTextColor(R.color.gray_1);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 0);
        mButtonText.setLayoutParams(lp);

        //设置本布局的属性
        setClickable(true);  //可点击
        setFocusable(true);  //可聚焦
//        setBackgroundResource(R.drawable.ent_button_selector);  //布局才用普通按钮的背景
        setOrientation(LinearLayout.VERTICAL);  //横向布局
        setGravity(Gravity.CENTER);
        setPadding(0, 0, 0, 10);

        //首先添加Image，然后才添加Text
        //添加顺序将会影响布局效果
        addView(parentLayout);
        addView(mButtonText);

        LayoutParams rootLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(rootLp);

    }

    // ----------------public method-----------------------------
    /*
    * setImageResource方法
    */
    public void setImageResource(int resId) {
        mButtonImage.setImageResource(resId);
    }

    /*
     * setText方法
     */
    public void setText(int resId) {
        mButtonText.setText(resId);
    }

    public void setText(CharSequence buttonText) {
        mButtonText.setText(buttonText);
    }

    public void setTypeface(Typeface typeface) {
        mButtonText.setTypeface(typeface);
    }

    public void setTextsize(float textsize) {
        mButtonText.setTextSize(textsize);
    }

    /*
     * setTextColor方法
     */
    public void setTextColor(int color) {
        mButtonText.setTextColor(color);
    }

    public void setDotTextVisible(int msgPointVisible) {
        dotText.setVisibility(msgPointVisible);
    }

    public TextView getDotText() {
        return dotText;
    }

}