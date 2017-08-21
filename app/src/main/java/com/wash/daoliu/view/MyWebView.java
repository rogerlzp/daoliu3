package com.wash.daoliu.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.*;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.entity.JavaScriptObject;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.SharedPrefsUtil;
import com.wash.daoliu.utility.StringUtils;
import com.wash.daoliu.utility.ViewUtils;
import com.wash.daoliu.utility.WebViewUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bobo on 2015/9/14.
 */
public class MyWebView extends WebView {

    private ProgressBar progressbar;

    private TextView titleBarTextView;

    private JavaScriptObject javaScriptObject = new JavaScriptObject();

    private WebViewUtil webViewUtil;

    private String urlString = "";

    private Map<String, String> headerMap = new HashMap<String, String>();

    private Context mContext;

    private MyWebViewClient myWebViewCLient = new MyWebViewClient();

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    public void setWebViewClient(Context context) {
        myWebViewCLient.setContext(context);
    }

    @SuppressLint("AddJavascriptInterface")
    public void setJSInterface(MyWebViewInterface myWebViewInterface, String interfaceName) {
        this.addJavascriptInterface(myWebViewInterface, interfaceName);

    }


    @SuppressLint("AddJavascriptInterface")
    public void init(Context context) {
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0));
        progressbar.setProgress(0);
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable));

        addView(progressbar);

        webViewUtil = new WebViewUtil(getContext());

        getSettings().setBuiltInZoomControls(true);
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        setWebChromeClient(new MyWebChromeClient());

        //   myWebViewCLient = new MyWebViewClient();
        // myWebViewCLient.setContext(context);
        setWebViewClient(myWebViewCLient);

        //  setWebViewClient(new MyWebViewClient());
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setSupportMultipleWindows(true);
        getSettings().setUseWideViewPort(true);

        getSettings().setLoadWithOverviewMode(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        addJavascriptInterface(javaScriptObject, "javaScriptObject");
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        requestFocus();
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setUserAgentString(getSettings().getUserAgentString()
                + " DeviceNo/"
                + SharedPrefsUtil.getString(getContext(), Constant.Share.SHARE_IMEI));

        //添加header信息
        headerMap.put("deviceNo", SharedPrefsUtil.getString(getContext(), Constant.Share.SHARE_IMEI));

        // add download listener
        this.setDownloadListener(new MyWebViewDownLoadListener());

    }

    public class MyWebChromeClient extends WebChromeClient {
        /*public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            //构建一个Builder来显示网页中的alert对话框
            showAlertDialog(message, 1);
            return true;
        }

        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            //构建一个Builder来显示网页中的alert对话框
            showAlertDialog(message, 2);
            return true;
        }*/

        //设置网页加载的进度条
        public void onProgressChanged(WebView view, int progress) {
            if (progress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(progress);
            }
            super.onProgressChanged(view, progress);
        }

        //设置应用程序的标题
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    protected class MyWebViewClient extends WebViewClient {
        private Context mContext = null;

        public void setContext(Context context) {
            mContext = context;
        }


        @Override
        // 在WebView中显示页面,而不是默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // Log.d("MyWebView", "url:" + url);

            if (url.indexOf("tel:") != -1) {
                return true;
            }
            //对scheme开头的，直接打开对应的scheme
            if (!(url.startsWith("http:") || url.startsWith("https:"))) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
                return true;
            }

            webViewUtil.doInterfaceEvent(url, view);
            setTitlebarText(view.getTitle());

            progressbar.setProgress(100);
            progressbar.setVisibility(View.GONE);

            if (url.contains(LTNConstants.BIND_CARD_RETURN_URL) || url.contains(LTNConstants.DEPOSIT_RETURN_URL)
                    || url.contains(LTNConstants.WITHDRAW_RETURN_URL)) {
                // 时间和Web时间相同,等着后面来确定
                // TODO: 和前台web确定时间
                TimeCounter timeCounter = new TimeCounter(6000, 1000);
                timeCounter.start();
            }
            return true;
        }

        public class TimeCounter extends CountDownTimer {

            public TimeCounter(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
            }

            @Override
            public void onFinish() {// 计时完毕

                // TODO:
                if (mContext != null) {
                    //      ((Activity) mContext).finish();
                }

            }

            @Override
            public void onTick(long millisUntilFinished) {// 计时过程
                // TODO
            }
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            urlString = url;
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setTitlebarText(view.getTitle());

            progressbar.setProgress(100);
            progressbar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
//            showWebError();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (request.getUrl().toString().equals(urlString)) {
                showWebError();
            }
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //接受证书
            handler.proceed();
        }

    }

    public void showWebError() {
        ViewUtils.showWarnDialog2(getContext(), "加载失败了!", "重试", "取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canGoBack()) {
                    goBack();
                } else {
                    ((Activity) mContext).finish();
                }
            }
        });
    }

    public void startLoad() {
        if (StringUtils.isNotEmpty(urlString)) {
            this.loadUrl(urlString);
        }
    }

    public void startLoad(String urlString) {
        if (StringUtils.isNotEmpty(urlString)) {
            this.loadUrl(urlString);
        }
    }

    public void setTitleBarTextView(TextView titleBarTextView) {
        this.titleBarTextView = titleBarTextView;
    }

    public void setTitlebarText(String titleText) {
        if (titleBarTextView != null) {
            this.titleBarTextView.setText(titleText);
        }
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public WebViewUtil getWebViewUtil() {
        return webViewUtil;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }


    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,

                                    long contentLength) {

            Uri uri = Uri.parse(url);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            mContext.startActivity(intent);

        }

    }
}
