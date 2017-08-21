package com.wash.daoliu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.ShopDetailActivity;
import com.wash.daoliu.imageloader.ImageLoaderProxy;
import com.wash.daoliu.model.Cloan;
import com.wash.daoliu.model.Shop;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.ProductTagView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhengpingli on 2017/6/25.
 */

public class CloanListAdapter extends BaseAdapter {

    public ArrayList<Cloan> mCloans = new ArrayList<Cloan>();
    public Context ctx;


    public CloanListAdapter(Context context) {
        this.ctx = context;
        // 当前时间
    }

    public void setCloans(ArrayList<Cloan> _mShops) {
        mCloans = _mShops;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Cloan cloanData = mCloans.get(position);
        CloanListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.cloan_item, null);
            holder = new CloanListAdapter.ViewHolder();

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            holder.tv_apply_customer = (TextView) convertView.findViewById(R.id.tv_apply_customer);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
            holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);

            holder.rl3 = (RelativeLayout) convertView.findViewById(R.id.rl3);

            holder.divide_tag = convertView.findViewById(R.id.divide_tag);
            convertView.setTag(holder);
        } else {
            holder = (CloanListAdapter.ViewHolder) convertView.getTag();
        }

        ImageLoaderProxy.getInstance().displayImage(ctx,
                cloanData.getCloanLogo(), holder.iv_logo, R.drawable.ic_image_holder);

        holder.tv_name.setText(cloanData.getCloanName());
        holder.tv_remark.setText(cloanData.getRemark());
        holder.tv_apply_customer.setText("" + cloanData.getApplyCustomer());
        holder.tv_rate.setText(cloanData.getRateRange());
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_logo;
        ProductTagView tag_view;
        ProductTagView tag_view_1;

        TextView tv_name;
        TextView tv_remark;

        TextView tv_apply_customer;
        TextView tv_rate;
        RelativeLayout rl3;
        View divide_tag;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mCloans == null ? 0 : mCloans.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mCloans.get(position - 1);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}