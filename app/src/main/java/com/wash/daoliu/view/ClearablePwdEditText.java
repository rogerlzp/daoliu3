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
import android.widget.RelativeLayout;

import com.wash.daoliu.R;

/**
 * Created by rogerlzp on 15/11/26.
 */
public class ClearablePwdEditText extends RelativeLayout {

    /**
     * EditText component
     */
    private EditText editText;

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

    private OnPWDEditTextChangedListener onEditTextChangedListener = null;

    public void setOnPWDEditTextChangedListener(OnPWDEditTextChangedListener onEditTextChangedListener) {
        this.onEditTextChangedListener = onEditTextChangedListener;
    }

    public ClearablePwdEditText(Context context) {
        super(context);
        init(null);
    }

    public ClearablePwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public ClearablePwdEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
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
        inflater.inflate(R.layout.view_edittext_clearable_pwd, this, true);

        // pass attributes to EditText, make clearable
        // pass attributes to EditText, make show icon
        editText = (EditText) findViewById(R.id.edittext);
        boolean enabled = true;
        if (attrs != null) {
            TypedArray attrsArray =
                    getContext().obtainStyledAttributes(attrs, R.styleable.ClearableEditText);
            editText.setInputType(
                    attrsArray.getInt(
                            R.styleable.ClearableEditText_android_inputType, InputType.TYPE_CLASS_TEXT));
            editText.setHint(attrsArray.getString(R.styleable.ClearableEditText_android_hint));
            enabled = attrsArray.getBoolean(R.styleable.ClearableEditText_android_enabled, true);
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
                        showButton.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        clearButton.setVisibility(RelativeLayout.GONE);
                        showButton.setVisibility(RelativeLayout.GONE);
                    }

                }


                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            editText.setEnabled(false);
        }

        //build clear button
        clearButton = (ImageButton) findViewById(R.id.button_clear);
        clearButton.setVisibility(RelativeLayout.INVISIBLE);
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                if (onClickClearListener != null) onClickClearListener.onClick(v);
            }
        });

        //build clear button
        showButton = (ImageButton) findViewById(R.id.button_show);
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


    /**
     * Set value
     *
     * @param text
     */
    public void setHint(String text) {
        editText.setHint(text);
    }

    public void setPasswordFormat() {
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public interface OnPWDEditTextChangedListener{
        void onPWDEditTextChanged(boolean flag);
    }
}