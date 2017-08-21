package com.wash.daoliu.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wash.daoliu.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by jiajia on 2017/4/11.
 */
public interface ImageLoader {
    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    void init(Context context);

    /**
     * 加载图片
     *
     * @param context
     * @param url         图片地址
     * @param img         ImageView
     * @param default_pic 默认占位图片
     */
    void displayImage(Context context, String url, ImageView img, int default_pic);

    /**
     * 加载图片
     *
     * @param context
     * @param drawable    本地图片id
     * @param img         ImageView
     * @param default_pic 默认占位图片
     */
    void displayImage(Context context, int drawable, ImageView img, int default_pic);

    /**
     * 加载图片
     *
     * @param context
     * @param url         图片地址
     * @param img         ImageView
     * @param default_pic 默认占位图片
     */

    void displayRoundedImage(Context context, String url, ImageView img, int default_pic);

    /**
     * 加载图片
     *
     * @param context
     * @param drawable    本地图片id
     * @param img         ImageView
     * @param default_pic 默认占位图片
     */
    void displayImage(Context context, int drawable, RoundedImageView img, int default_pic);

    /**
     * 暂停加载
     *
     * @param context
     */
    void resume(Context context);

    /**
     * 恢复加载
     *
     * @param context
     */
    void pause(Context context);
}
