package com.wash.daoliu.utility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.LTNWebPageActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jiangqingbo on 2014/10/27.
 */
public class TextUtils {

    /**
     * 设置TextView某个区段字符串的颜色值
     *
     * @param textView
     * @param text
     * @param startIndex
     * @param endIndex
     * @param colorResId
     */
    public static void setTextColor(TextView textView, String text, int startIndex, int endIndex, int colorResId) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(colorResId), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
    }

    /**
     * 设置TextView某个区段字符串的颜色值
     *
     * @param text
     * @param startIndex
     * @param endIndex
     * @param colorResId
     * @return
     */
    public static SpannableString getTextColor(String text, int startIndex, int endIndex, int colorResId) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(colorResId), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    public static boolean isDigitsOnly(String name) {
        return android.text.TextUtils.isDigitsOnly(name);
    }

    public static void initAgreement(TextView textView, Context mContext, String mAgreement, String agreementUrl, String preText1, String postText) {
        SpannableString spannableString = new SpannableString(mAgreement);
        LTNClickableSpan clickableSpan = new LTNClickableSpan(mContext, spannableString, agreementUrl);
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(preText1);
        textView.append(spannableString);
        textView.append(postText);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setFocusable(false);
        textView.setClickable(false);
        //  textView.setFocusable(false);

    }

    private static class LTNClickableSpan extends ClickableSpan {

        private SpannableString spannableString;
        private Context context;
        private String agreementUrl;

        public LTNClickableSpan(Context _context, SpannableString _spannableString, String _agreementUrl) {
            this.spannableString = _spannableString;
            this.context = _context;
            this.agreementUrl = _agreementUrl;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.recommend));

        }

        @Override
        public void onClick(View widget) {

            Intent intent = new Intent(this.context, LTNWebPageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(LTNWebPageActivity.BUNDLE_URL, agreementUrl);
            bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, spannableString.toString());
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }


    // 处理时间, 将时间更改为状态 hh:mm:ss 的形式.
    public static String convertDateToFromat2(String date) {
        // 当前时间
        java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dfs = new SimpleDateFormat("hh:mm:ss");
        String formatString = null;
        if (date == null) {
            return null;
        }
        try {
//            System.currentTimeMillis()
            formatString = format1.format(dfs.parse(date));
        } catch (Exception e) {
        }
        return formatString.substring(11, formatString.length());
    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    // 处理时间, 将时间更改为状态 YYYY-MM-DD 的形式.
    public static String convertDateToFromat1(String date) {
        // 当前时间

        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
        String formatString = null;
        if (date == null) {
            return null;
        }
        try {
//            System.currentTimeMillis()

            formatString = DateFormat.getDateInstance().format(dfs.parse(date));
        } catch (Exception e) {
        }
        return formatString;
    }

    // 处理时间, 将时间更改为状态 YYYY-MM-DD 的形式.
    public static String replaceStarToString(String str1) {
        // 当前时间

        String formatString = null;
        if (str1 == null || str1.length() < 11) {
            return null;
        }
        try {
            formatString = str1.substring(0,3)+ "****"+str1.substring(7, str1.length());
//            System.currentTimeMillis()
        } catch (Exception e) {
        }
        return formatString;
    }


    /**
     * 将double的值更新为小数点两位
     * 如果d < 0, 则不加0
     *
     * @param str1
     * @return
     */

    public static String formatDoubleValue(double d) {
        if (d == 0) {
            return "0.00";
        }
        String result = String.valueOf(d);
        try {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
            result = df.format(d);
        } catch (Exception e) {
        }
        if (d < 0) { //负数. > -1 的时候改变,,前面加上小数点,否则不需要改变
            if (d > -1) {
                result = result.substring(0, 1) + "0" + result.substring(1, result.length());
            }
        } else if (d < 1) {  // 0<d < 1 的时候改变
            result = "0" + result;
        }
        return result;
    }

    public static String formatDoubleValue(String str) {
        try {
            double d = Double.parseDouble(str);
            return formatDoubleValue(d);
        } catch (Exception e) {

        }
        return str;
    }

    public static String formatDoubleValueWithUnit(double d) {
        if (d == 0) {
            return "0.00";
        }
        String result = String.valueOf(d);
        try {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
            if (d >= 10000) {
                d /= 10000;
                result = df.format(d) + "万";
            } else {
                result = df.format(d) + "元";
            }

        } catch (Exception e) {

        }
        if (d < 1) {
            result = "0" + result;
        }
        return result;
    }


    public static String formatDoubleValueWithUnit(String str) {
        boolean startWithYuan = false;
        String yuanSign = "￥";
        if (str.startsWith(yuanSign)) {
            str = str.substring(1, str.length());
            startWithYuan = true;
        }
        try {
            double d = Double.parseDouble(str);
            String resultValue = formatDoubleValueWithUnit(d);
            return startWithYuan ? (yuanSign + resultValue.substring(0, resultValue.length() - 1)) : resultValue;
        } catch (Exception e) {
        }
        return startWithYuan ? yuanSign + str : str;
    }

    public static String getTodayDate() {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
        String formatString = null;

        try {
            formatString = DateFormat.getDateInstance().format(System.currentTimeMillis());
        } catch (Exception e) {
        }
        return formatString;
    }
    //判断数字是否超过2位小数
    public static boolean checkNum(String numStr){
        boolean flag = true;
            String[] strs = numStr.split("\\.");
            if(strs.length>1&&strs[1].length()>2){
                flag = false;
        }
        return flag;

    }

    public static void main(String args){

    }
}
