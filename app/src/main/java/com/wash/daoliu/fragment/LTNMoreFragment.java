package com.wash.daoliu.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.FeedBackActivity;
import com.wash.daoliu.activities.LTNWebPageActivity;
import com.wash.daoliu.activities.LoginActivity;
import com.wash.daoliu.activities.SetHostActivity;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.imageloader.ImageLoaderProxy;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.LogUtils;
import com.wash.daoliu.utility.MarketUtils;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.utility.ViewUtils;


/**
 * 关于我们
 */
public class LTNMoreFragment extends BaseFragment implements OnClickListener {

    public static String FRAGMENT_TAG = LTNMoreFragment.class.getSimpleName();
    private View rootView;
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_more, null);
        initView();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
    //    UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_feedback:
                Intent intent = null;
                if (TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    Bundle b = new Bundle();
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.LOGIN_TO_FEEDBACK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtras(b);
                } else {
                    intent = new Intent(getActivity(), FeedBackActivity.class);
                }
                startActivity(intent);

                break;
            case R.id.tv_score:
                //TODO: 等在应用市场确定后在修改

                int result = MarketUtils.mainMarkets(getActivity());

                try {
                    Uri uri = Uri.parse("market://details?id=" + "com.wy.lingtouniao");
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    switch (result) {
                        case 1:
                            intent1.setPackage(MarketUtils.MARKET_YINGYONGBAO_APP);
                            break;
                        case 2:
                            intent1.setPackage(MarketUtils.MARKET_360_APP);
                            break;
                        default:
                            break;
                    }
                    startActivity(intent1);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "请先去安装应用宝或者其他主流应用市场!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_about:
                intent = new Intent(getActivity(), LTNWebPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.H5_ABOUT_URL);
                bundle.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "关于领投鸟理财");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_phone:
                ViewUtils.showCallDialog(getActivity(), LTNConstants.PHONE_NUMBER);
                break;
            case R.id.tv_share:
              //  ShareUtils.share(getActivity(), umShareListener);
                break;
            case R.id.set_host:
                intent = new Intent(getActivity(), SetHostActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_help:
                intent = new Intent(getActivity(), LTNWebPageActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString(LTNWebPageActivity.BUNDLE_URL, LTNConstants.ACCESS_URL.H5_HELP_URL);
                bundle1.putString(LTNWebPageActivity.BUNDLE_TITLEBAR, "帮助中心");
                intent.putExtras(bundle1);
                startActivity(intent);
                break;
        }
    }



    private void initView() {
        rootView.findViewById(R.id.back_btn).setVisibility(View.GONE);
        ((TextView) rootView.findViewById(R.id.title)).setText(getString(R.string.tab_more));
        rootView.findViewById(R.id.tv_feedback).setOnClickListener(this);
        rootView.findViewById(R.id.tv_score).setOnClickListener(this);
        rootView.findViewById(R.id.tv_about).setOnClickListener(this);
        rootView.findViewById(R.id.tv_phone).setOnClickListener(this);
        rootView.findViewById(R.id.tv_share).setOnClickListener(this);
        rootView.findViewById(R.id.tv_help).setOnClickListener(this);

        String versionCode = Utils.getAppVersionName(getActivity());
        if (!TextUtils.isEmpty(versionCode)) {
            rootView.findViewById(R.id.versionCode).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.versionCode)).setText("版本号：" + versionCode);
        }

        rootView.findViewById(R.id.rl_jpush).setVisibility(LogUtils.isDebug ? View.VISIBLE : View.GONE);
        ((TextView) rootView.findViewById(R.id.tv_jpush_id_value)).setText(LTNApplication.getInstance().getJPushID());

        ImageView imageview = (ImageView) rootView.findViewById(R.id.iv_banner);
        ImageLoaderProxy.getInstance().displayImage(getActivity(), R.drawable.icon_placeholder, imageview,R.drawable.ic_image_holder);
    }


    @Override
    protected void lazyLoad() {
        //在调用了onCreateView后并且fragment的UI是可见的就填充数据
        //如果是要下载网络数据，不是给view填充数据之类的，就不需要isPrepared参数了
        if (!isPrepared || !isVisible) return;
        //填充各控件的数据
    }


}
