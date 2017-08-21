package com.wash.daoliu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.ReserveActivity;
import com.wash.daoliu.model.ShopService;
import com.wash.daoliu.utility.LTNConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhengpingli on 2017/4/3.
 */

public class ShopServiceAdapter extends RecyclerView.Adapter<ShopServiceAdapter.MyHolder> {

    public ArrayList<ShopService> mShopServices = new ArrayList<ShopService>();
    public Context ctx;

    // 当前时间
    SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ShopServiceAdapter(Context context) {
        this.ctx = context;
        // 当前时间
    }

    public void setShopServices(ArrayList<ShopService> _mShops) {
        mShopServices = _mShops;
        notifyDataSetChanged();
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.shopservice_item, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        ShopService shopService = mShopServices.get(position);
        ShopServiceAdapter.ShopServiceListener shopServiceListener = new ShopServiceAdapter.ShopServiceListener(ctx, shopService);
        //  convertView.setOnClickListener(shopServiceListener);
        holder.tv_service_name.setOnClickListener(shopServiceListener);
        holder.tv_original_price.setOnClickListener(shopServiceListener);
        holder.tv_time_consume.setOnClickListener(shopServiceListener);
        holder.tv_service_name.setText(shopService.getServiceName());
        holder.tv_original_price.setText(String.valueOf(shopService.getOriginalPrice()));
        holder.tv_time_consume.setText(String.valueOf(shopService.getTimeConsume()));

    }

    @Override
    public int getItemCount() {
        return mShopServices.size();
    }


    class ShopServiceListener implements View.OnClickListener {
        ShopService shopService;
        public Context ctx;

        public ShopServiceListener(Context _ctx, ShopService _shopService) {
            ctx = _ctx;
            this.shopService = _shopService;
        }


        /**
         * 体验标跳转到体验标
         * 散标跳转到散标
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ctx, ReserveActivity.class);
            intent.putExtra(LTNConstants.SHOP_SERVICE, new Gson().toJson(shopService));
            ctx.startActivity(intent);
        }

    }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_service_name;
        TextView tv_original_price;
        TextView tv_time_consume;
        View divide_tag;

        //实现的方法
        public MyHolder(View itemView) {
            super(itemView);
            tv_service_name = (TextView) itemView.findViewById(R.id.tv_service_name);
            tv_original_price = (TextView) itemView.findViewById(R.id.tv_original_price);
            tv_time_consume = (TextView) itemView.findViewById(R.id.tv_time_consume);
        }
    }
}
