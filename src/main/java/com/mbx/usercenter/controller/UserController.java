package com.mbx.usercenter.controller;

import com.mbx.usercenter.model.domain.User;
import com.mbx.usercenter.model.request.UserLoginRequest;
import com.mbx.usercenter.model.request.UserRegisterRequest;
import com.mbx.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 *
 * @author mbx
 */

@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserService userService;



    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求
     * @return 新用户id
     */
    @RequestMapping("/register")
    public Long register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest != null){
            return null ;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     * @param userLoginRequest 用户登录请求
     * @return 脱敏后的用户信息
     */
    @RequestMapping("/login")
    public User login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest != null){
            return null ;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }






}
