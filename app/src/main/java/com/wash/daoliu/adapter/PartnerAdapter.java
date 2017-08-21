package com.wash.daoliu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.model.FriendItem;
import com.wash.daoliu.utility.TextUtils;

import java.util.List;

/**
 * Created by jiajia on 2016/1/25.
 */
public class PartnerAdapter extends BaseAdapter {
    private Context context = null;
    private List<FriendItem> friendItems = null;
    private int currentType = 0;

    public PartnerAdapter() {

    }

    public PartnerAdapter(Context context, List<FriendItem> friendItems, int currentType) {
        this.context = context;
        this.friendItems = friendItems;
        this.currentType = currentType;
    }

    public void update(List<FriendItem> friendItems, int currentType) {
        this.friendItems = friendItems;
        this.currentType = currentType;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return friendItems == null ? 0 : friendItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendItem friendItem = friendItems.get(position);
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.partner_list_item, null);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.mobile.setText(TextUtils.replaceStarToString(friendItem.mobile));
        if (currentType == 0) {
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(friendItem.createDate);
        } else {
            holder.date.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class Holder {
        TextView date;
        TextView mobile;
    }
}
