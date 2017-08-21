package com.wash.daoliu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wash.daoliu.R;
import com.wash.daoliu.utility.ViewUtils;

/**
 * Created by rogerlzp on 15/11/26.
 */
public class MyPwdEditText extends RelativeLayout {

    /**
     * EditText component
     */
    private EditText editText;

    private ImageView imageView;
    /**
     * Button that clears the EditText contents
     */
    private ImageButton clearButton;

    /**
     * Button that show the EditText contents
     */
    private ImageButton showButton;

    private Boolean flag = false;

    /**
     * Additional listener fired when cleared
     */
    private OnClickListener onClickClearListener;

    private OnMyPwdEditTextChangedListener onEditTextChangedListener = null;

    public MyPwdEditText(Context context) {
        super(context);
        init(null);
    }

    public MyPwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MyPwdEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public void setOnPWDEditTextChangedListener(OnMyPwdEditTextChangedListener onEditTextChangedListener) {
        this.onEditTextChangedListener = onEditTextChangedListener;
    }
    /**
     * Initialize view
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {

        //inflate layout
        LayoutInflater inflater
                = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_edittext_clearable_pwd_login, this, true);

        // pass attributes to EditText, make clearable
        // pass attributes to EditText, make show icon
        editText = (EditText) findViewById(R.id.edittext);

        imageView = (ImageView) findViewById(R.id.mobile_icon2);


        boolean enabled = true;
        if (attrs != null) {
            TypedArray attrsArray =
                    getContext().obtainStyledAttributes(attrs, R.styleable.MyPwdEditText);
            editText.setInputType(
                    attrsArray.getInt(
                            R.styleable.MyPwdEditText_android_inputType, InputType.TYPE_CLASS_TEXT));
            editText.setHint(attrsArray.getString(R.styleable.MyPwdEditText_android_hint));
            enabled = attrsArray.getBoolean(R.styleable.MyPwdEditText_android_enabled, true);

            imageView.setImageDrawable(getResources().getDrawable(attrsArray.getResourceId(R.styleable.MyPwdEditText_pwdImageID,
                    R.drawable.icon_mobileno))); // default to icon_mobileno
            imageView.setPadding(ViewUtils.dip2px(this.getContext(), attrsArray.getInteger(R.styleable.MyPwdEditText_pwdImageLeft, 14)), 0, 0, 0);

            clearButton = (ImageButton) findViewById(R.id.button_clear);
            RelativeLayout.LayoutParams lp = (LayoutParams) clearButton.getLayoutParams();
            lp.setMargins(0, 0, ViewUtils.dip2px(this.getContext(), attrsArray.getInteger(R.styleable.MyPwdEditText_pwdClearRight, 14)), 0);
            clearButton.setLayoutParams(lp);

            showButton = (ImageButton) findViewById(R.id.button_show);
            RelativeLayout.LayoutParams lp2 = (LayoutParams) showButton.getLayoutParams();
            lp2.setMargins(ViewUtils.dip2px(this.getContext(), attrsArray.getInteger(R.styleable.MyPwdEditText_pwdShowLeft, 14)), 0, 0, 0);
            showButton.setLayoutParams(lp2);


        }
        if (enabled) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(onEditTextChangedListener!=null){
                        onEditTextChangedListener.onPWDEditTextChanged(s.length() > 0);
                    }
                    if (s.length() > 0) {
                        clearButton.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        clearButton.setVisibility(RelativeLayout.GONE);
                    }

                }


                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            editText.setEnabled(false);
        }


        clearButton.setVisibility(RelativeLayout.INVISIBLE);
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                if (onClickClearListener != null) onClickClearListener.onClick(v);
            }
        });

        //build clear button

        showButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!flag) {
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    showButton.setBackground(getResources().getDrawable(R.drawable.icon_show));

                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showButton.setBackground(getResources().getDrawable(R.drawable.icon_hide));
                }
                flag = !flag;
                editText.postInvalidate();
                CharSequence pwdText = editText.getText();
                if (pwdText instanceof Spannable) {
                    Spannable spanText = (Spannable) pwdText;
                    Selection.setSelection(spanText, pwdText.length());
                }
            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
    }

    /**
     * Get value
     *
     * @return text
     */
    public Editable getText() {
        return editText.getText();
    }

    /**
     * Set value
     *
     * @param text
     */
    public void setText(String text) {
        editText.setText(text);
    }

    /**
     * Set OnClickListener, making EditText unfocusable
     *
     * @param listener
     */
    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setOnClickListener(listener);
    }

    /**
     * Set listener to be fired after EditText is cleared
     *
     * @param listener
     */
    public void setOnClearListener(View.OnClickListener listener) {
        onClickClearListener = listener;
    }

    private class OnFocusChangeListenerImpl implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                clearButton.setVisibility(RelativeLayout.GONE);
            }
            if (((EditText) v).getText().length() > 0 && hasFocus) {
                clearButton.setVisibility(RelativeLayout.VISIBLE);
            }

        }
    }

    /**
     * set maxlength to maxLength
     *
     * @param maxLength
     */
    public void setMaxLength(int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void setPasswordFormat() {
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public interface OnMyPwdEditTextChangedListener{
        void onPWDEditTextChanged(boolean flag);
    }


}