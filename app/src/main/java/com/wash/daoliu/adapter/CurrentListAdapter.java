package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.model.CurrentIncome;
import com.wash.daoliu.utility.TextUtils;

import java.util.ArrayList;

/**
 * Created by jiajia on 2016/1/24.
 */
public class CurrentListAdapter extends BaseAdapter {
    public ArrayList<CurrentIncome> currentIncomes = null;
    public Context ctx;

    public CurrentListAdapter(Context context, ArrayList<CurrentIncome> currentIncomes) {
        this.ctx = context;
        this.currentIncomes = currentIncomes;
    }

    public void setBalances(ArrayList<CurrentIncome> currentIncomes) {
        this.currentIncomes = currentIncomes;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        CurrentIncome currentIncome = currentIncomes.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.current_account_list_item, null);
            holder = new ViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.time);
            holder.tv_balance = (TextView) convertView.findViewById(R.id.balance);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_time.setText(currentIncome.time.substring(0,10)); //增加个数
        holder.tv_balance.setText("+"+TextUtils.formatDoubleValue(currentIncome.income));
        return convertView;
    }


    private class ViewHolder {
        TextView tv_time;
        TextView tv_balance;
    }

    public int getCount() {
        // TODO Auto-generated method stub
//        return balances==null?0:balances.size();
        return currentIncomes == null ? 0 : currentIncomes.size();
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


