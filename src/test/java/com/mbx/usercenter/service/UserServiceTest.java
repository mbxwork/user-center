package com.mbx.usercenter.service;

import com.mbx.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
class UserServiceTest {

    private static final byte[] SALT =  "1a2b3c4d".getBytes();

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("testMbx");
        user.setUserAccount("111");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("111");
        user.setPhone("111");
        user.setEmail("111");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserRole(0);
        boolean save = userService.save(user);
        System.out.print(save);
        Assertions.assertTrue(save);

    }

    @Test
    void testMd5() {

        String encryptPassword = DigestUtils.md5DigestAsHex((Arrays.toString(SALT) + "123213").getBytes(StandardCharsets.UTF_8));
        System.out.println(encryptPassword);
    }

}