package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.model.PartnerEaring;
import com.wash.daoliu.utility.TextUtils;

import java.util.ArrayList;

/**
 * Created by rogerlzp on 15/12/29.
 */
public class ParterEarningsAdapter extends BaseAdapter {
    public ArrayList<PartnerEaring> balances = null;
    public Context ctx;

    public ParterEarningsAdapter(Context context, ArrayList<PartnerEaring> balances) {
        this.ctx = context;
        this.balances = balances;
    }

    public void setData(ArrayList<PartnerEaring> balances) {
        this.balances = balances;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        PartnerEaring balance = balances.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.partner_earnings_list_item, null);
            convertView.setBackgroundResource(R.color.white);
            holder = new ViewHolder();
            holder.tv_type = (TextView) convertView.findViewById(R.id.type);
            holder.tv_time = (TextView) convertView.findViewById(R.id.time);
            holder.tv_balance = (TextView) convertView.findViewById(R.id.balance);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(TextUtils.replaceStarToString(balance.mobileNo));
        holder.tv_time.setText(balance.reward);
        holder.tv_balance.setText(balance.orderReward);
        return convertView;
    }


    private class ViewHolder {
        TextView tv_type;
        TextView tv_time;
        TextView tv_balance;
    }

    public int getCount() {
        // TODO Auto-generated method stub
//        return balances==null?0:balances.size();
        return balances == null ? 0 : balances.size();
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

