package com.wash.daoliu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
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
import com.wash.daoliu.utility.ViewUtils;

/**
 * Created by rogerlzp on 15/12/24.
 */
public class MyEditText extends RelativeLayout {

    private static final String TAG = "MyEditText";

    /**
     * EditText component
     */
    private EditText editText;

    /**
     * Button that clears the EditText contents
     */
    private ImageButton clearButton;


    private ImageView imageView;

    private RelativeLayout relativeLayout;

    private boolean isRun = false;

    private String d = "";
    private boolean bankType = false;
    /**
     * Additional listener fired when cleared
     */
    private OnClickListener onClickClearListener;


    public MyEditText(Context context) {
        super(context);
        init(null);
    }


    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Initialize view
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {


        LayoutInflater inflater
                = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_myedittext, this, true);

        //pass attributes to EditText, make clearable
        editText = (EditText) findViewById(R.id.edittext);

        imageView = (ImageView) findViewById(R.id.mobile_icon2);

        relativeLayout = (RelativeLayout) findViewById(R.id.rl1);

        boolean enabled = true;
        if (attrs != null) {
            TypedArray attrsArray =
                    getContext().obtainStyledAttributes(attrs, R.styleable.MyEditText);
            editText.setInputType(
                    attrsArray.getInt(
                            R.styleable.MyEditText_android_inputType, InputType.TYPE_CLASS_TEXT));
            editText.setHint(attrsArray.getString(R.styleable.MyEditText_android_hint));
            enabled = attrsArray.getBoolean(R.styleable.MyEditText_android_enabled, true);

            imageView.setImageDrawable(getResources().getDrawable(attrsArray.getResourceId(R.styleable.MyEditText_imageID,
                    R.drawable.icon_mobileno))); // default to icon_mobileno

            imageView.setPadding(ViewUtils.dip2px(this.getContext(), attrsArray.getInteger(R.styleable.MyEditText_imageLeft, 14)), 0, 0, 0);

            clearButton = (ImageButton) findViewById(R.id.button_clear);

            RelativeLayout.LayoutParams lp = (LayoutParams) clearButton.getLayoutParams();
            lp.setMargins(0, 0, ViewUtils.dip2px(this.getContext(), attrsArray.getInteger(R.styleable.MyEditText_clearRight, 14)), 10);
            clearButton.setLayoutParams(lp);
        }


        if (enabled) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!bankType) {

                        if (s.length() > 0)
                            clearButton.setVisibility(RelativeLayout.VISIBLE);
                        else
                            clearButton.setVisibility(RelativeLayout.GONE);
                    } else {
                        if (isRun) {
                            isRun = false;
                            return;
                        }
                        isRun = true;
                        d = "";
                        String newStr = s.toString();
                        newStr = newStr.replace(" ", "");

                        int index = 0;
                        while ((index + 4) < newStr.length()) {
                            d += (newStr.substring(index, index + 4) + " ");
                            index += 4;
                        }
                        d += (newStr.substring(index, newStr.length()));

                        int i = getEditTextCursorIndex(editText);


                        editText.setText(d);
                        try {

                            if (i % 5 == 0 && before == 0) {
                                if (i + 1 <= d.length()) {//判断位数再设置，否则在第四位的时候按空格程序会崩掉
                                    editText.setSelection(i + 1);
                                } else {
                                    editText.setSelection(d.length());
                                }
                            } else if (before == 1 && i < d.length()) {
                                editText.setSelection(i);
                            } else if (before == 0 && i < d.length()) {
                                editText.setSelection(i);
                            } else
                                editText.setSelection(d.length());
                        } catch (Exception e) {
                           // Log.d(TAG, e.getMessage());
                        }

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

    private int getEditTextCursorIndex(EditText mEditText) {
        return mEditText.getSelectionStart();
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
    public void setMaxLength(int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void setBankCardTypeOn() {
        this.bankType = true;
    }
}