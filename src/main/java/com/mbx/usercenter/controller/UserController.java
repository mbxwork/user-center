package com.mbx.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mbx.usercenter.model.domain.User;
import com.mbx.usercenter.model.request.UserLoginRequest;
import com.mbx.usercenter.model.request.UserRegisterRequest;
import com.mbx.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @PostMapping("/register")
    public Long register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null){
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
    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null){
            return null ;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }


    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户列表
     */
    @GetMapping("/getUserByUsername")
    public List<User> getUserByUsername(String username, HttpServletRequest request) {
        //判断是否管理员用户
        if (!userService.isAdmin(request)) {
            return new ArrayList<>();
        }
        List<User> userList = userService.list(new QueryWrapper<>(new User()).like("username", username));
        //用户数据脱敏
        return userList.stream().map(user -> userService.userMasking(user)).toList();
    }

    /**
     * 删除用户
     * @param id 用户id
     * @return 是否删除成功
     */
    @PostMapping("/deleteUser")
    public boolean deleteUser(@RequestBody Long id, HttpServletRequest request) {

        if(id <= 0 ){
            return false;
        }

        //判断是否管理员用户
        if (!userService.isAdmin(request)) {
            return false;
        }
        return userService.removeById(id);
    }





}
