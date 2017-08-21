package com.wash.daoliu.utility;

/**
 * Created by zhengpingli on 2017/6/28.
 */

public enum IncomeEnum {

    INCOME_L1("L1", "0~1000"),
    INCOME_L2("L2", "1000~2000"),
    INCOME_L3("L3", "2000~3500"),
    INCOME_L4("L4", "3500~5000"),
    INCOME_L5("L5", "5000~7000"),//有效时间+获取
    INCOME_L6("L6", "7000~8000"),
    INCOME_L7("L7", "8000~10000"),
    INCOME_L8("L8", "其他");


    private String enName;
    private String zhName;

    IncomeEnum(String enName, String zhName) {
        this.enName = enName;
        this.zhName = zhName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public static IncomeEnum getIncomeEnumByEnName(String enName) {
        for (IncomeEnum educationEnum : IncomeEnum.values()) {
            if (educationEnum.enName.equalsIgnoreCase(enName)) {
                return educationEnum;
            }
        }
        return null;
    }


    public static IncomeEnum getIncomeEnumByZhName(String ZhName) {
        for (IncomeEnum educationEnum : IncomeEnum.values()) {
            if (educationEnum.enName.equalsIgnoreCase(ZhName)) {
                return educationEnum;
            }
        }
        return null;
    }
}
