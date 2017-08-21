package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.imageloader.ImageLoaderProxy;
import com.wash.daoliu.model.CloanUserRecord;
import com.wash.daoliu.utility.TextUtils;

import java.util.ArrayList;

/**
 * Created by zhengpingli on 2017/6/25.
 */

public class CloanUserListAdapter extends BaseAdapter {
    public ArrayList<CloanUserRecord> myInvests = null;
    public Context ctx;

    public CloanUserListAdapter(Context context, ArrayList<CloanUserRecord> myInvests) {
        this.ctx = context;
        this.myInvests = myInvests;
    }

    public void setData(ArrayList<CloanUserRecord> myInvests) {
        this.myInvests = myInvests;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CloanUserRecord myInvest = myInvests.get(position);
        CloanUserListAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.cloanuser_list_item, null);
            holder = new CloanUserListAdapter.ViewHolder();
            holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);

        } else {
            holder = (CloanUserListAdapter.ViewHolder) convertView.getTag();
        }
        //  holder.tv_product_name.setText(myInvest.productType);
        holder.tv_name.setText(myInvest.company + "-" + myInvest.cloanName);
        //  holder.tv_time.setText(TextUtils.convertDateToFromat2(myInvest.createDate));
        holder.tv_time.setText(myInvest.createDate);

        ImageLoaderProxy.getInstance().displayImage(this.ctx, myInvest.cloanLogo, holder.iv_logo, R.drawable.ic_image_holder);

        return convertView;
    }


    private class ViewHolder {
        ImageView iv_logo;
        TextView tv_name;
        TextView tv_time;
    }

    public int getCount() {
        // TODO Auto-generated method stub
//        return balances==null?0:balances.size();
        return myInvests == null ? 0 : myInvests.size();
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