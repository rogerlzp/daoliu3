package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.model.ProductBuyInfo;
import com.wash.daoliu.utility.TextUtils;

import java.util.ArrayList;

/**
 * Created by rogerlzp on 15/12/29.
 */
public class ProductBuyListAdapter extends BaseAdapter {
    public ArrayList<ProductBuyInfo> productBuyInfos = null;
    public Context ctx;

    public ProductBuyListAdapter(Context context, ArrayList<ProductBuyInfo> productBuyInfos) {
        this.ctx = context;
        this.productBuyInfos = productBuyInfos;
    }

    public void setData(ArrayList<ProductBuyInfo> productBuyInfos) {
        this.productBuyInfos = productBuyInfos;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ProductBuyInfo productBuyInfo = productBuyInfos.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.product_buy_list_item, null);
            holder = new ViewHolder();
            holder.tv_username = (TextView) convertView.findViewById(R.id.user_name);
            holder.tv_money = (TextView) convertView.findViewById(R.id.money);
            holder.tv_date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_username.setText(TextUtils.replaceStarToString(productBuyInfo.userName));
        holder.tv_money.setText(productBuyInfo.orderAmount);
        holder.tv_date.setText(productBuyInfo.orderDate);
        return convertView;
    }


    private class ViewHolder {
        TextView tv_username;
        TextView tv_money;
        TextView tv_date;
    }

    public int getCount() {
        // TODO Auto-generated method stub
//        return balances==null?0:balances.size();
        return productBuyInfos == null ? 0 : productBuyInfos.size();
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

