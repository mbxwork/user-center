package com.mbx.usercenter.service;

import com.mbx.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author mbx
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2026-01-27 19:45:10
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

}
