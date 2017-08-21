package com.wash.daoliu.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wash.daoliu.R;

/**
 * Created by zhengpingli on 2017/8/19.
 */

public class LoadingDialogGif extends ProgressDialog {

    private AnimationDrawable mAnimation;
    private Context mContext;

    //显示的图片
    private ImageView mImageView;

    //提示的文字
    private String mLoadingTip;
    private TextView mLoadingTv;
    private int mResId;
    private LinearLayout load_layout;

    private GifView load_gifv;

    //GIF动态图片
    private String showType;

    public static final String TYPE_GIF = "GIF";

    public static final String TYPE_IMG = "IMG";


    public LoadingDialogGif(Context context, String content, int resId, String type){
        super(context);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResId = resId;
        this.showType = type;
        setCanceledOnTouchOutside(true);
    }

    public LoadingDialogGif(Context context, String content, int resId, String type, int theme  ){
        super(context, theme);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResId = resId;
        this.showType = type;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog_gif);
        initView();
        initData();
    }


    /**
     * 页面初始化
     */
    private void initView() {
        mLoadingTv = (TextView) findViewById(R.id.load_tetv);
        mImageView = (ImageView) findViewById(R.id.load_imgv);
        load_layout = (LinearLayout) findViewById(R.id.load_layout);
        load_gifv = (GifView) findViewById(R.id.load_gifv);
    }
    /**
     * 数据初始化
     */
    private void initData() {
        if(showType.equals(TYPE_GIF)){
            load_gifv.setMovieResource(mResId);
            load_gifv.setVisibility(View.VISIBLE);
            load_layout.setVisibility(View.GONE);
        }else if(showType.equals(TYPE_IMG)){
            load_gifv.setVisibility(View.GONE);
            load_layout.setVisibility(View.VISIBLE);
            mImageView.setBackgroundResource(mResId);
            // 通过ImageView对象拿到背景显示的AnimationDrawable
            mAnimation = (AnimationDrawable) mImageView.getBackground();
            // 为了防止在onCreate方法中只显示第一帧的解决方案之一
            mImageView.post(new Runnable() {
                @Override
                public void run() {
                    mAnimation.start();
                }
            });
            mLoadingTv.setText(mLoadingTip);
        }
    }

    /**
     * 设置提示信息内容
     *
     * @param str
     */
    public void setContent(String str) {
        mLoadingTv.setText(str);
    }


}
