package com.wash.daoliu.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.WithDrawActivity;

/**
 * Created by rogerlzp on 16/1/6.
 */
public class WithdrawPostDialogFragment extends DialogFragment implements View.OnClickListener {


    private PostWithdrawListener mPostWithdrawListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        setCancelable(false);
        View view = inflater.inflate(R.layout.fragment_post_withdraw, container);
        //  view.findViewById(R.id.btn_next).findViewById(R.id.btn_next);

        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            ((TextView) view.findViewById(R.id.tv_note)).setText(bundle.getString(WithDrawActivity.POST_WITHDRAW_NOTE));
        }

        view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPostWithdrawListener != null) {
                    mPostWithdrawListener.onPostListener();
                }
                dismissAllowingStateLoss();
            }
        });


        return view;

    }


    //初始化mListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mPostWithdrawListener = (PostWithdrawListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement BackListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (mPostWithdrawListener != null) {
                    mPostWithdrawListener.onPostListener();
                }
                dismissAllowingStateLoss();
                break;
        }
    }


    public interface PostWithdrawListener {
        public void onPostListener();
    }

}
