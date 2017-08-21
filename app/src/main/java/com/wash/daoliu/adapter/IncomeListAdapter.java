package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.model.Income;
import com.wash.daoliu.utility.TextUtils;

import java.util.ArrayList;

/**
 * Created by rogerlzp on 15/12/29.
 */
public class IncomeListAdapter extends BaseAdapter {
    public ArrayList<Income> balances = null;
    public Context ctx;

    public IncomeListAdapter(Context context, ArrayList<Income> balances) {
        this.ctx = context;
        this.balances = balances;
    }

    public void setBalances(ArrayList<Income> balances) {
        this.balances = balances;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Income balance = balances.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.income_list_item, null);
            holder = new ViewHolder();
            holder.tv_type = (TextView) convertView.findViewById(R.id.type);
            holder.tv_time = (TextView) convertView.findViewById(R.id.time);
            holder.tv_balance = (TextView) convertView.findViewById(R.id.balance);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(balance.earningName);
        holder.tv_time.setText(balance.earningDate.replace(".0", ""));
        holder.tv_balance.setText("+" + TextUtils.formatDoubleValue(balance.earning));
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

