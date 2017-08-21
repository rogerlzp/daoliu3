package com.wash.daoliu.model;

import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.utility.LTNConstants;

/**
 * Created by zhengpingli on 2017/6/27.
 */

public class UserProfile {

    public long id;
    public long userId;
    public String userName;
    public String idcard;
    public String education;
    public String job;
    public String income;
    public int hasCreditCard;
    public String createDate;
    public String updateDate;

    public String clientType;
    public String sessionKey;

    public UserProfile(){
        hasCreditCard = -1; //默认为-1， 没有
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public int getHasCreditCard() {
        return hasCreditCard;
    }

    public void setHasCreditCard(int hasCreditCard) {
        this.hasCreditCard = hasCreditCard;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}
