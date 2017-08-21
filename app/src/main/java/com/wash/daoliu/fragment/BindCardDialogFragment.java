package com.wash.daoliu.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.wash.daoliu.R;

/**
 * Created by rogerlzp on 16/1/3.
 */
public class BindCardDialogFragment extends DialogFragment {

    private BindcardSuccessListener mBindcardSuccessListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        setCancelable(false);
        View view = inflater.inflate(R.layout.fragment_bindcard_success, container);
//        view.findViewById(R.id.btn_quit).setOnClickListener(this);
//        view.findViewById(R.id.btn_next).setOnClickListener(this);

        view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBindcardSuccessListener != null) {
                    mBindcardSuccessListener.onBindcardSucceedNext();
                }
                dismissAllowingStateLoss();
            }
        });
        view.findViewById(R.id.btn_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBindcardSuccessListener != null) {
                    mBindcardSuccessListener.onBindcardSucceedCancel();
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
            mBindcardSuccessListener = (BindcardSuccessListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "必须实现 BindcardSuccessListener");
        }
    }

    public interface BindcardSuccessListener {
        public void onBindcardSucceedCancel();
        public void onBindcardSucceedNext();
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_quit:
//                dismiss();
//                break;
//            case R.id.btn_next:  // 去充值
//                Intent mIntent = new Intent(getActivity(), DepositActivity.class);
//                getActivity().startActivity(mIntent);
//                this.dismiss();
//                break;
//            default:
//                break;
//        }
//
//    }

}
