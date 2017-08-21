package com.wash.daoliu.model;

import java.util.List;

/**
 * Created by zhengpingli on 2017/4/5.
 */

public class Reserve {
    private long Id;
    private String reserveNo;
    private long shopId;  //店名
    private String shopName;  //店名
    private long userId;
    private long carId;

    private double totalAmount;
    private short status; //0:新建； 1:商家确认； 2:到店 3：过期

    private short isFirstReserve = 0; //1是首单，0:非首单


    private String createTime;//创建时间

    private String updateTime;//修改时间

    private String reserveBegintime;//预订洗车的时间,开始时间

    private String reserveEndtime;//预订洗车的时间,结束时间

    private List<ShopService> reserveProductList;


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getReserveNo() {
        return reserveNo;
    }

    public void setReserveNo(String reserveNo) {
        this.reserveNo = reserveNo;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public short getIsFirstReserve() {
        return isFirstReserve;
    }

    public void setIsFirstReserve(short isFirstReserve) {
        this.isFirstReserve = isFirstReserve;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getReserveBegintime() {
        return reserveBegintime;
    }

    public void setReserveBegintime(String reserveBegintime) {
        this.reserveBegintime = reserveBegintime;
    }

    public String getReserveEndtime() {
        return reserveEndtime;
    }

    public void setReserveEndtime(String reserveEndtime) {
        this.reserveEndtime = reserveEndtime;
    }

    public List<ShopService> getReserveProductList() {
        return reserveProductList;
    }

    public void setReserveProductList(List<ShopService> reserveProductList) {
        this.reserveProductList = reserveProductList;
    }
}
