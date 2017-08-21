package com.wash.daoliu.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wash.daoliu.R;

/**
 * Created by jiajia on 2016/2/19.
 */
public class TipsPopupWindowUtils {
    private Context mContext = null;
    public TipsPopupWindowUtils(){
    }
    public static void showPopupView(Context context,View view,String content,int x,int y){
        View contentView =  LayoutInflater.from(context).inflate(R.layout.tip_popup_view,null);
        TextView tipTextView = (TextView)contentView.findViewById(R.id.tip_text);
        tipTextView.setText(content);
        PopupWindow mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.transparent));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(view,x,y);
    }
}
