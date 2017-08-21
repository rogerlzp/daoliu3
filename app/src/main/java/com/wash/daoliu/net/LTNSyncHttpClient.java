package com.wash.daoliu.net;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by rogerlzp on 15/12/17.
 */
public class LTNSyncHttpClient {

    private static SyncHttpClient mSyncHttpClient;

    public static SyncHttpClient getLTNHttpClient() {
        if (mSyncHttpClient == null) {
            mSyncHttpClient = new SyncHttpClient();
        }
        return mSyncHttpClient;
    }

    /**
     * 执行 get 请求
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mSyncHttpClient.get(url, params, responseHandler);
    }


    /**
     * 执行 post 请求
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mSyncHttpClient.post(url, params, responseHandler);
    }

}
