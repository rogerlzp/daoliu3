package com.wash.daoliu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.LTNWebPageActivity;
import com.wash.daoliu.utility.LTNConstants;

/**
 * Created by jiajia on 2016/1/22.
 */
public class TrustView extends RelativeLayout implements View.OnClickListener{
    private TextView checkbox_agreement = null;
    private TextView trust_html = null;
    private String htmlUrl = null;
    private Context context = null;
    private OnTrustCheckChanged onTrustCheckChanged = null;
    public TrustView(Context context) {
        this(context, null);
    }
    public TrustView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.trust_view,null);
        checkbox_agreement = (TextView) view.findViewById(R.id.checkbox_agreement);
        trust_html = (TextView) view.findViewById(R.id.trust_html);
        this.addView(view, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void setOnTrustCheckChanged(OnTrustCheckChanged onTrustCheckChanged) {
        this.onTrustCheckChanged = onTrustCheckChanged;
    }

    /**
     *
     * @param trustText checkbox内容 (如：我已同意)
     * @param trustName 协议内容 (如：《投资协议》)
     * @param htmlUrl  协议对应html地址
     */
    public void setData(String trustText,String trustName,String htmlUrl){
        this.htmlUrl = htmlUrl;
        checkbox_agreement.setSelected(true);
        checkbox_agreement.setText(trustText);
        checkbox_agreement.setOnClickListener(this);
        trust_html.setText(trustName);
        trust_html.setOnClickListener(this);
    }
    public boolean isAgreeSelected(){
        return checkbox_agreement.isSelected();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkbox_agreement:
                checkbox_agreement.setSelected(!checkbox_agreement.isSelected());
                if(onTrustCheckChanged!=null){
                    onTrustCheckChanged.onChanged(checkbox_agreement.isSelected());
                }
                break;
            case R.id.trust_html:
                Intent intent = new Intent(context, LTNWebPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(LTNWebPageActivity.BUNDLE_URL, htmlUrl);
                bundle.putString(LTNConstants.FORWARD_URL, LTNConstants.BACK_TO_BINDCARD);
                intent.putExtras(bundle);
                context.startActivity(intent);
                break;
        }
    }
    public interface OnTrustCheckChanged{
        void onChanged(boolean flag);
    }

}
