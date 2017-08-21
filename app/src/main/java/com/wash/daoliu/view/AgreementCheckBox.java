package com.wash.daoliu.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.LTNWebPageActivity;

/**
 * Created by rogerlzp on 15/12/3.
 */
public class AgreementCheckBox extends RelativeLayout {

    private static final String TAG = "AgreementCheckBox";

    // checkBox
    private CheckBox checkBox;


    // agreement
    private TextView textView;

    /**
     * Additional listener fired when cleared
     */
    private OnClickListener onClickClearListener;

    // Context
    private Context mContext;

    public AgreementCheckBox(Context context) {
        super(context);
        mContext = context;
        init(null);
    }


    public AgreementCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);

    }

    public AgreementCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }

    /**
     * Initialize view
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {

        //   float scale = getResources().getDisplayMetrics().density;
        //  int dpAsPixels = (int) (R.styleable.ClearableEditText_imagePaddingLeft *scale + 0.5f);
        //inflate layout
        LayoutInflater inflater
                = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_aggrement_checkbox, this, true);

        //pass attributes to EditText, make clearable
        checkBox = (CheckBox) findViewById(R.id.v1_agreement_checkbox);
        checkBox.setChecked(true);
        textView = (TextView) findViewById(R.id.v1_agreement_tv);


        boolean checked = true;


        if (attrs != null) {
            TypedArray attrsArray =
                    getContext().obtainStyledAttributes(attrs, R.styleable.AgreementCheckBox);
            checked = attrsArray.getBoolean(R.styleable.AgreementCheckBox_android_checked, false);
         //   textView.setTextSize(R.styleable.AgreementCheckBox_android_textSize);
         //   textView.setTextColor(getResources().getColor(R.styleable.AgreementCheckBox_android_textColor));
        }


        checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickClearListener != null)
                    onClickClearListener.onClick(v);
            }
        });

    }

    public void initAgreement(String mAgreement, String agreementUrl) {
        SpannableString spannableString = new SpannableString("《"+mAgreement+"》");
        NiaorenClickableSpan clickableSpan = new NiaorenClickableSpan(mContext, spannableString,mAgreement,agreementUrl);
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(this.getResources().getString(R.string.read_agreement));
        textView.append(spannableString);

        textView.setMovementMethod(LinkMovementMethod.getInstance());;
        //  textView.setFocusable(false);

    }


    private class NiaorenClickableSpan extends ClickableSpan {

        private SpannableString spannableString;
        private String  sString;
        private Context context;
        private String agreementUrl;

        public NiaorenClickableSpan(Context _context, SpannableString _spannableString,String sString, String _agreementUrl) {
            this.spannableString = _spannableString;
            this.sString = sString;
            this.context = _context;
            this.agreementUrl = _agreementUrl;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.recommend));

        }

        @Override
        public void onClick(View widget) {

            Intent intent = new Intent(this.context, LTNWebPageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(LTNWebPageActivity.BUNDLE_URL, agreementUrl);
            bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, sString);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }

    /**
     * @return checked status from checkbox
     */
    public boolean getChecked() {
        return checkBox.isChecked();
    }


    /**
     * Set OnClickListener, making EditText unfocusable
     *
     * @param listener
     */
    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        boolean checked = this.getChecked();
        if (checked) {
            checkBox.setChecked(false);
            checkBox.setBackgroundResource(R.drawable.uncheck_single);
        } else {
            checkBox.setChecked(false);
            checkBox.setBackgroundResource(R.drawable.check_single);
        }
    }


}