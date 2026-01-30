package com.mbx.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mbx.usercenter.common.BaseResponse;
import com.mbx.usercenter.common.ErrorCode;
import com.mbx.usercenter.common.ResultUtils;
import com.mbx.usercenter.exception.BusinessException;
import com.mbx.usercenter.model.domain.User;
import com.mbx.usercenter.model.request.UserLoginRequest;
import com.mbx.usercenter.model.request.UserRegisterRequest;
import com.mbx.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mbx.usercenter.constant.UserConstant.USER_LOGIN_STATE;

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
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号、密码或确认密码不能为空");
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest 用户登录请求
     * @return 脱敏后的用户信息
     */
    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号密码不能为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户列表
     */
    @GetMapping("/getUserByUsername")
    public BaseResponse<List<User>> getUserByUsername(String username, HttpServletRequest request) {
        //判断是否管理员用户
        if (!userService.isAdmin(request)) {
//            return ResultUtils.error(401, "无权限访问");
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        List<User> userList = userService.list(new QueryWrapper<>(new User()).like("username", username));
        //用户数据脱敏
        List<User> maskedUserList = userList.stream().map(user -> userService.userMasking(user)).toList();
        return ResultUtils.success(maskedUserList);
    }

    /**
     * 删除用户
     * @param id 用户id
     * @return 是否删除成功
     */
    @PostMapping("/deleteUser")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request) {
        if(id == null || id <= 0 ){
//            return ResultUtils.error(400, "用户ID不合法");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不合法");
        }

        //判断是否管理员用户
        if (!userService.isAdmin(request)) {
//            return ResultUtils.error(401, "无权限删除用户");
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     * @param request 请求
     * @return 当前用户
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        if (request == null) {
//            return ResultUtils.error(400, "请求为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
//            return ResultUtils.error(20001, "未登录");
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"未登录");
        }
        long userId = user.getId();
        user = userService.getById(userId);
        return ResultUtils.success(userService.userMasking(user));
    }

    /**
     * 用户注销
     * @param request 请求
     * @return 是否注销成功
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.userLoginOut(request);
        return ResultUtils.success(true);
    }
}
