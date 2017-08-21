package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.model.Anxintou;
import com.wash.daoliu.utility.TextUtils;

import java.util.ArrayList;

/**
 * Created by rogerlzp on 16/2/20.
 */
public class AnxintouListAdapter extends BaseAdapter {
    public ArrayList<Anxintou> myInvests = null;
    public Context ctx;

    public AnxintouListAdapter(Context context, ArrayList<Anxintou> myInvests) {
        this.ctx = context;
        this.myInvests = myInvests;
    }

    public void setData(ArrayList<Anxintou> myInvests) {
        this.myInvests = myInvests;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Anxintou myInvest = myInvests.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.anxintou_list_item, null);
            holder = new ViewHolder();
            holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            holder.annualIncomeText = (TextView) convertView.findViewById(R.id.annualIncomeText);
            holder.productDeadLine = (TextView) convertView.findViewById(R.id.productDeadLine);
            holder.repaymentType = (TextView) convertView.findViewById(R.id.repaymentType);
            holder.orderAmount = (TextView) convertView.findViewById(R.id.orderAmount);
            holder.orderRevenue = (TextView) convertView.findViewById(R.id.orderRevenue);
            holder.payDate = (TextView) convertView.findViewById(R.id.payDate);
            holder.endDate = (TextView) convertView.findViewById(R.id.endDate);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //  holder.tv_product_name.setText(myInvest.productType);
        holder.tv_product_name.setText(myInvest.product_name);
        if (!myInvest.annual_income.endsWith("%")) {
            myInvest.annual_income = myInvest.annual_income + "%";
        }
        holder.annualIncomeText.setText(myInvest.annual_income);

        holder.productDeadLine.setText(myInvest.investment_term);
        // holder.repaymentType.setText(myInvest.repaymentType);
        holder.orderAmount.setText(TextUtils.formatDoubleValueWithUnit(myInvest.order_amount));
        holder.orderRevenue.setText(TextUtils.formatDoubleValueWithUnit(myInvest.profit));
        holder.payDate.setText(myInvest.order_date);
        holder.endDate.setText(myInvest.over_date);
        return convertView;
    }


    private class ViewHolder {
        TextView tv_product_name;
        TextView annualIncomeText;
        TextView productDeadLine;
        TextView repaymentType;
        TextView orderAmount;
        TextView orderRevenue;
        TextView payDate;
        TextView endDate;
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

