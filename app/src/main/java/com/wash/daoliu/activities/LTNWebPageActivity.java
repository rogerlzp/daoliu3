package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.MyWebView;
import com.wash.daoliu.view.MyWebViewInterface;

public class LTNWebPageActivity extends BaseActivity {

    private LTNWebPageActivity mActivity;

    public static final String BUNDLE_TITLEBAR = "titleBar";
    public static final String BUNDLE_URL = "url";

    private MyWebView webView = null;

    private String titleBar;
    private String urlString = null;

    private Bundle bundle = null;
    private TextView tvTitle = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        LTNApplication.stackActivity.add(this);
        setContentView(R.layout.web_page);
        // 第一次过来接收Intent过来的值
        bundle = getIntent().getExtras();

        // 点击后需要回退到页面
        tvTitle = (TextView) findViewById(R.id.title);
        findViewById(R.id.back_btn).setOnClickListener(this);
        webView = (MyWebView) findViewById(R.id.webpage_view);
        webView.setTitleBarTextView(tvTitle);

        if (bundle.getString(LTNConstants.FORWARD_URL) != null) {
            if (bundle.getString(LTNConstants.FORWARD_URL).equals(LTNConstants.BACK_TO_BINDCARD)) {
                webView.setWebViewClient(LTNWebPageActivity.this);
                webView.setJSInterface(new MyWebViewInterface(LTNWebPageActivity.this), LTNConstants.JSInterface);
            } else if (bundle.getString(LTNConstants.FORWARD_URL).equals(LTNConstants.BACK_TO_DEPOSIT)) {
                webView.setWebViewClient(LTNWebPageActivity.this);
                webView.setJSInterface(new MyWebViewInterface(LTNWebPageActivity.this), LTNConstants.JSInterface);
            } else if (bundle.getString(LTNConstants.FORWARD_URL).equals(LTNConstants.BACK_TO_WITHDRAW)) {
                webView.setWebViewClient(LTNWebPageActivity.this);
                webView.setJSInterface(new MyWebViewInterface(LTNWebPageActivity.this), LTNConstants.JSInterface);
            } else if (bundle.getString(LTNConstants.FORWARD_URL).equals(LTNConstants.BACK_TO_PRODUCT_BUY)) {
                webView.setWebViewClient(LTNWebPageActivity.this);
                webView.setJSInterface(new MyWebViewInterface(LTNWebPageActivity.this), LTNConstants.JSInterface);
            } else if (bundle.getString(LTNConstants.FORWARD_URL).equals(LTNConstants.BACK_TO_CURRENT)) {
                webView.setWebViewClient(LTNWebPageActivity.this);
                webView.setJSInterface(new MyWebViewInterface(LTNWebPageActivity.this), LTNConstants.JSInterface);
            } else if (bundle.getString(LTNConstants.FORWARD_URL).equals(LTNConstants.BACK_TO_MIANMI)) {
                webView.setWebViewClient(LTNWebPageActivity.this);
                webView.setJSInterface(new MyWebViewInterface(LTNWebPageActivity.this), LTNConstants.JSInterface);
            } else if (bundle.getString(LTNConstants.FORWARD_URL).equals(LTNConstants.BACK_TO_HOMEPAGE)) {
                webView.setWebViewClient(LTNWebPageActivity.this);
                webView.setJSInterface(new MyWebViewInterface(LTNWebPageActivity.this), LTNConstants.JSInterface);
            }
//            else if (bundle.getString(LTNConstants.FORWARD_URL).equals(LTNConstants.BACK_TO_HOMEPAGE)) {
//                webView.setWebViewClient(LTNWebPageActivity.this);
//                webView.setJSInterface(new MyWebViewInterface(LTNWebPageActivity.this), LTNConstants.JSInterface);
//            }
        }

//        initTitleBar();

        initData();

    }

    public void initTitleBar() {
//        tvTitle.setText(getString(R.string.app_name));
//        leftMenuButton.setVisibility(View.VISIBLE);
//        leftMenuButton.setImageResource(R.drawable.nav_return);
//        rightMenuButton.setVisibility(View.INVISIBLE);
//        rightMenuCommit.setVisibility(View.GONE);
//        leftMenuButton.setOnClickListener(new MyOnClickEffectiveListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, titleBar);
        outState.putString(LTNWebPageActivity.BUNDLE_URL, urlString);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        titleBar = savedInstanceState.getString(LTNWebPageActivity.BUNDLE_TITLEBAR);
        urlString = savedInstanceState.getString(LTNWebPageActivity.BUNDLE_URL);
    }

    protected void initData() {
        urlString = bundle.getString(BUNDLE_URL);

        titleBar = bundle.getString(BUNDLE_TITLEBAR);
        webView.setTitlebarText(titleBar);

        if (ViewUtils.isNetwork(mActivity)) {
            webView.startLoad(urlString);
        }


    }

//    private class MyOnClickEffectiveListener extends OnClickEffectiveListener {
//
//        @Override
//        public void onClickEffective(View view) {
//            clickEffective(view);
//        }
//    }
//
//    public void clickEffective(View v) {
//        int viewId = v.getId();
//        switch (viewId) {
//            case R.id.back_btn:
//                if (webView.canGoBack()) {
//                    webView.goBack();
//                } else {
//                    finish();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_btn:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // @Override
    public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
        //  super.shouldOverrideUrlLoading(view, urlString);
        return true;

    }
}
