package com.mbx.usercenter.constant;

/**
 * 用户服务常量类
 */
public interface UserConstant {


    /**
     * 用户账户校验正则表达式
     */
    String VALID_PATTERN = "^[a-zA-Z0-9]+$";

    /**
     * 用户登录状态session键名
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;
    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;



}
