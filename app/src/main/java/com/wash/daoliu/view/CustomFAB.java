package com.wash.daoliu.view;

/**
 * Created by rogerlzp on 16/2/22.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by francesco on 11/09/14.
 */
public class CustomFAB extends ImageView implements View.OnTouchListener {

    private static final int RAD = 56;
    private Context ctx;
    private boolean hasChange = false;
    private int currentLeft, currentRight, currentTop, currentBottom;
    int screenWidth;
    int screenHeight;
    int lastX;
    int lastY;
    boolean flag = false;

    public CustomFAB(Context context) {
        super(context);
        this.ctx = context;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 300;
        setOnTouchListener(this);
    }

    public CustomFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 300;
        setOnTouchListener(this);
    }
    private void refresh(){
        if (hasChange) {
            layout(currentLeft, currentTop, currentRight, currentBottom);
        }
    }
    public CustomFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ctx = context;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 300;
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refresh();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        int action = event.getAction();
        boolean canClick = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            /**
             * layout(l,t,r,b)
             * l  Left position, relative to parent
             t  Top position, relative to parent
             r  Right position, relative to parent
             b  Bottom position, relative to parent
             * */
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                if (Math.abs(dx) > 0 || Math.abs(dy) > 0) {
                    if (!flag) {
                        flag = true;
                    }
                    if (!hasChange) {
                        hasChange = true;
                    }
                }
                int left = v.getLeft() + dx;
                int top = v.getTop() + dy;
                int right = v.getRight() + dx;
                int bottom = v.getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + v.getWidth();
                }
                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - v.getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + v.getHeight();
                }
                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - v.getHeight();
                }
                currentLeft = left;
                currentTop = top;
                currentRight = right;
                currentBottom = bottom;
                v.layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                canClick = flag;
                flag = false;
                if ((v.getLeft() + v.getWidth() / 2) <= screenWidth / 2) {
                    currentLeft = 0;
                    currentTop = v.getTop();
                    currentRight = v.getWidth();
                    currentBottom = v.getHeight() + v.getTop();
                    v.layout(0, v.getTop(), v.getWidth(), v.getHeight() + v.getTop());
                } else {
                    currentLeft = screenWidth - v.getWidth();
                    currentTop = v.getTop();
                    currentRight = screenWidth;
                    currentBottom = v.getHeight() + v.getTop();
                    v.layout(screenWidth - v.getWidth(), v.getTop(), screenWidth, v.getHeight() + v.getTop());
                }
                break;
        }
        return canClick;
    }
}