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
 * Created by rogerlzp on 16/1/5.
 */
public class WithdrawPreDialogFragment extends DialogFragment implements View.OnClickListener {


    private BackListener mBackListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        setCancelable(false);
        View view = inflater.inflate(R.layout.fragment_pre_withdraw, container);
      view.findViewById(R.id.btn_quit).setOnClickListener(this);
      view.findViewById(R.id.btn_next).setOnClickListener(this);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            ((TextView) view.findViewById(R.id.tv_note)).setText(bundle.getString(WithDrawActivity.PRE_WITHDRAW_NOTE));
        }
        return view;
    }


    //初始化mListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mBackListener = (BackListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement BackListener");
        }
    }


    public interface BackListener {
        public void onBackListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_quit:
                dismiss();
                break;
            case R.id.btn_next:
                if(mBackListener!=null){
                    mBackListener.onBackListener();
                }
                dismissAllowingStateLoss();
            default:
                break;
        }

    }

}


