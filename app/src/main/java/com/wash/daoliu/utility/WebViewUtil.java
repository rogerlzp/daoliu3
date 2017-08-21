package com.wash.daoliu.utility;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by bobo on 2015/9/12.
 */
public class WebViewUtil {

    private Context context;

    public WebViewUtil() {
    }

    public WebViewUtil(Context context) {
        this.context = context;
    }

    public void doInterfaceEvent(String url, WebView webView) {
        if (StringUtils.isNotEmpty(url)) {
            webView.loadUrl(url);
        }

    }

}
