package com.wash.daoliu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wash.daoliu.R;

/**
 * Created by Super on 2015/8/11.
 */
public class MyImageButtonWithText extends LinearLayout{
    private int mySrc;
    private String text;
    private int textColor;
    LinearLayout linearLayout;

    ImageView imageView;
    TextView textView;

    Context context;

    Paint paint;



    public MyImageButtonWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        this.context = context;
//        linearLayout = (LinearLayout) LinearLayout.inflate(context,R.layout.my_image_button_with_text,null);
        linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.my_image_button_with_text, this, true);
//        linearLayout = (LinearLayout) View.inflate(context, R.layout.my_image_button_with_text,null);
        imageView = (ImageView) linearLayout.findViewById(R.id.imageview);
        textView = (TextView) linearLayout.findViewById(R.id.textview);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MyImageButtonWithText);
        mySrc = a.getResourceId(R.styleable.MyImageButtonWithText_mySrc, R.mipmap.ic_launcher);
        text = a.getString(R.styleable.MyImageButtonWithText_myText);
        textColor = a.getColor(R.styleable.MyImageButtonWithText_myTextColor,1);
        a.recycle();
        imageView.setImageResource(mySrc);
        textView.setText(text);
        textView.setTextColor(textColor);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),mySrc),0,0,paint);
//        paint.setColor(textColor);
//        paint.setTextSize(150);
//        canvas.drawText(text,0,0,paint);
//        canvas.drawText(text,);
    }

    /**
     * 设置图片资源
     */
    public void setImageResource(int resId) {
        imageView.setImageResource(resId);
    }

    /**
     * 设置显示的文字
     */
    public void setTextViewText(String text) {
        textView.setText(text);
    }

    /**
     * 设置显示的文字的颜色
     */
    public void setTextViewTextColor(int color) {
        textView.setTextColor(color);
    }

}
