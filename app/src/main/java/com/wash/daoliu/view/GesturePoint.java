package com.wash.daoliu.view;


import android.widget.ImageView;

import com.wash.daoliu.R;
import com.wash.daoliu.utility.LTNConstants;

public class GesturePoint {

    //
    private int leftX;

    //
    private int rightX;

    //
    private int topY;

    //
    private int bottomY;

    //
    private ImageView image;

    //
    private int centerX;

    //
    private int centerY;

    //
    private int pointState;

    //
    private int num;

    public GesturePoint(int leftX, int rightX, int topY, int bottomY,
                        ImageView image, int num) {
        super();
        this.leftX = leftX;
        this.rightX = rightX;
        this.topY = topY;
        this.bottomY = bottomY;
        this.image = image;

        this.centerX = (leftX + rightX) / 2;
        this.centerY = (topY + bottomY) / 2;

        this.num = num;
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getTopY() {
        return topY;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getPointState() {
        return pointState;
    }

    public void setPointState(int state) {
        pointState = state;
        switch (state) {
            case LTNConstants.POINT_STATE_NORMAL:
                this.image.setBackgroundResource(R.drawable.oval);
                break;
            case LTNConstants.POINT_STATE_SELECTED:
                this.image.setBackgroundResource(R.drawable.oval_pressed);
                break;
            case LTNConstants.POINT_STATE_WRONG:
                this.image.setBackgroundResource(R.drawable.gesture_node_wrong);
                break;
            default:
                break;
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bottomY;
        result = prime * result + ((image == null) ? 0 : image.hashCode());
        result = prime * result + leftX;
        result = prime * result + rightX;
        result = prime * result + topY;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GesturePoint other = (GesturePoint) obj;
        if (bottomY != other.bottomY)
            return false;
        if (image == null) {
            if (other.image != null)
                return false;
        } else if (!image.equals(other.image))
            return false;
        if (leftX != other.leftX)
            return false;
        if (rightX != other.rightX)
            return false;
        if (topY != other.topY)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Point [leftX=" + leftX + ", rightX=" + rightX + ", topY="
                + topY + ", bottomY=" + bottomY + "]";
    }
}
