package com.wash.daoliu.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wash.daoliu.R;
import com.wash.daoliu.entity.JavaScriptObject;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.view.MyWebView;
import com.wash.daoliu.view.MyWebViewInterface;

/**
 * Created by rogerlzp on 16/1/4.
 */
public class HitEggsActivity extends BaseActivity implements View.OnClickListener {
    private MyWebView webView = null;
    String eggUrl = null;
    private JavaScriptObject javaScriptObject = new JavaScriptObject();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hit_eggs_dialog_fragment);
        if (getIntent().getExtras() != null) {
            eggUrl = getIntent().getExtras().getString(LTNConstants.DT_URL);
        }
        initView();
    }

    private void initView() {
        webView = (MyWebView) findViewById(R.id.web_view);
        webView.setWebViewClient(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setJSInterface(new MyWebViewInterface(HitEggsActivity.this), LTNConstants.JSInterface);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        findViewById(R.id.close).setOnClickListener(this);
        webView.setBackgroundResource(R.color.hit_egg_bg);
        webView.getLayoutParams().height = (dm.widthPixels - ViewUtils.dip2px(this, 80)) * dm.heightPixels / dm.widthPixels;
        webView.loadUrl(eggUrl);
//        webView.loadUrl("http://192.168.18.15:9999/Desktop/wuyou/trunk_20160118/src/main/webapp/h5/drawaward.html");
        Window window = getWindow();
        WindowManager.LayoutParams mParams = window.getAttributes();
        mParams.height = dm.heightPixels;
        mParams.width = dm.widthPixels;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
        }
    }
}
