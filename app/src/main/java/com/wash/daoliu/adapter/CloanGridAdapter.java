package com.wash.daoliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.imageloader.ImageLoaderProxy;
import com.wash.daoliu.model.Cloan;
import com.wash.daoliu.utility.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengpingli on 2017/6/24.
 */

public class CloanGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context mContext;

    public ArrayList<Cloan> mCloans = new ArrayList<Cloan>();

    public void setCloans(ArrayList<Cloan> _mCloans) {
        mCloans = _mCloans;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //  if (viewType == 0) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cloan_griditem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        view.setMinimumHeight(view.getMinimumWidth());

        view.setOnClickListener(this);
        //   view.setOnLongClickListener(this);

        return holder;
        //}

        // return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        Cloan mCloan = mCloans.get(position);
        ImageLoaderProxy.getInstance().displayImage(mContext, mCloan.getCloanLogo(),
                ((MyViewHolder) holder).iv_logo, R.drawable.icon_business);
        ((MyViewHolder) holder).tv_name.setText(mCloan.getCloanName());

        //TODO:确认业务
        if (!StringUtils.isNullOrEmpty(mCloan.getCloanTags())) {
            switch (mCloan.getCloanTags()) {
                case "tuijian":
                    ((MyViewHolder) holder).iv_tag.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).iv_tag.setImageResource(R.drawable.icon_tuijian);
                    break;
                case "dixi":
                    ((MyViewHolder) holder).iv_tag.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).iv_tag.setImageResource(R.drawable.icon_dixi);

                    break;
                case "hot":
                    ((MyViewHolder) holder).iv_tag.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).iv_tag.setImageResource(R.drawable.icon_hot);
                    break;
                case "new":
                    ((MyViewHolder) holder).iv_tag.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).iv_tag.setImageResource(R.drawable.icon_new);
                    break;
            }

        } else {
            ((MyViewHolder) holder).iv_tag.setVisibility(View.GONE);

        }

        ((MyViewHolder) holder).itemView.setTag(mCloan); //存储Tag

    }

    @Override
    public int getItemCount() {
        return mCloans == null ? 0 : mCloans.size();
    }

    private int itemHeight;

    public int getItemHeight() {
        return itemHeight;
    }

    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener != null) {
            //     mOnRecyclerViewItemClickListener.onItemClick(v);
            mOnRecyclerViewItemClickListener.onItemClick(v, (Cloan) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Cloan cloan);

        void onItemLongClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnRecyclerViewItemClickListener = listener;
    }

    //适配器初始化
    public CloanGridAdapter(Context context, ArrayList<Cloan> _mCloans) {
        mContext = context;
        this.mCloans = _mCloans;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_logo;
        private ImageView iv_tag;

        private TextView tv_name;

        public MyViewHolder(View view) {
            super(view);
            iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
            iv_tag = (ImageView) view.findViewById(R.id.iv_tag);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
