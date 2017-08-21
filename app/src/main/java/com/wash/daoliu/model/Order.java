package com.wash.daoliu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rogerlzp on 15/12/25.
 */
public class Order implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcelDest, int flags) {

    }

//    public final static Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
//
//        @Override
//        public Product createFromParcel(Parcel source) {
//            return new Product(source);
//        }
//
//        @Override
//        public Product[] newArray(int size) {
//            return new Product[size];
//        }
//
//    };

    public Order(Parcel source) {

    }

    private String orderId;
    private String orderPorfit;           // 订单收益 元
    private String orderBirdcoinProfit;   // 订单鸟币收益
    private String orderCoupon;           // 订单使用的优惠券
    private String orderBirdcoin;         // 订单使用的鸟币

    private String orderCreate;
    private String orderCompleteDate;     // 订单完成时间


}