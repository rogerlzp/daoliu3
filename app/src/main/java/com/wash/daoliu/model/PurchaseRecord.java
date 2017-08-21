package com.wash.daoliu.model;

/**
 * Created by rogerlzp on 16/1/3.
 *
 * 产品购买记录
 */
public class PurchaseRecord {

    String userName;     // 用户名
    String orderAmount;  // 购买金额
    String orderDate;    // 购买时间

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
