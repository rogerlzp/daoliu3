package com.wash.daoliu.model;

import android.text.TextUtils;

/**
 * Created by rogerlzp on 15/12/16.
 */
//TODO: check different type

public class Product {

    public static final String DUMMY_STRING = "dummy";
    public static final int DUMMY_INT = -1;
    // 增加说明
    public String createBy;

    public String annualIncomeText;
    public String bidPublishDate;
    public String productName;

    // 产品详情url
    public String detailsUrl;


    // 产品ID
    public String productId;

    // 产品 NO
    public String productNo;


    // 产品Tag
    public String productTag;

    // 产品Type
    public String productType;


    // 已买产品次数
    public String buyCount;

    // 收益方式
    public String repaymentType;

    // 起息时间
    public String staRateDate;

    // 递增金额
    public int staInvestAmount;

    // 产品认购上限
    public int singleLimitAmount;

    // 期限单位
    public String deadlineUnit;

    // 期限
    public int productDeadline;

    // 剩余总额
    public float productRemainAmount;

    // 总额
    public float productTotalAmount;

    // 年收益率
    public float annualIncome;

    //TODO: 增加更多的产品属性


    // TODO: 增加说明
    public String arrangeDate;
    public String createDate;

    public String isArrange;
    public int isFisrtPage;
    public int orderNo;
    public String productTitle;
    public String raiseEndDate;
    public String rateCalculateType;
    public int convertDay;
    public String productStatus;

    public String getAnnualIncomeText() {
        return annualIncomeText;
    }

    public void setAnnualIncomeText(String annualIncomeText) {
        this.annualIncomeText = annualIncomeText;
    }

    public String getBidPublishDate() {
        return bidPublishDate;
    }

    public void setBidPublishDate(String bidPublishDate) {
        this.bidPublishDate = bidPublishDate;
    }


    public Product(String productNo) {
        this.productNo = productNo;
    }

    public Product() {
    }


    public int getConvertDay() {
        return convertDay;
    }

    public void setConvertDay(int convertDay) {
        this.convertDay = convertDay;
    }


    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDetailUrl() {
        return detailsUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public String getStaRateDate() {
        return staRateDate;
    }

    public void setStaRateDate(String staRateDate) {
        this.staRateDate = staRateDate;
    }

    public int getStaInvestAmount() {
        return staInvestAmount;
    }

    public void setStaInvestAmount(int staInvestAmount) {
        this.staInvestAmount = staInvestAmount;
    }

    public int getSingleLimitAmount() {
        return singleLimitAmount;
    }

    public void setSingleLimitAmount(int singleLimitAmount) {
        this.singleLimitAmount = singleLimitAmount;
    }

    public String getDeadlineUnit() {
        String name = "天";
        if (!TextUtils.isEmpty(deadlineUnit)) {
            if ("Y".equals(deadlineUnit)) {
                name = "年";
            } else if ("M".equals(deadlineUnit)) {
                name = "个月";
            }
        }
        return name;
    }

    public void setDeadlineUnit(String deadlineUnit) {
        this.deadlineUnit = deadlineUnit;
    }

    public int getProductDeadline() {
        return productDeadline;
    }

    public void setProductDeadline(int productDeadline) {
        this.productDeadline = productDeadline;
    }

    public float getProductRemainAmount() {

        return productRemainAmount;
    }

    public void setProductRemainAmount(float productRemainAmount) {
        this.productRemainAmount = productRemainAmount;
    }

    public float getProductTotalAmount() {
        return productTotalAmount;
    }

    public void setProductTotalAmount(float productTotalAmount) {
        this.productTotalAmount = productTotalAmount;
    }

    public float getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(float annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getArrangeDate() {
        return arrangeDate;
    }

    public void setArrangeDate(String arrangeDate) {
        this.arrangeDate = arrangeDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getIsArrange() {
        return isArrange;
    }

    public void setIsArrange(String isArrange) {
        this.isArrange = isArrange;
    }

    public int getIsFisrtPage() {
        return isFisrtPage;
    }

    public void setIsFisrtPage(int isFisrtPage) {
        this.isFisrtPage = isFisrtPage;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getRaiseEndDate() {
        return raiseEndDate;
    }

    public void setRaiseEndDate(String raiseEndDate) {
        this.raiseEndDate = raiseEndDate;
    }

    public String getRateCalculateType() {
        return rateCalculateType;
    }

    public void setRateCalculateType(String rateCalculateType) {
        this.rateCalculateType = rateCalculateType;
    }
}
