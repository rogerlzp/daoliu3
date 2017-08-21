package com.wash.daoliu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.utility.LTNConstants;

/**
 * Created by rogerlzp on 15/12/28.
 */
public class ProductBuySuccessActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ProductBuySuccessActivity.class.getSimpleName();

    Button btnNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_buy_success);
        initView();
    }

    public void initView() {
        findViewById(R.id.btn_next).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("投资成功");
        if(getIntent().getBooleanExtra("hasGoldenEgg",false)){
            Intent intent = new Intent(this, HitEggsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(LTNConstants.DT_URL, getIntent().getStringExtra("dturl"));
            // 砸金蛋的title
            //        bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "砸金蛋");
            bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_HOMEPAGE);
            intent.putExtras(bundle);
//                                    // 跳转回来
            startActivityForResult(intent, LTNConstants.FROM_WEBVIEW_INTERFACE);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.back_btn:
            case R.id.btn_next:
                Intent mIntent = new Intent(ProductBuySuccessActivity.this, MainActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mIntent);
                finish();
                // 跳转到查看我的投资页面
                //    Intent nIntent = new Intent(ProductBuySuccessActivity.this, MyInvestActivity.class);
                //    startActivity(nIntent);
                //   finish();
                break;


        }
    }


}