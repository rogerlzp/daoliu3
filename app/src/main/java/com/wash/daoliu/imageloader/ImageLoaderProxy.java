package com.wash.daoliu.imageloader;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * ImageLoader代理类
 * Created by jiajia on 2017/4/11.
 */
public class ImageLoaderProxy implements ImageLoader{
    private ImageLoader imageLoader;
    private static ImageLoaderProxy imageLoaderProxy;

    public static ImageLoaderProxy getInstance(){
        if(imageLoaderProxy==null){
            imageLoaderProxy = new ImageLoaderProxy();
        }
        return imageLoaderProxy;
    }
    private ImageLoaderProxy(){
        imageLoader = new GlideImageLoader();
    }
    @Override
    public void init(Context context) {
        imageLoader.init(context);
    }

    @Override
    public void displayImage(Context context, String url, ImageView img, int default_pic) {
        imageLoader.displayImage(context,url,img,default_pic);
    }
    @Override
    public void displayRoundedImage(Context context, String url, ImageView img, int default_pic) {

        imageLoader.displayRoundedImage(context,url,img,default_pic);
    }

    @Override
    public void displayImage(Context context, int drawable, ImageView img, int default_pic) {
        imageLoader.displayImage(context,drawable,img,default_pic);
    }
    @Override
    public void displayImage(Context context, int drawable, RoundedImageView img, int default_pic) {
        imageLoader.displayImage(context,drawable,img,default_pic);
    }

    @Override
    public void resume(Context context) {
        imageLoader.resume(context);
    }

    @Override
    public void pause(Context context) {
        imageLoader.pause(context);
    }
}
