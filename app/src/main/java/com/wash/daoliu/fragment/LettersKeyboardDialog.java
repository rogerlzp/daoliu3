package com.wash.daoliu.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wash.daoliu.R;


/**
 * 输入
 * Created by WANG on 2016/8/30.
 */
public class LettersKeyboardDialog extends DialogFragment implements View.OnClickListener {

    private KeyboardView keyboardView;
    private OnChooseLetter onChooseLetter;
    private String[] letterAndDigit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.province_keyboard, container, false);
        initView(view);
        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.KeyboardDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        Window window = dialog.getWindow();
//        window.setBackgroundDrawableResource(R.drawable.sharebord);
        window.getAttributes().windowAnimations = R.style.dialogAnim;
        dialog.setContentView(R.layout.province_keyboard);
        dialog.setCanceledOnTouchOutside(false);
        // 设置宽度为屏宽、靠近屏幕底部。
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        return dialog;
    }

    public static LettersKeyboardDialog newInstance() {
        LettersKeyboardDialog f = new LettersKeyboardDialog();
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnChooseLetter) {
            onChooseLetter = (OnChooseLetter) activity;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpData();
    }

    private void setUpData() {
        Keyboard k = new Keyboard(getActivity(), R.xml.lettersanddigit_keyboard);
        keyboardView.setKeyboard(k);
        letterAndDigit = new String[]{"0","1", "2", "3", "4", "5", "6", "7", "8", "9"
                , "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"
                , "A", "S", "D", "F", "G", "H", "J", "K", "L"
                , "Z", "X", "C", "V", "B", "N", "M","港","澳","学","警","领"};
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        keyboardView = (KeyboardView) view.findViewById(R.id.keyboard_view);
        keyboardView.setEnabled(true);
        //设置为true时,当按下一个按键时会有一个popup来显示<key>元素设置的android:popupCharacters=""
        keyboardView.setPreviewEnabled(true);
        //设置键盘按键监听器
        keyboardView.setOnKeyboardActionListener(listener);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            // 按下一个按键后,设置相应的EditText的值
            // 然后切换为字母数字键
            if(primaryCode==112){
                onChooseLetter.onDeleteLetter();
            }else if (primaryCode < letterAndDigit.length) {
                onChooseLetter.onChooseLetter(letterAndDigit[primaryCode]);
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismissAllowingStateLoss();
                break;
        }
    }


    public interface OnChooseLetter {
        void onChooseLetter(String provice);
        void onDeleteLetter();
    }
}
