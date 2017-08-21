package com.wash.daoliu.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wash.daoliu.R;
import com.wash.daoliu.model.User;

/**
 * Created by rogerlzp on 16/1/7.
 */
public class ForgetTradePwdActivity extends BaseActivity implements View.OnClickListener {

    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    Intent sentIntent = new Intent(SENT_SMS_ACTION);
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_trade_pwd);
        initView();
    }

    public void initView() {
        ((TextView) findViewById(R.id.title)).setText("重置支付密码");
        ((Button) findViewById(R.id.btn_reset_trade_pwd)).setOnClickListener(this);
         findViewById(R.id.back_btn).setOnClickListener(this);
    }

    private void sendSMS(String number, String message){
        String SENT = "sms_sent";
        String DELIVERED = "sms_delivered";
        PendingIntent sentPI = PendingIntent.getActivity(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getActivity(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                       // Log.i("====>", "Activity.RESULT_OK");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                       // Log.i("====>", "RESULT_ERROR_GENERIC_FAILURE");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                       // Log.i("====>", "RESULT_ERROR_NO_SERVICE");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                       // Log.i("====>", "RESULT_ERROR_NULL_PDU");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                       // Log.i("====>", "RESULT_ERROR_RADIO_OFF");
                        break;
                }
            }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                       // Log.i("====>", "RESULT_OK");
                        break;
                    case Activity.RESULT_CANCELED:
                       // Log.i("=====>", "RESULT_CANCELED");
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        SmsManager smsm = SmsManager.getDefault();
        smsm.sendTextMessage(number, null, message, sentPI, deliveredPI);
    }
//    public void doSendSMSTo(String phoneNumber, String message) {
//        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
//        intent.putExtra("sms_body", message);
//        startActivity(intent);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset_trade_pwd:
                // TODO: 确认怎么修改?
                if(User.getUserInstance().getUserInfo()==null){
                    Toast.makeText(this,R.string.reset_trand_passwpord_error,Toast.LENGTH_SHORT).show();
                    return;
                }
                String cardId =  User.getUserInstance().getUserInfo().getCardId();
                if(TextUtils.isEmpty(cardId)){
                    Toast.makeText(this,R.string.reset_trand_passwpord_error,Toast.LENGTH_SHORT).show();
                    return;
                }
                sendSMS("10690569687","CSMM#"+ cardId.substring(cardId.length()-4,cardId.length()));
                finish();
                break;
            case R.id.back_btn:
                finish();
                break;

        }
    }
}