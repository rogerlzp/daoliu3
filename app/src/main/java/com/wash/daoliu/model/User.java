package com.wash.daoliu.model;

import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.utility.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rogerlzp on 15/12/30.
 */
// 临时存储用户信息
public class User {


    public Account account;
    public UserInfo userInfo;
    public UserProfile userProfile;
    public String userPhone;

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public List<Car> carList = new ArrayList<>();

    public static User user = null;

    private User() {
        if (account == null) {
            account = new Account();
        }
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        if (userProfile == null) {
            userProfile = new UserProfile();
        }
    }

    public static void clearUser() {
        if (user != null) {
            user = null;
        }
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public static User getUserInstance() {
        if (user == null) {
            user = new User();
            if (!StringUtils.isNullOrEmpty(LTNApplication.getInstance().getUserMobile())){
                //获取mobile
                user.setUserPhone(LTNApplication.getInstance().getUserMobile());
            }
        }
        return user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    // account 信息
    public class Account {
        public double birdCoin;            // 账户鸟币信息
        public double birdCoinRevenue;     // 账户鸟币收益
        public double collectCapital;      // 待收本金
        public double collectRevenue;      // 待收收益
        public double frozenAmount;        // 冻结总资产
        public double id;                  //
        public double liveAmount;           // 活期金额
        public double sumRevenue;           // 累计总收益
        public double totalAsset;           // 总资产
        public double usableBalance;        // 可用余额
        public double userId;               // 用户ID
        public double currentHoldAmount;     // 用户活期总额
        public String freeCounter;          // 免费提现次数
        public String sxtIsShow;          // 随心投
        public String axtIsShow;          // 安心投

        public String getSxtIsShow() {
            return sxtIsShow;
        }

        public void setSxtIsShow(String sxtIsShow) {
            this.sxtIsShow = sxtIsShow;
        }

        public String getAxtIsShow() {
            return axtIsShow;
        }

        public void setAxtIsShow(String axtIsShow) {
            this.axtIsShow = axtIsShow;
        }

        public Account() {

        }

        public double getCurrentHoldAmount() {
            return currentHoldAmount;
        }

        public void setCurrentHoldAmount(double currentHoldAmount) {
            this.currentHoldAmount = currentHoldAmount;
        }

        public String getFreeCounter() {
            return freeCounter;
        }

        public double getBirdCoin() {
            return birdCoin;
        }

        public void setBirdCoin(double birdCoin) {
            this.birdCoin = birdCoin;
        }

        public double getBirdCoinRevenue() {
            return birdCoinRevenue;
        }

        public void setBirdCoinRevenue(double birdCoinRevenue) {
            this.birdCoinRevenue = birdCoinRevenue;
        }

        public double getCollectCapital() {
            return collectCapital;
        }

        public void setCollectCapital(double collectCapital) {
            this.collectCapital = collectCapital;
        }

        public double getCollectRevenue() {
            return collectRevenue;
        }

        public void setCollectRevenue(double collectRevenue) {
            this.collectRevenue = collectRevenue;
        }

        public double getFrozenAmount() {
            return frozenAmount;
        }

        public void setFrozenAmount(double frozenAmount) {
            this.frozenAmount = frozenAmount;
        }

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public double getLiveAmount() {
            return liveAmount;
        }

        public void setLiveAmount(double liveAmount) {
            this.liveAmount = liveAmount;
        }

        public double getSumRevenue() {
            return sumRevenue;
        }

        public void setSumRevenue(double sumRevenue) {
            this.sumRevenue = sumRevenue;
        }

        public double getTotalAsset() {
            return totalAsset;
        }

        public void setTotalAsset(double totalAsset) {
            this.totalAsset = totalAsset;
        }

        public double getUsableBalance() {
            return usableBalance;
        }

        public void setUsableBalance(double usableBalance) {
            this.usableBalance = usableBalance;
        }

        public double getUserId() {
            return userId;
        }

        public void setUserId(double userId) {
            this.userId = userId;
        }
    }

    public class UserInfo {

        // 账户信息
        private String bankAuthStatus; // 绑卡信息
        private String bankNo; // 银行卡信息
        private String belongBank; // 银行信息
        private String cardId;          // 身份证信息
        private String certification;  // 认证
        private String createDate;       // 创建时间
        private String guestType;       // 顾客类型
        private String investStatus;       // 投资类型, 已投资/未投资
        private int isExperience;       // TODO: 确认适用范围
        private int isFirstOrder;       // TODO: 确认适用范围
        private int isStaff;           // 员工
        private String mobile;           // 手机号码
        private String preMoblieNo;           // 前手机号码
        private String referralCode;     // 推荐人
        private String state;            // TODO: 状态


        private String umpayUserNo;         // 联通优势号码
        private String userId;         // 用户ID
        private String userName;         // 用户姓名
        private String userLevelId;         // 合伙人级别

        private String agreementCZ;   // 是否签订免密充值  1：是  0:否
        private String agreementTZ; // 是否签订免密投资  1：是  0:否

        public String getAgreementCZ() {
            return agreementCZ;
        }

        public void setAgreementCZ(String agreementCZ) {
            this.agreementCZ = agreementCZ;
        }

        public String getAgreementTZ() {
            return agreementTZ;
        }

        public void setAgreementTZ(String agreementTZ) {
            this.agreementTZ = agreementTZ;
        }

        public String getUserLevelId() {
            return userLevelId;
        }

        public void setUserLevelId(String userLevelId) {
            this.userLevelId = userLevelId;
        }


        public UserInfo() {

        }

        public String getBankAuthStatus() {
            return bankAuthStatus;
        }

        public void setBankAuthStatus(String bankAuthStatus) {
            this.bankAuthStatus = bankAuthStatus;
        }

        public String getBankNo() {
            return bankNo;
        }

        public void setBankNo(String bankNo) {
            this.bankNo = bankNo;
        }

        public String getBelongBank() {
            return belongBank;
        }

        public void setBelongBank(String belongBank) {
            this.belongBank = belongBank;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getCertification() {
            return certification;
        }

        public void setCertification(String certification) {
            this.certification = certification;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getGuestType() {
            return guestType;
        }

        public void setGuestType(String guestType) {
            this.guestType = guestType;
        }

        public String getInvestStatus() {
            return investStatus;
        }

        public void setInvestStatus(String investStatus) {
            this.investStatus = investStatus;
        }

        public int getIsExperience() {
            return isExperience;
        }

        public void setIsExperience(int isExperience) {
            this.isExperience = isExperience;
        }

        public int getIsFirstOrder() {
            return isFirstOrder;
        }

        public void setIsFirstOrder(int isFirstOrder) {
            this.isFirstOrder = isFirstOrder;
        }

        public int getIsStaff() {
            return isStaff;
        }

        public void setIsStaff(int isStaff) {
            this.isStaff = isStaff;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPreMoblieNo() {
            return preMoblieNo;
        }

        public void setPreMoblieNo(String preMoblieNo) {
            this.preMoblieNo = preMoblieNo;
        }

        public String getReferralCode() {
            return referralCode;
        }

        public void setReferralCode(String referralCode) {
            this.referralCode = referralCode;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUmpayUserNo() {
            return umpayUserNo;
        }

        public void setUmpayUserNo(String umpayUserNo) {
            this.umpayUserNo = umpayUserNo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
