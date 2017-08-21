package com.wash.daoliu.model;

import java.util.Date;

/**
 * Created by zhengpingli on 2017/6/24.
 */

public class Cloan {
    private Integer id;//
    private String cloanName;//'贷款名字'
    private String cloanLogo;//'图标URL'
    private String company;//公司ID， 待确认
    private String description;//待确认
    private int applyCustomer;//''申请人数''
    private double monthRate;//月利率
    private double dayRate;//日利率
    private double yearRate;//年利率

    private String loanRange;//'贷款范围'
    private String rateRange;//'利率范围'
    private String dateRange;//'借款时间范围'

    private String applyCondition;//''申请人数''
    private String applyDescription;//''申请人数''

    private int loanMax;//'最多借款'
    private int loanMin;//'最少借款'
    private String passRate;//''PH:通过率高；PM：通过率中； PL：通过率低','

    private int status;//''1:有效   2:新建   0:撤出','

    private String cloanTags;// 产品标签

    private String cloanNo;// 产品标签
    private String cloanType;// '备用字段：产品类型'

    private String createDate;//创建时间
    private String createBy;//创建人
    private String remark;//备注

    private int dateRangeMin;//'最少借款天数'
    private int dateRangeMax;//'最长借款天数'

    private double rateRangeMin;//''利率最低''

    private double rateRangeMax;//''利率最高''

    private String h5link; // 链接

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCloanName() {
        return cloanName;
    }

    public void setCloanName(String cloanName) {
        this.cloanName = cloanName;
    }

    public String getCloanLogo() {
        return cloanLogo;
    }

    public void setCloanLogo(String cloanLogo) {
        this.cloanLogo = cloanLogo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getApplyCustomer() {
        return applyCustomer;
    }

    public void setApplyCustomer(int applyCustomer) {
        this.applyCustomer = applyCustomer;
    }

    public double getMonthRate() {
        return monthRate;
    }

    public void setMonthRate(double monthRate) {
        this.monthRate = monthRate;
    }

    public double getDayRate() {
        return dayRate;
    }

    public void setDayRate(double dayRate) {
        this.dayRate = dayRate;
    }

    public double getYearRate() {
        return yearRate;
    }

    public void setYearRate(double yearRate) {
        this.yearRate = yearRate;
    }

    public String getLoanRange() {
        return loanRange;
    }

    public void setLoanRange(String loanRange) {
        this.loanRange = loanRange;
    }

    public String getRateRange() {
        return rateRange;
    }

    public void setRateRange(String rateRange) {
        this.rateRange = rateRange;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public int getLoanMax() {
        return loanMax;
    }

    public void setLoanMax(int loanMax) {
        this.loanMax = loanMax;
    }

    public int getLoanMin() {
        return loanMin;
    }

    public void setLoanMin(int loanMin) {
        this.loanMin = loanMin;
    }

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCloanTags() {
        return cloanTags;
    }

    public void setCloanTags(String cloanTags) {
        this.cloanTags = cloanTags;
    }

    public String getCloanNo() {
        return cloanNo;
    }

    public void setCloanNo(String cloanNo) {
        this.cloanNo = cloanNo;
    }

    public String getCloanType() {
        return cloanType;
    }

    public void setCloanType(String cloanType) {
        this.cloanType = cloanType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDateRangeMin() {
        return dateRangeMin;
    }

    public void setDateRangeMin(int dateRangeMin) {
        this.dateRangeMin = dateRangeMin;
    }

    public int getDateRangeMax() {
        return dateRangeMax;
    }

    public void setDateRangeMax(int dateRangeMax) {
        this.dateRangeMax = dateRangeMax;
    }

    public double getRateRangeMin() {
        return rateRangeMin;
    }

    public void setRateRangeMin(double rateRangeMin) {
        this.rateRangeMin = rateRangeMin;
    }

    public double getRateRangeMax() {
        return rateRangeMax;
    }

    public void setRateRangeMax(double rateRangeMax) {
        this.rateRangeMax = rateRangeMax;
    }

    public String getH5link() {
        return h5link;
    }

    public void setH5link(String h5link) {
        this.h5link = h5link;
    }

    public String getApplyCondition() {
        return applyCondition;
    }

    public void setApplyCondition(String applyCondition) {
        this.applyCondition = applyCondition;
    }

    public String getApplyDescription() {
        return applyDescription;
    }

    public void setApplyDescription(String applyDescription) {
        this.applyDescription = applyDescription;
    }
}
