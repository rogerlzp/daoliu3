package com.wash.daoliu.model;

import java.io.Serializable;

/**
 * Created by zhengpingli on 2017/3/27.
 */

public class Shop implements Serializable  {

    /*
            "distance": 12755905,
                    "latitude": "31.200585",
                    "longitude": "121.471944",
                    "phone": "(021)64438618",
                    "primaryPic": "http://onlmws3zs.bkt.clouddn.com/WechatIMG10.jpeg",
                    "rating": 0,
                    "shopId": 100008,
                    "shopLocation": "上海市徐汇区零陵路78号3楼",
                    "shopName": "吉运车行",
                    "status": 1,
                    "waitingTime": 0,
                    "washSpace": 2,
                    "workTime": "8:00~21:00",
                    "worker": 2
                    */


    //距离  显示为多少m，如果超过1km，则显示为1.6km这样的形式
    public int distance;

    // 店名
    public String shopName;

    // 店面主要图片
    public String primaryPic;

    // 店名 NO
    public String shopId;

    // 店的 Tag
    public String shopTag;

    // 店的位置
    public String shopLocation;

    // 预计排队时间
    public int waitingTime;

    // 正在洗车的车数
    public int carWashing;

    // 正在排队的车数
    public int carWaiting;

    // 店的评级
    public float rating;

    // 洗车价格， 主打产品的价格
    public double originalPrice;

    // 当前打折的洗车价格， 主打产品的价格
    public double currentPrice;

    // 打折的时间开始时间段
    private String discountBeginTime;

    // 打折的时间结束时间段
    private String discountEndTime;

    private int washSpace;   //洗车位
    private int worker;      // 洗车工人数


    // 工作时间段
    private String workTime;

    // 经度
    private double latidude;
    //维度
    private double longitude;


    public double getLatidude() {
        return latidude;
    }

    public void setLatidude(double latidude) {
        this.latidude = latidude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopTag() {
        return shopTag;
    }

    public void setShopTag(String shopTag) {
        this.shopTag = shopTag;
    }


    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getCarWashing() {
        return carWashing;
    }

    public void setCarWashing(int carWashing) {
        this.carWashing = carWashing;
    }

    public int getCarWaiting() {
        return carWaiting;
    }

    public void setCarWaiting(int carWaiting) {
        this.carWaiting = carWaiting;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getDiscountBeginTime() {
        return discountBeginTime;
    }

    public void setDiscountBeginTime(String discountBeginTime) {
        this.discountBeginTime = discountBeginTime;
    }

    public String getDiscountEndTime() {
        return discountEndTime;
    }

    public void setDiscountEndTime(String discountEndTime) {
        this.discountEndTime = discountEndTime;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getPrimaryPic() {
        return primaryPic;
    }

    public void setPrimaryPic(String primaryPic) {
        this.primaryPic = primaryPic;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public int getWashSpace() {
        return washSpace;
    }

    public void setWashSpace(int washSpace) {
        this.washSpace = washSpace;
    }

    public int getWorker() {
        return worker;
    }

    public void setWorker(int worker) {
        this.worker = worker;
    }
}
