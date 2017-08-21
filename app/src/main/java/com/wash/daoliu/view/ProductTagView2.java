package com.wash.daoliu.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.ViewUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiajia on 2016/1/9.
 */
public class ProductTagView2 extends LinearLayout {
    private Map<String, Integer> tyMap = null;
    private Map<String, Integer> otherMap = null;
    private Map<String, Integer> colorMap = null;
    private final String XC = "限次";
    private final String XS = "新手";
    private final String KZ = "可转";
    private final String XE = "限额";
    private Context context = null;
    private int defaultTagBg = R.drawable.tag_xe_bg;
    private int defaultTagTextBg = R.color.tag_xe_color;
    public ProductTagView2(Context context) {
        this(context, null);
    }

    public ProductTagView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        tyMap = new HashMap<String, Integer>();
        tyMap.put(XC, R.drawable.tag_ty_xs_bg);
        tyMap.put(XS, R.drawable.tag_ty_xs_bg);

        otherMap = new HashMap<String, Integer>();
        otherMap.put(XS, R.drawable.tag_xs_bg);
        otherMap.put(XC, R.drawable.tag_xc_bg);
        otherMap.put(KZ, R.drawable.tag_kz_bg);
        otherMap.put(XE, R.drawable.tag_xe_bg);


        colorMap = new HashMap<String,Integer>();
        colorMap.put(XS,R.color.tag_xs_color);
        colorMap.put(XC,R.color.tag_xc_color);
        colorMap.put(KZ,R.color.tag_kz_color);
        colorMap.put(XE,R.color.tag_xe_color);
        setOrientation(HORIZONTAL);
    }

    public ProductTagView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTag(String productTags, String type) {
        removeAllViews();
        if (TextUtils.isEmpty(productTags) || TextUtils.isEmpty(type)) {
            setVisibility(View.INVISIBLE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }
        String[] productTagArray = productTags.split(";");
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = ViewUtils.dip2px(context, 5);
        for (String tag : productTagArray) {
            if(TextUtils.isEmpty(tag)){
                continue;
            }
            TextView imageView = new TextView(context);
            imageView.setPadding(ViewUtils.dip2px(context,8),ViewUtils.dip2px(context,1),ViewUtils.dip2px(context,8),ViewUtils.dip2px(context,1));
            imageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            imageView.setText(tag);
            imageView.setBackgroundResource(getTagImageRes(type, tag));
            imageView.setTextColor(getTagTextColorRes(type,tag));
            addView(imageView, params);
        }
    }

    //获取tag背景
    private int getTagImageRes(String type, String tag) {
        int tagRes = 0;
        if (type.equals(LTNConstants.PRODUCT.TYPE_TYB)) {
            tagRes = R.drawable.tag_ty_xs_bg;
        } else {
            //如果有有tag 表错的时候,用默认来显示
            if (otherMap.containsKey(tag)) {
                tagRes = otherMap.get(tag);
            } else {
                tagRes = defaultTagBg;
            }
        }

        return tagRes;
    }
    //获取tag背景
    private int getTagTextColorRes(String type, String tag) {
        int tagRes = 0;
        if (type.equals(LTNConstants.PRODUCT.TYPE_TYB)) {
            tagRes = R.color.white;
        } else {
            //如果有有tag 表错的时候,用默认来显示
            if (colorMap.containsKey(tag)) {
                tagRes = colorMap.get(tag);
            } else {
                tagRes = defaultTagTextBg;
            }
        }

        return ContextCompat.getColor(context,tagRes);
    }




    public void setListTag(String productTags, String type) {
        removeAllViews();
        if (TextUtils.isEmpty(productTags) || TextUtils.isEmpty(type)) {
            setVisibility(View.INVISIBLE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }
        String[] productTagArray = productTags.split(";");
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = ViewUtils.dip2px(context, 5);
        for (String tag : productTagArray) {
            if(TextUtils.isEmpty(tag)){
                continue;
            }
            TextView imageView = new TextView(context);
            imageView.setPadding(ViewUtils.dip2px(context,8),ViewUtils.dip2px(context,1),ViewUtils.dip2px(context,8),ViewUtils.dip2px(context,1));
            imageView.setText(tag);
            imageView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            imageView.setBackgroundResource(getListTagImageRes(type, tag));
            imageView.setTextColor(getListTagTextColorRes(type, tag));
            addView(imageView, params);
        }
    }
    public void setListDarkTag(String productTags, String type) {
        removeAllViews();
        if (TextUtils.isEmpty(productTags) || TextUtils.isEmpty(type)) {
            setVisibility(View.INVISIBLE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }
        String[] productTagArray = productTags.split(";");
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = ViewUtils.dip2px(context, 5);
        for (String tag : productTagArray) {
            if(TextUtils.isEmpty(tag)){
                continue;
            }
            TextView imageView = new TextView(context);
            imageView.setPadding(ViewUtils.dip2px(context,8),ViewUtils.dip2px(context,1),ViewUtils.dip2px(context,8),ViewUtils.dip2px(context,1));
            imageView.setText(tag);
            imageView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            imageView.setBackgroundResource(R.drawable.tag_dark_bg);
            imageView.setTextColor(ContextCompat.getColor(context, R.color.label_grey1));
            addView(imageView, params);
        }
    }

    //获取tag背景
    private int getListTagImageRes(String type, String tag) {
        if (otherMap.containsKey(tag)) {
            return otherMap.get(tag);
        }
        // Log.e("TAG", "======" + tag);
//            鸟币；券
        return defaultTagBg;
    }
    //获取tag文字颜色值
    private int getListTagTextColorRes(String type, String tag) {
        if (colorMap.containsKey(tag)) {
            return ContextCompat.getColor(context, colorMap.get(tag));
        }
        // Log.e("TAG", "======" + tag);
//            鸟币；券
        return ContextCompat.getColor(context,defaultTagTextBg);
    }
}
