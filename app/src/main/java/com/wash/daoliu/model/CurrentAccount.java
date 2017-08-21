package com.wash.daoliu.model;

/**
 * Created by jiajia on 2016/1/22.
 */
public class CurrentAccount {
    public String annual_income_rate;//年化收益率
    public String applying_extract_amount;//申请中转出金额
    public double available_amount;//可用余额
    public double current_hold_amount;//活期持有金额
    public String lastday_income;//昨日收益
    public double current_total_amount;//活期标的当天总金额
    public double current_remain_amount;//活期标的当天剩余金额
    public String per_million_income;//每日万元收益
    public String total_income;//持有金额累计收益

    public CurrentAccount(){
         this.annual_income_rate = "6.5%";
        this.per_million_income = "3.51/天";
         current_hold_amount = 10000;
         available_amount = 150;
         current_total_amount = 20000.00;
         current_remain_amount = 1000.00;
        total_income = "1000";
         lastday_income="23.00";
         applying_extract_amount = "100";
    }
}
