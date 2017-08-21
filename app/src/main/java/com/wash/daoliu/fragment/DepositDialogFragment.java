package com.wash.daoliu.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.DepositActivity;

/**
 * Created by rogerlzp on 16/1/3.
 */
public class DepositDialogFragment extends DialogFragment implements View.OnClickListener {

    private Button btnNext;
    private DepositOKListener mDepositOKListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        setCancelable(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_deposit_success, container);

        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            ((TextView) view.findViewById(R.id.tv_deposit_note)).setText(bundle.getString(DepositActivity.DEPOSIT_NOTE));
        }
        btnNext = (Button) view.findViewById(R.id.btn_next);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDepositOKListener.onDepositOK();
            }
        });
        return view;
    }


    public interface DepositOKListener {
        public void onDepositOK();
    }

    //初始化mListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDepositOKListener = (DepositOKListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement BackListener");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                dismiss();
                break;
            default:
                break;
        }

    }

}
