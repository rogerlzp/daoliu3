package com.wash.daoliu.utility;

/**
 * Created by zhengpingli on 2017/6/28.
 */

public enum JobEnum {
    JOB_WORKER("WORKER", "工人"),
    JOB_CRAFTSMAN("CRAFTSMAN", "技工"),
    JOB_WAITER("WAITER", "服务员"),
    JOB_COURIER("COURIER", "快递员"),
    JOB_ENGINEER("ENGINEER", "工程师"),//有效时间+获取
    JOB_FARMER("FARMER", "农民"),
    JOB_WHITE_COLLAR("WHITE_COLLAR", "白领"),
    JOB_TEACHER("TEACHER", "教师"),
    JOB_OTHERS("OTHERS", "其他");

    private String enName;
    private String zhName;

    JobEnum(String enName, String zhName) {
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

    public static JobEnum getJobEnumByEnName(String enName) {
        for (JobEnum educationEnum : JobEnum.values()) {
            if (educationEnum.enName.equalsIgnoreCase(enName)) {
                return educationEnum;
            }
        }
        return null;
    }


    public static JobEnum getJobEnumByZhName(String ZhName) {
        for (JobEnum educationEnum : JobEnum.values()) {
            if (educationEnum.enName.equalsIgnoreCase(ZhName)) {
                return educationEnum;
            }
        }
        return null;
    }
}
