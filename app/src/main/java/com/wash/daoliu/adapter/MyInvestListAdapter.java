package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.model.MyInvest;
import com.wash.daoliu.utility.TextUtils;

import java.util.ArrayList;

/**
 * Created by rogerlzp on 15/12/29.
 */
public class MyInvestListAdapter extends BaseAdapter {
    public ArrayList<MyInvest> myInvests = null;
    public Context ctx;

    public MyInvestListAdapter(Context context, ArrayList<MyInvest> myInvests) {
        this.ctx = context;
        this.myInvests = myInvests;
    }

    public void setData(ArrayList<MyInvest> myInvests) {
        this.myInvests = myInvests;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        MyInvest myInvest = myInvests.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.myinvest_list_item, null);
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
        holder.tv_product_name.setText(myInvest.productName);
        if (!myInvest.annualIncomeText.endsWith("%")) {
            myInvest.annualIncomeText = myInvest.annualIncomeText + "%";
        }
        holder.annualIncomeText.setText(myInvest.annualIncomeText);

        holder.productDeadLine.setText(myInvest.productDeadLine + myInvest.productDeadlineUnit);
        holder.repaymentType.setText(myInvest.repaymentType);
        holder.orderAmount.setText(TextUtils.formatDoubleValueWithUnit(myInvest.orderAmount));
        holder.orderRevenue.setText(TextUtils.formatDoubleValueWithUnit(myInvest.orderRevenue));
        holder.payDate.setText(myInvest.orderDate);
        holder.endDate.setText(myInvest.expireDate);
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

