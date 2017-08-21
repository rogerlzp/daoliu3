package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.model.Balance;
import com.wash.daoliu.utility.TextUtils;

import java.util.ArrayList;

/**
 * Created by rogerlzp on 15/12/29.
 */
public class BalanceListAdapter extends BaseAdapter {
    public ArrayList<Balance> balances = null;
    public Context ctx;

    public BalanceListAdapter(Context context, ArrayList<Balance> balances) {
        this.ctx = context;
        this.balances = balances;
    }

    public void setBalances(ArrayList<Balance> balances) {
        this.balances = balances;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Balance balance = balances.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.balance_list_item, null);
            holder = new ViewHolder();
            holder.tv_type = (TextView) convertView.findViewById(R.id.type);
            holder.tv_time = (TextView) convertView.findViewById(R.id.time);
            holder.tv_balance = (TextView) convertView.findViewById(R.id.balance);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(balance.operateType);
        holder.tv_time.setText(balance.operateDate.substring(0, 10)); //增加个数
//        holder.tv_balance.setText(TextUtils.formatDoubleValue(balance.amount));
        holder.tv_balance.setText(TextUtils.formatDoubleValue(balance.amountText));
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

