package com.wash.daoliu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.wash.daoliu.activities.OnClickEffectiveListener;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.view.LoadingDialog;

public abstract class BaseFragment extends Fragment {

    protected static String TAG = BaseFragment.class.getSimpleName();
    /**
     * when activity is recycled by system, isFirstTimeStartFlag will be reset to default true,
     * when activity is recreated because a configuration change for example screen rotate, isFirstTimeStartFlag will stay false
     */
    private boolean isFirstTimeStartFlag = true;

    protected final static int FIRST_TIME_START = 0; //when activity is first time start
    protected final static int SCREEN_ROTATE = 1;    //when activity is destroyed and recreated because a configuration change, see setRetainInstance(boolean retain)
    protected final static int ACTIVITY_DESTROY_AND_CREATE = 2;  //when activity is destroyed because memory is too low, recycled by android system

    public LTNApplication app;

    public boolean isVisible;


    public LoadingDialog loadingDialog;

    public void showLoadingProgressDialog(Context context, String dialogText) {
        this.showProgressDialog(context, dialogText);
    }

    public void showProgressDialog(Context context, CharSequence message) {
        if (this.loadingDialog == null) {
            this.loadingDialog = new LoadingDialog(context);
            this.loadingDialog.setTitle(message);
            this.loadingDialog.setCancelable(true);
            //     this.loadingDialog.setIndeterminate(true);
            this.loadingDialog.setCanceledOnTouchOutside(false);
        }
        if (!this.loadingDialog.isShowing()) {
            this.loadingDialog.show();
        }

    }

    public void dismissProgressDialog() {
        if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
            this.loadingDialog.dismiss();
        }
    }

    public BaseOnClickEffectiveListener baseOnClickEffectiveListener;

    protected int getCurrentState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            isFirstTimeStartFlag = false;
            return ACTIVITY_DESTROY_AND_CREATE;
        }

        if (!isFirstTimeStartFlag) {
            return SCREEN_ROTATE;
        }

        isFirstTimeStartFlag = false;
        return FIRST_TIME_START;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log.v(TAG, "onCreate");
        setRetainInstance(false);

        app = (LTNApplication) this.getActivity().getApplication();

        baseOnClickEffectiveListener = new BaseOnClickEffectiveListener();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible() {
    }


    @Override
    public void onStart() {
        // Log.v(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Log.v(TAG, "onResume");
    }

    @Override
    public void onPause() {
        // Log.v(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        // Log.v(TAG, "onStop");
        super.onStop();
    }


    @Override
    public void onDestroy() {
        // Log.v(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        // Log.v(TAG, "onDestroyView");
        super.onDestroyView();
    }

    private class BaseOnClickEffectiveListener extends OnClickEffectiveListener {

        @Override
        public void onClickEffective(View view) {
            clickEffective(view);
        }
    }

    public void clickEffective(View view) {
    }


}

