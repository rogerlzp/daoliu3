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
 * Created by rogerlzp on 16/1/4.
 */
public class RegisterDialogFragment extends DialogFragment {

    private RegisterSuccessListener mRegisterSuccessListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_register_success, container);
        // view.findViewById(R.id.btn_quit).setOnClickListener(this);
        // view.findViewById(R.id.btn_next).setOnClickListener(this);

        view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRegisterSuccessListener != null) {
                    mRegisterSuccessListener.onRegisterSucceedNext();
                }
                dismissAllowingStateLoss();
            }
        });
        view.findViewById(R.id.btn_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRegisterSuccessListener != null) {
                    mRegisterSuccessListener.onRegisterSucceedReturn();
                }
                dismissAllowingStateLoss();
            }
        });

        return view;
    }

    public interface RegisterSuccessListener {
        public void onRegisterSucceedNext();

        public void onRegisterSucceedReturn();
    }


    //初始化mListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mRegisterSuccessListener = (RegisterSuccessListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement BackListener");
        }
    }


//    @Override
//    public void onClick(View v) {
//        Intent mIntent;
//
//        switch (v.getId()) {
//            case R.id.btn_quit:
//                // goto main activity
//                mIntent = new Intent(getActivity(), MainActivity.class);
//                getActivity().startActivity(mIntent);
//                dismissAllowingStateLoss();
//                break;
//            case R.id.btn_next:  // 去充值
//                mIntent = new Intent(getActivity(), DepositActivity.class);
//                getActivity().startActivity(mIntent);
//                dismissAllowingStateLoss();
//                break;
//            default:
//                break;
//        }
//    }

}
