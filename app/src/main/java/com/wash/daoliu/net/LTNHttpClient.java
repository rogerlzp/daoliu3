package com.wash.daoliu.net;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.activities.MainActivity;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.utility.ActivityUtils;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;

import org.json.JSONObject;

/**
 * Created by rogerlzp on 15/11/24.
 */
public class LTNHttpClient extends AsyncHttpClient {


    private static LTNHttpClient mAsyncHttpClient;
    public Context context = null;


    public LTNHttpClient() {
    }

    public LTNHttpClient(Context context) {
        this.context = context;
    }

    public static LTNHttpClient getLTNHttpClient() {
        if (mAsyncHttpClient == null) {
            mAsyncHttpClient = new LTNHttpClient();
            mAsyncHttpClient.setConnectTimeout(10000);
            mAsyncHttpClient.setResponseTimeout(10000);
        }
        return mAsyncHttpClient;
    }

    public static LTNHttpClient getLTNHttpClient(Context context) {

        if (mAsyncHttpClient == null) {
            mAsyncHttpClient = new LTNHttpClient(context);
        }
        return mAsyncHttpClient;

    }

    public RequestHandle get(String url, RequestParams params, final JsonHttpResponseHandler responseHandler) {
        return post(url, params, responseHandler);
    }

//    public RequestHandle post(String url, RequestParams params, final JsonHttpResponseHandler responseHandler) {
//        return post(context, url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                if (checkResult(response)) {
//                    LogUtils.e(response.toString());
//                    responseHandler.onSuccess(statusCode, headers, response);
//                }
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                super.onSuccess(statusCode, headers, response);
//                LogUtils.e(response.toString());
//                responseHandler.onSuccess(statusCode, headers, response);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                super.onSuccess(statusCode, headers, responseString);
//                responseHandler.onSuccess(statusCode, headers, responseString);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                Utils.isNetworkConnected(LTNApplication.getInstance());
//                responseHandler.onFailure(statusCode, headers, responseString, throwable);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                Utils.isNetworkConnected(LTNApplication.getInstance());
//                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                Utils.isNetworkConnected(LTNApplication.getInstance());
//                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//        });
//    }

    public boolean checkResult(JSONObject response) {
        if (response == null) {
            return false;
        }
        if (response.has(LTNConstants.RESULT_CODE)) {
            String responseCode = response.optString(LTNConstants.RESULT_CODE);

            if (responseCode.equals(LTNConstants.MAG_SESSION)) {
                if (mAsyncHttpClient.context != null) {
                    ActivityUtils.finishAll();
                    LTNApplication.getInstance().clearUser();
                    Intent intent = new Intent(mAsyncHttpClient.context, MainActivity.class);
                    intent.putExtra(LTNConstants.SESSION_KEY_TIME_OUT, true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mAsyncHttpClient.context.startActivity(intent);
                }
                return false;
            }
            if (!responseCode.equals(LTNConstants.MSG_SUCCESS)) {
                if (response.has(LTNConstants.RESULT_MESSAGE)) {
                    LogUtils.e(response.toString());
                    Toast.makeText(LTNApplication.getInstance(), response.optString(LTNConstants.RESULT_MESSAGE), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }
        return true;
    }
}
