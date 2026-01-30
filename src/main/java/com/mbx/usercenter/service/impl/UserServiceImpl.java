package com.mbx.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mbx.usercenter.common.ErrorCode;
import com.mbx.usercenter.exception.BusinessException;
import com.mbx.usercenter.mapper.UserMapper;
import com.mbx.usercenter.model.domain.User;
import com.mbx.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static com.mbx.usercenter.constant.UserConstant.*;


/**
 * @author mbx
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2026-01-27 19:45:10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final String SALT =  "1a2b3c4d";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 账户不能包含特殊字符
        if (!userAccount.matches(VALID_PATTERN)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //账户不能重复
        long result = userMapper.selectCount(new QueryWrapper<>(new User()).eq("userAccount", userAccount));
        if (result > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能重复");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));

        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 账户不能包含特殊字符
        if (!userAccount.matches(VALID_PATTERN)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验用户是否存在
        User user = userMapper.selectOne(new QueryWrapper<>(new User()).eq("userAccount", userAccount));
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //用户脱敏
        User safeUser = userMasking(user);

        //记录用户登陆状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);

        return user;
    }

    /**
     * 用户数据脱敏
     */
    @Override
    public User userMasking(User user) {
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setUpdateTime(user.getUpdateTime());
        safeUser.setUserRole(user.getUserRole());
        return safeUser;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Object user = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user != null) {
            User currentUser = (User) user;
            return currentUser.getUserRole() == ADMIN_ROLE;
        }
        return false;
    }

    @Override
    public void userLoginOut(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }
}




