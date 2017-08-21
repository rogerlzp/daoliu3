package com.wash.daoliu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wash.daoliu.R;

import static android.widget.LinearLayout.LayoutParams.*;

/**
 * Created by jiajia on 2016/1/8.
 */
public class EmptyView extends LinearLayout {
    private Context context = null;
    private TextView mTitle = null;
    private TextView mContent = null;
    private ImageView mIcon = null;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.empty_view, null);
        mIcon = (ImageView) view.findViewById(R.id.icon);
        mTitle = (TextView) view.findViewById(R.id.title);
        mContent = (TextView) view.findViewById(R.id.content);
        this.addView(view,new LayoutParams(MATCH_PARENT,MATCH_PARENT));
        init(attrs);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.EmptyViewStyle);
        String title = t.getString(R.styleable.EmptyViewStyle_emptyTitle);
        String content = t.getString(R.styleable.EmptyViewStyle_emptyContent);
        int iconRes = t.getResourceId(R.styleable.EmptyViewStyle_emptyIconRes, R.drawable.icon_nothing_net);
        mIcon.setImageResource(iconRes);
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
        } else {
            mTitle.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(content)) {
            mContent.setText(content);
            mContent.setVisibility(View.VISIBLE);
        } else {
            mContent.setVisibility(View.INVISIBLE);
        }

    }
}
