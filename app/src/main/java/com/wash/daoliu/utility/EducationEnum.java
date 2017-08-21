package com.wash.daoliu.utility;

import com.wash.daoliu.R;

/**
 * Created by zhengpingli on 2017/6/27.
 */

public enum EducationEnum {


    EDUCATION_BACHELOR("BACHELOR", "本科"),
    EDUCATION_COLLEGE("COLLEGE", "大专"),
    EDUCATION_SECONDARY("SECONDARY", "中专"),
    EDUCATION_HIGHSCHOOL("HIGH_SCHOOL", "高中"),
    EDUCATION_MIDDLESCHOOL("MIDDLE_SCHOOL", "初中"),//有效时间+获取
    EDUCATION_PRIMARYSCHOOL("PRIMARY_SCHOOL", "小学"),
    EDUCATION_MASTER("MASTER", "硕士"),
    EDUCATION_DOCTOR("DOCTOR", "博士"),
    EDUCATION_OTHERS("OTHERS", "其他");

    private String enName;
    private String zhName;

    EducationEnum(String enName, String zhName) {
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

    public static EducationEnum getEducationEnumByEnName(String enName) {
        for (EducationEnum educationEnum : EducationEnum.values()) {
            if (educationEnum.enName.equalsIgnoreCase(enName)) {
                return educationEnum;
            }
        }
        return null;
    }


    public static EducationEnum getEducationEnumByZhName(String ZhName) {
        for (EducationEnum educationEnum : EducationEnum.values()) {
            if (educationEnum.enName.equalsIgnoreCase(ZhName)) {
                return educationEnum;
            }
        }
        return null;
    }
}
