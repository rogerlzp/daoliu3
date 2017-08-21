package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.model.Coupon;


import java.util.ArrayList;

/**
 * Created by rogerlzp on 15/12/29.
 */
public class CouponAdapter extends BaseAdapter {
    public ArrayList<Coupon> mCoupons = null;
    public Context ctx;

    public CouponAdapter(Context context) {
        this.ctx = context;
    }

    public CouponAdapter(Context context, ArrayList<Coupon> _mCoupons) {
        this.ctx = context;
        mCoupons = _mCoupons;
    }

    public void updateCoupons(ArrayList<Coupon> _mCoupons) {
        mCoupons = _mCoupons;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.coupon_item, null);
            holder = new ViewHolder();
            holder.ll2 = (LinearLayout) convertView.findViewById(R.id.ll2);
            holder.tv_coupon_name = (TextView) convertView.findViewById(R.id.tv_coupon_name);
            holder.tv_coupon_condition1 = (TextView) convertView.findViewById(R.id.tv_coupon_condition1);
            holder.tv_coupon_deadline = (TextView) convertView.findViewById(R.id.tv_coupon_deadline);
            holder.iv_coupon_status = (ImageView) convertView.findViewById(R.id.iv_coupon_status);
            holder.tv_coupon_value = (TextView) convertView.findViewById(R.id.tv_coupon_value);
            holder.tv_coupon_usage = (TextView) convertView.findViewById(R.id.tv_coupon_usage);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Coupon couponData = mCoupons.get(position);
        if (couponData.getStatus().equals("GQ")) {
            holder.ll2.setBackgroundResource(R.drawable.icon_coupon_sx);
            holder.iv_coupon_status.setImageResource(R.drawable.watermark_invalid);

            holder.tv_coupon_name.setTextColor(ctx.getResources().getColor(R.color.text_color3));
            holder.tv_coupon_condition1.setTextColor(ctx.getResources().getColor(R.color.text_color3));
            holder.tv_coupon_deadline.setTextColor(ctx.getResources().getColor(R.color.text_color3));
        } else {

            holder.tv_coupon_name.setTextColor(ctx.getResources().getColor(R.color.text_color2));
            holder.tv_coupon_condition1.setTextColor(ctx.getResources().getColor(R.color.text_color1));
            holder.tv_coupon_deadline.setTextColor(ctx.getResources().getColor(R.color.text_color1));

            if (couponData.getActivityType().equals("投资返现")) {
                holder.ll2.setBackgroundResource(R.drawable.icon_coupon_xj);
            } else if (couponData.getActivityType().equals("体验抵扣")) {
                holder.ll2.setBackgroundResource(R.drawable.icon_coupon_ty);
            } else if (couponData.getActivityType().equals("加息券")) {
                holder.ll2.setBackgroundResource(R.drawable.icon_coupon_jxq);
            } else {
                holder.ll2.setBackgroundResource(R.drawable.icon_coupon_xj);
            }
            if (couponData.getStatus().equals("YX")) {
                holder.iv_coupon_status.setImageResource(R.drawable.transparent);
            } else if (couponData.getStatus().equals("SYZ")) {
                holder.iv_coupon_status.setImageResource(R.drawable.watermark_complete);
                holder.ll2.setBackgroundResource(R.drawable.icon_coupon_sx);
                holder.tv_coupon_name.setTextColor(ctx.getResources().getColor(R.color.label_grey1));
                holder.tv_coupon_condition1.setTextColor(ctx.getResources().getColor(R.color.label_grey1));
                holder.tv_coupon_deadline.setTextColor(ctx.getResources().getColor(R.color.label_grey1));

            } else {//作废
                holder.iv_coupon_status.setImageResource(R.drawable.transparent);
            }
        }
        holder.tv_coupon_name.setText(couponData.getCouponName());

        // 加息券用%, 其他的用 ￥
        if (couponData.getActivityType().equals("加息券")) {
            holder.tv_coupon_value.setText(couponData.getAmount() + "%");
        } else {
            holder.tv_coupon_value.setText("￥" + couponData.getAmount());
        }

        holder.tv_coupon_usage.setText(couponData.getActivityType());
        holder.tv_coupon_condition1.setText("" + couponData.getDesc());
        holder.tv_coupon_deadline.setText("有效期至" + couponData.getCouponDate());

        return convertView;
    }


    private class ViewHolder {
        LinearLayout ll2;
        TextView tv_coupon_name;
        TextView tv_coupon_condition1;
        TextView tv_coupon_deadline;
        ImageView iv_coupon_status;
        TextView tv_coupon_value;
        TextView tv_coupon_usage;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mCoupons == null ? 0 : mCoupons.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


}

