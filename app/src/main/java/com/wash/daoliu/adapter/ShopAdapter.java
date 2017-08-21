package com.wash.daoliu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wash.daoliu.R;
import com.wash.daoliu.activities.ShopDetailActivity;
import com.wash.daoliu.imageloader.ImageLoaderProxy;
import com.wash.daoliu.model.Shop;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.view.ProductTagView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhengpingli on 2017/3/27.
 */

public class ShopAdapter extends BaseAdapter {

    private static final String TAG = ShopAdapter.class.getSimpleName();
    public ArrayList<Shop> mShops = new ArrayList<Shop>();
    public Context ctx;

    // 当前时间
    SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ShopAdapter(Context context) {
        this.ctx = context;
        // 当前时间
    }

    public void setShops(ArrayList<Shop> _mShops) {
        mShops = _mShops;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ShopAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.shop_item, null);
            holder = new ShopAdapter.ViewHolder();

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tag_view = (ProductTagView) convertView.findViewById(R.id.tag_view);
            holder.tag_view_1 = (ProductTagView) convertView.findViewById(R.id.tag_view_1);
            holder.iv_shop = (ImageView) convertView.findViewById(R.id.iv_shop);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.tv_activity_desc = (TextView) convertView.findViewById(R.id.tv_activity_desc);
            holder.rb_rating = (RatingBar) convertView.findViewById(R.id.rb_rating);
            holder.tv_buy = (TextView) convertView.findViewById(R.id.tv_buy);
            holder.divide_tag = convertView.findViewById(R.id.divide_tag);
            holder.tv_queue_cars = (TextView) convertView.findViewById(R.id.tv_queue_cars);
            holder.tv_working_cars = (TextView) convertView.findViewById(R.id.tv_working_cars);
            holder.tv_waiting_time = (TextView) convertView.findViewById(R.id.tv_waiting_time);


            convertView.setTag(holder);
        } else {
            holder = (ShopAdapter.ViewHolder) convertView.getTag();
        }

        Shop shopData = mShops.get(position);
        ShopAdapter.ShopDetailListener shopDetailListener = new ShopAdapter.ShopDetailListener(ctx, shopData);
        convertView.setOnClickListener(shopDetailListener);
        holder.tv_buy.setOnClickListener(shopDetailListener);
        holder.tv_name.setText(shopData.getShopName());
        holder.tv_address.setText(shopData.getShopLocation());
        //TODO: add the logic it later
        if (shopData.getDistance() % 1000 != 0) {
            holder.tv_distance.setText(shopData.getDistance() / 1000 + "公里");
        } else {
            holder.tv_distance.setText("" + shopData.getDistance() + "米");
        }

        holder.tv_waiting_time.setText("" + shopData.getWaitingTime());

        holder.tv_name.setText(shopData.getShopName());

        holder.tv_queue_cars.setText("" + shopData.getCarWaiting());
        holder.tv_working_cars.setText("" + shopData.getCarWashing());

        ImageLoaderProxy.getInstance().displayImage(ctx, shopData.getPrimaryPic(), holder.iv_shop, R.drawable.ic_image_holder);

        holder.rb_rating.setRating(shopData.getRating());



        return convertView;
    }

    private class ViewHolder {
        TextView tv_name;
        ProductTagView tag_view;
        ProductTagView tag_view_1;
        ImageView iv_shop;
        TextView tv_address;
        TextView tv_distance;
        TextView tv_activity_desc;
        TextView tv_buy;
        RatingBar rb_rating;

        TextView tv_queue_cars;
        TextView tv_working_cars;
        TextView tv_waiting_time;

        View divide_tag;
    }


    class ShopDetailListener implements View.OnClickListener {
        Shop shop;
        public Context ctx;

        public ShopDetailListener(Context _ctx, Shop _shop) {
            ctx = _ctx;
            this.shop = _shop;
        }


        /**
         * 体验标跳转到体验标
         * 散标跳转到散标
         *
         * @param v
         */
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(ctx, ShopDetailActivity.class);
            //  Bundle bundle = new Bundle();
            intent.putExtra(LTNConstants.SHOP, new Gson().toJson(shop));

            // intent.putExtras(bundle);
            ctx.startActivity(intent);
        }

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return mShops == null ? 0 : mShops.size();
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
