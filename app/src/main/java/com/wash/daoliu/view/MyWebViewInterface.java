package com.wash.daoliu.view;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.wash.daoliu.activities.AccountInfoActivity;
import com.wash.daoliu.activities.BirdDetailActivity;
import com.wash.daoliu.activities.HitEggsActivity;
import com.wash.daoliu.activities.LTNWebPageActivity;
import com.wash.daoliu.utility.LTNConstants;

/**
 * Created by rogerlzp on 15/12/25.
 */
public class MyWebViewInterface {
    private static final String TAG = MyWebViewInterface.class.getSimpleName();
    Context mContext;

    public MyWebViewInterface(Context ctx) {
        mContext = ctx;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        // Log.d(TAG, "showToast");
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        Intent mIntent = new Intent(mContext, AccountInfoActivity.class);
        mContext.startActivity(mIntent);
    }


    // 充值之后跳转页面
    @JavascriptInterface
    public void notifyAndroid_gotoDeposit() {
        // Log.d(TAG, "notifyAndroid_gotoDeposit");
        // Intent mIntent = new Intent(mContext, AccountInfoActivity.class);
        // mContext.startActivity(mIntent);
        ((LTNWebPageActivity) mContext).setResult(LTNConstants.DEPOSIT_RESULT_SUCCESS);
        ((LTNWebPageActivity) mContext).finish();
    }

    // 绑卡之后跳转页面
    @JavascriptInterface
    public void notifyAndroid_gotoBindcard() {
        // Log.d(TAG, "notifyAndroid_gotoBindcard");
        //  Intent mIntent = new Intent(mContext, BindBankCardActivity.class);
        // ((LTNWebPageActivity) mContext).startActivityForResult(mIntent, LTNConstants.FROM_WEBVIEW_INTERFACE);
        /*
        Intent intent =
                ((LTNWebPageActivity) mContext).getIntent();
        Bundle bundle = ((LTNWebPageActivity) mContext).getIntent().getExtras();
        if (bundle != null) {
            bundle = new Bundle();
        }
        bundle.putString(LTNConstants.BIND_CARD_RESULT, LTNConstants.BIND_CARD_RESULT_SUCCESS);
        intent.putExtras(bundle);
        */

        Intent intent =
                ((LTNWebPageActivity) mContext).getIntent();
        if (intent != null) {
            intent = new Intent();
        }
        ((LTNWebPageActivity) mContext).setResult(LTNConstants.BIND_CARD_RESULT_SUCCESS, intent);
        ((LTNWebPageActivity) mContext).finish();
    }


    // 提现之后跳转页面
    @JavascriptInterface
    public void notifyAndroid_gotoWithdraw() {
        // Log.d(TAG, "notifyAndroid_gotoWithdraw");
        // Intent mIntent = new Intent(mContext, AccountInfoActivity.class);
        //  mContext.startActivity(mIntent);
        ((LTNWebPageActivity) mContext).setResult(LTNConstants.WITHDRAW_RESULT_SUCCESS);
        ((LTNWebPageActivity) mContext).finish();
    }


    // 购买之后跳转页面
    @JavascriptInterface
    public void notifyAndroid_gotoTransfer() {
        // Log.d(TAG, "notifyAndroid_gotoTransfer");
        //  Intent mIntent = new Intent(mContext, BindBankCardActivity.class);
        // ((LTNWebPageActivity) mContext).startActivityForResult(mIntent, LTNConstants.FROM_WEBVIEW_INTERFACE);
        ((LTNWebPageActivity) mContext).setResult(LTNConstants.PRODUCT_BUY_SUCCESS);
        ((LTNWebPageActivity) mContext).finish();
    }

    // 随心投购买之后跳转页面
    @JavascriptInterface
    public void notifyAndroid_gotoCurrentDeposit() {
        // Log.d(TAG, "notifyAndroid_gotoTransfer");
        //  Intent mIntent = new Intent(mContext, BindBankCardActivity.class);
        // ((LTNWebPageActivity) mContext).startActivityForResult(mIntent, LTNConstants.FROM_WEBVIEW_INTERFACE);
        ((LTNWebPageActivity) mContext).setResult(LTNConstants.CURRENT_BUY_SUCCESS);
        ((LTNWebPageActivity) mContext).finish();
    }

    // 购买之后跳转页面
    @JavascriptInterface
    public void notifyAndroid_goBackToApp() {
        // Log.d(TAG, "notifyAndroid_goBackToApp");
        //  Intent mIntent = new Intent(mContext, BindBankCardActivity.class);
        // ((LTNWebPageActivity) mContext).startActivityForResult(mIntent, LTNConstants.FROM_WEBVIEW_INTERFACE);
        ((HitEggsActivity) mContext).setResult(LTNConstants.ZAJIDAN_SUCCESS);
        ((HitEggsActivity) mContext).finish();
    }


    // 免密协议签署成功后,跳转的页面
    @JavascriptInterface
    public void notifyAndroid_gotoGrant() {
        // Log.d(TAG, "notifyAndroid_gotoTransfer");
        //  Intent mIntent = new Intent(mContext, BindBankCardActivity.class);
        // ((LTNWebPageActivity) mContext).startActivityForResult(mIntent, LTNConstants.FROM_WEBVIEW_INTERFACE);
        Intent intent = new Intent();
        intent.putExtra(LTNConstants.AGREEMENT_CZ, "1");
        ((LTNWebPageActivity) mContext).setResult(LTNConstants.GRANT_RESULT_SUCCESS, intent);

        ((LTNWebPageActivity) mContext).finish();
    }
    @JavascriptInterface
    public void notifyAndroid_gotoTopUpCancel() {
        // Log.d(TAG, "notifyAndroid_gotoTransfer");
        //  Intent mIntent = new Intent(mContext, BindBankCardActivity.class);
        // ((LTNWebPageActivity) mContext).startActivityForResult(mIntent, LTNConstants.FROM_WEBVIEW_INTERFACE);
        Intent intent = new Intent();
        intent.putExtra(LTNConstants.AGREEMENT_CZ, "0");
        ((LTNWebPageActivity) mContext).setResult(LTNConstants.GRANT_RESULT_SUCCESS, intent);

        ((LTNWebPageActivity) mContext).finish();
    }


    // 换卡申请提请成功后跳转页面
    @JavascriptInterface
    public void notifyAndroid_gotoChangeCard() {
        // Log.d(TAG, "notifyAndroid_gotoTransfer");
        //  Intent mIntent = new Intent(mContext, BindBankCardActivity.class);
        // ((LTNWebPageActivity) mContext).startActivityForResult(mIntent, LTNConstants.FROM_WEBVIEW_INTERFACE);
        ((LTNWebPageActivity) mContext).setResult(LTNConstants.CARD_CHANGE_SUCCESS);
        ((LTNWebPageActivity) mContext).finish();
    }


    // 换卡申请提请成功后跳转页面
    @JavascriptInterface
    public void notifyAndroid_goToBirdCoinsList() {
        // Log.d(TAG, "notifyAndroid_gotoTransfer");
        //  Intent mIntent = new Intent(mContext, BindBankCardActivity.class);
        // ((LTNWebPageActivity) mContext).startActivityForResult(mIntent, LTNConstants.FROM_WEBVIEW_INTERFACE);
//        ((LTNWebPageActivity) mContext).setResult(LTNConstants.CHECK_BIRDCOIN_SUCCESS);
//        ((LTNWebPageActivity) mContext).finish();
        Intent intent = new Intent(mContext, BirdDetailActivity.class);
        mContext.startActivity(intent);
    }

}
