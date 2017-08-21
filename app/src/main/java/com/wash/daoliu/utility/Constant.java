package com.wash.daoliu.utility;

/**
 * @author: bobo Email: jqbo84@163.com
 * create date: 2012-3-22 下午03:52:35
 */
public class Constant {
    public final static String USER_AUTH_FROM = "user_auth_from";
    public final static String FROM_BIND_BANK_CARD = "from_bind_bank_card";
    /**
     * 弹出对话框
     */
    public static class SelectDialog {
        public static final int SELECT_DATE_BEGIN = 0;
        public static final int SELECT_DATE_END = 1;
        public static final int SELECT_PLAN_DATE_BEGIN = 0;
        public static final int SELECT_PLAN_DATE_END = 1;
        public static final int SELECT_ACTUAL_DATE_BEGIN = 2;
        public static final int SELECT_ACTUAL_DATE_END = 3;
    }

    /**
     * SharedPreferences缓存信息变量
     */
    public static class Share {
        public static final String SHARE_NAME = "me_property";
        public static final String SHARE_USERNAME = "username";
        public static final String SHARE_USERCODE = "user_code";
        public static final String SHARE_USER_PORTRAITS = "userPortraits";
        public static final String SHARE_USERID = "userid";
        public static final String SHARE_SESSIONID = "session_id";
        public static final String SHARE_TOKENKEY = "tokenKey";
        public static final String SHARE_LOGIN_NAME = "loginname";
        public static final String SHARE_LOGIN_PASSWORD = "password";
        public static final String SHARE_REMEMBER_PWD = "remember_pwd";
        public static final String SHARE_CHANNEL_NO = "channelNo";
        public static final String SHARE_CHANNEL_NAME = "channelName";
        public static final String SHARE_LOGIN_DATETIME = "login_datetime";
        public static final String SHARE_IMEI = "imei";
        public static final String SHARE_SIM_NO = "sim_no";
        public static final String SHARE_SYS_VERSION_NAME = "sysversion_name";
        public static final String SHARE_SYS_VERSION_CODE = "sysversion_code";
        public static final String SHARE_PACKAGE_NAME = "package_name";
        public static final String SHARE_CACHE_USERINFO = "cache_userinfo";
        public static final String ACTIVITY_CLASS = "activity_class";
        //登录成功后跳转到我的账户

    }

    public static class LoginParams {
        public static final String LOGIN_TYPE = "login_type";
        public static final String LOGIN_TO_MYACCOUNT = "login_to_myaccount";//登陆后跳转我的账户
        public static final String LOGIN_TO_COUPON = "login_to_coupon";//登录后跳转理财金券
        public static final String LOGIN_TO_FEEDBACK = "login_to_feedback";//登录后跳转意见反馈
        public static final String LOGIN_TO_TYB_PRODUCT = "login_to_tyb_product";//登录后跳转体验标产品详情

        public static final String LOGIN_TO_CLOAN_PRODUCT = "login_to_cloan_product";//登录后跳转贷款产品详情

        public static final String LOGIN_TO_PRODUCT = "login_to_product";//登录后跳转产品详情
        public static final String LOGIN_TO_PARTNER = "login_to_partner";//登录后跳转合伙人
        public static final String LOGIN_TO_MAIN = "login_to_main";//登录后跳转主页，并清空手势密码
        public static final String LOGIN_TO_PRODUCT_LIST = "login_to_product_list";//购买成功返回列表页
        public static final String REFRESH_HOME_PRODUCT = "refresh_home_product";//体验表登录成功返回首页刷新首页
        public static final String FROM_FORGET_GESTURE = "from_forget_gesture";//体验表登录成功返回首页刷新首页
        public static final String FROM_PRODUCT_LIST_SXT= "from_product_list_sxt";//理财列表随心投
        public static final String FROM_RESERVE_SUCCESS= "from_reserve";//理财列表随心投

    }

    /**
     * 加载数据文本显示
     */
    public static class Variable {
        public static final String LOADING_TEXT = "加载中，请等待...";
        public static final String COMMITING_TEXT = "正在提交中，请稍等....";
        public static final String CHECKING_TEXT = "正在检查中，请稍等....";
        public static final String REMOVEING_TEXT = "正在解除中，请稍等....";

        public static String NETWORK_NAME = null;
    }

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

}
