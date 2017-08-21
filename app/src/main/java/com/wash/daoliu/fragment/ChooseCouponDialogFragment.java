package com.wash.daoliu.fragment;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wash.daoliu.R;
import com.wash.daoliu.model.Coupon;
import com.wash.daoliu.utility.LTNConstants;


//购买前选择商品型号尺码等
public class ChooseCouponDialogFragment extends DialogFragment implements OnClickListener {
    private View mBackground = null;
    private LinearLayout mContentLayout = null;
    private ListView mCouponList = null;
    private ArrayList<Coupon> coupons = null;
    private int current = -1;
    private OnChooseCouponListener onChooseCouponListener = null;

    public static LoginDialogFragment newInstance() {
        return new LoginDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //		setStyle(R.style.choose_type_dialog_theme,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        View view = inflater.inflate(R.layout.choose_coupon_dialog_fragment, null);
        String cString = getArguments().getString(LTNConstants.PRACELABLE_COUPONS);
        if (!TextUtils.isEmpty(cString)) {
            coupons = new Gson().fromJson(cString, new TypeToken<ArrayList<Coupon>>() {
            }.getType());
        }
        initView(view);
        showAnimator();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onChooseCouponListener = (OnChooseCouponListener) activity;

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return coupons == null ? 0 : coupons.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Coupon coupon = coupons.get(position);
            Holder holder = null;
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(getActivity(), R.layout.choose_coupon_item, null);
                holder.tv_coupon_name = (TextView) convertView.findViewById(R.id.tv_coupon_name);
                holder.tv_coupon_deadline = (TextView) convertView.findViewById(R.id.tv_coupon_deadline);
                holder.iv_coupon = (ImageView) convertView.findViewById(R.id.iv_coupon);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.tv_coupon_name.setText(coupon.getCouponName());
            holder.tv_coupon_deadline.setText("有效期至" + coupon.getCouponDate());
            if (current == position) {
                holder.iv_coupon.setImageResource(R.drawable.icon_selected);
            } else {
                holder.iv_coupon.setImageResource(R.drawable.icon_choice);
            }
            return convertView;
        }
    }

    class Holder {
        TextView tv_coupon_name;
        TextView tv_coupon_deadline;
        ImageView iv_coupon;
    }

    private void initView(View view) {
        // TODO Auto-generated method stub
        view.findViewById(R.id.close).setOnClickListener(this);
        mBackground = view.findViewById(R.id.bg);
        mCouponList = (ListView) view.findViewById(R.id.coupon_list);
        mBackground.setOnClickListener(this);
        mContentLayout = (LinearLayout) view.findViewById(R.id.type_layout);
        mCouponList.setAdapter(new MyAdapter());
        mCouponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current = position;
                ((BaseAdapter) mCouponList.getAdapter()).notifyDataSetChanged();
                hideAnimator();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onActivityCreated(arg0);
    }

    private void showAnimator() {
        // TODO Auto-generated method stub
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentLayout.getLayoutParams();
        if (params != null) {
            AnimatorSet set = new AnimatorSet();
//            ValueAnimator animator = ValueAnimator.ofInt(-params.height, 0);
//            animator.addUpdateListener(new AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    // TODO Auto-generated method stub
//                    params.bottomMargin = Integer.parseInt(animation.getAnimatedValue().toString());
//                    mContentLayout.requestLayout();
//                }
//            });
//            animator.addListener(new AnimatorListener() {
//
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    // TODO Auto-generated method stub
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//                    // TODO Auto-generated method stub
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    // TODO Auto-generated method stub
//
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//                    // TODO Auto-generated method stub
//
//                }
//            });
            set.setDuration(300);
            set.setInterpolator(new LinearInterpolator());
            set.playTogether(ObjectAnimator.ofFloat(mContentLayout, "alpha", 0f, 1f), ObjectAnimator.ofFloat(mBackground, "alpha", 0f, 1f));
            set.start();


        }
    }

    private void hideAnimator() {
        // TODO Auto-generated method stub
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentLayout.getLayoutParams();
        if (params != null) {
            AnimatorSet set = new AnimatorSet();
//            ValueAnimator animator = ValueAnimator.ofInt(0, -params.height);
//            animator.addUpdateListener(new AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    // TODO Auto-generated method stub
//                    params.bottomMargin = Integer.parseInt(animation.getAnimatedValue().toString());
//                    mContentLayout.requestLayout();
//                }
//            });
            set.setDuration(300);
            set.setInterpolator(new LinearInterpolator());
            set.playTogether( ObjectAnimator.ofFloat(mContentLayout, "alpha", 1f, 0f), ObjectAnimator.ofFloat(mBackground, "alpha", 1f, 0f));
            set.start();
            set.addListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // TODO Auto-generated method stub
                    if (current != -1 && onChooseCouponListener != null) {
                        onChooseCouponListener.onChoose(coupons.get(current));
                    }
                    dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // TODO Auto-generated method stub

                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setStyle(LoginDialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                hideAnimator();
                break;
        }
    }

    public interface OnChooseCouponListener {
        void onChoose(Coupon coupon);
    }
}
