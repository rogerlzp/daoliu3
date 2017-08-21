package com.wash.daoliu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wash.daoliu.R;


/**
 * Created by rogerlzp on 15/11/26.
 */
public class ClearableEditText extends RelativeLayout {

    private static final String TAG = "ClearableEditText";

    /**
     * EditText component
     */
    private EditText editText;

    /**
     * Button that clears the EditText contents
     */
    private ImageButton clearButton;


    private ImageView imageView;

    /**
     * Additional listener fired when cleared
     */
    private OnClickListener onClickClearListener;

    private OnEditTextChangedListener onEditTextChangedListener = null;

    public void setOnEditTextChangedListener(OnEditTextChangedListener onEditTextChangedListener) {
        this.onEditTextChangedListener = onEditTextChangedListener;
    }

    public ClearableEditText(Context context) {
        super(context);
        init(null);
    }


    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        inflater.inflate(R.layout.view_edittext_clearable, this, true);

        //pass attributes to EditText, make clearable
        editText = (EditText) findViewById(R.id.edittext);

        imageView = (ImageView)findViewById(R.id.mobile_icon2);


        boolean enabled = true;


        if (attrs != null) {
            TypedArray attrsArray =
                    getContext().obtainStyledAttributes(attrs, R.styleable.ClearableEditText);
            editText.setInputType(
                    attrsArray.getInt(
                            R.styleable.ClearableEditText_android_inputType, InputType.TYPE_CLASS_TEXT));
            editText.setHint(attrsArray.getString(R.styleable.ClearableEditText_android_hint));
            enabled = attrsArray.getBoolean(R.styleable.ClearableEditText_android_enabled, true);

          //  imageView.setPadding(R.styleable.ClearableEditText_imagePaddingLeft, 0, 0, 0);
         //   imageView.setPaddingRelative(dpAsPixels, 0, 0, 0);


        }


        if (enabled) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(onEditTextChangedListener!=null){
                        onEditTextChangedListener.onEditTextChanged(s.length() > 0);
                    }
                    if (s.length() > 0)
                        clearButton.setVisibility(RelativeLayout.VISIBLE);
                    else
                        clearButton.setVisibility(RelativeLayout.GONE);
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


    /*

     */
    public void setTextSize(float textSize) {
        editText.setTextSize(textSize);
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
    /*
    public void setMaxLength(int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }
    */

    public interface OnEditTextChangedListener{
        void onEditTextChanged(boolean flag);
    }
}