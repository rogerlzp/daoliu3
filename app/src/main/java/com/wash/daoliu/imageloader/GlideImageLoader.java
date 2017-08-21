package com.wash.daoliu.imageloader;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wash.daoliu.R;
import com.wash.daoliu.utility.ViewUtils;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by jiajia on 2017/4/11.
 */
public class GlideImageLoader implements ImageLoader {
    @Override
    public void init(Context context) {
    }

    @Override
    public void displayImage(Context context, String url, ImageView img, int default_pic) {
        // Glide.with(context).load(url).placeholder(R.drawable.ic_image_holder).error(R.drawable.ic_image_holder).into(img);
        Glide.with(context).load(url).placeholder(R.drawable.icon_placeholder).crossFade().into(img);

        // Glide.with(context).load(url).placeholder(R.drawable.ic_image_holder).error(R.drawable.ic_image_holder).into(img);
    }

    @Override
    public void displayRoundedImage(Context context, String url, ImageView img, int default_pic) {
        // Glide.with(context).load(url).placeholder(R.drawable.ic_image_holder).error(R.drawable.ic_image_holder).into(img);
        final ObjectAnimator anim = ObjectAnimator.ofInt(img, "ImageLevel", 0, 6);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();

        Glide.with(context).load(url).placeholder(R.drawable.icon_placeholder).crossFade()
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    //  @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        anim.cancel();
//                        return false;
//                    }
//
//                    //    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        anim.cancel();
//                        return false;
//                    }
//                })
// .
                .bitmapTransform(
                        new RoundedCornersTransformation(context, ViewUtils.dip2px(context, 10), 0,
                                RoundedCornersTransformation.CornerType.ALL)).into(img);

        // Glide.with(context).load(url).placeholder(R.drawable.ic_image_holder).error(R.drawable.ic_image_holder).into(img);
    }

    @Override
    public void displayImage(Context context, int drawable, ImageView img, int default_pic) {
        Glide.with(context).load(drawable).placeholder(R.drawable.icon_placeholder).crossFade().into(img);
        // Glide.with(context).load(drawable).placeholder(R.drawable.ic_image_holder).error(R.drawable.ic_image_holder).into(img);

    }

    @Override
    public void displayImage(Context context, int drawable, RoundedImageView img, int default_pic) {
        Glide.with(context).load(drawable).placeholder(R.drawable.icon_placeholder).bitmapTransform(new RoundedCornersTransformation(context, 30, 0,
                RoundedCornersTransformation.CornerType.ALL)).into(img);
        // Glide.with(context).load(drawable).placeholder(R.drawable.ic_image_holder).error(R.drawable.ic_image_holder).into(img);

    }


    @Override
    public void resume(Context context) {
        Glide.with(context).resumeRequests();
    }

    @Override
    public void pause(Context context) {
        Glide.with(context).pauseRequests();
    }
}
