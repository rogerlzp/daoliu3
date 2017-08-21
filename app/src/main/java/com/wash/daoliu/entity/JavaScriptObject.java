package com.wash.daoliu.entity;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.wash.daoliu.activities.LTNWebPageActivity;

public class JavaScriptObject {

    private LTNWebPageActivity activity;

    private WebView webView;

    private String shareJson;

    public JavaScriptObject() {
    }

    /**
     * 分享(本地调用)
     */
    @JavascriptInterface
    public String doShare(String json) {
        shareJson = json;
        return json;
    }

    public String getShareJson() {
        return shareJson;
    }

    /**
     * 分享成功回调
     */
    @JavascriptInterface
    public void doShareStatus(int status) {//1 true、 0 false
        webView.loadUrl("javascript:doShareStatus('" + status + "')");
    }

    public void setActivity(LTNWebPageActivity activity) {
        this.activity = activity;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

}
