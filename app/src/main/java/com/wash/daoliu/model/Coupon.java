package com.wash.daoliu.model;

import java.io.Serializable;

/**
 * Created by rogerlzp on 15/12/16.
 */

public class Coupon implements Serializable {

    /**
     *    "limitAmount": 200,
     "amount": 10,
     "getDate": "2016-01-07",
     "duration": 0,
     "couponDate": "2016-01-25",
     "desc": "满1000元，赎回时自动变现",
     "couponName": "10元返现券",
     "status": "YX",
     "couponType": "AYXQ",
     "validDateCount": 30,
     "couponId": "1000001",
     "activityType": "投资返现",
     "userCouponId": "1100627"
     */
    private String activityType;
    private String desc;
    private int limitAmount;
    private int id;
    private int amount;
    private String getDate;
    private String couponDate;
    private String couponName;
    private String userCouponId;
    private String status;
    private String couponType;
    private int validDateCount;
    private String couponId;
    private int duration;

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }



    public Coupon() {
    }

    public String getCouponDate() {
        return couponDate;
    }

    public void setCouponDate(String couponDate) {
        this.couponDate = couponDate;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValidDateCount() {
        return validDateCount;
    }

    public void setValidDateCount(int validDateCount) {
        this.validDateCount = validDateCount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(String userCouponId) {
        this.userCouponId = userCouponId;
    }
}