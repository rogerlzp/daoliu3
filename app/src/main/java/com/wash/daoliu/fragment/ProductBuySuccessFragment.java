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
 * Created by rogerlzp on 16/1/6.
 */
public class ProductBuySuccessFragment extends DialogFragment {


    private ProductBuyListener mProductBuyListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        setCancelable(false);
        View view = inflater.inflate(R.layout.fragment_buy_success, container);

        view.findViewById(R.id.btn_quit).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (mProductBuyListener != null) {
                     mProductBuyListener.onBuyProductSucceedListener();
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
            mProductBuyListener = (ProductBuyListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement BackListener");
        }
    }


    public interface ProductBuyListener {
        public void onBuyProductSucceedListener();
    }


}