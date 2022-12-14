package com.qgexam.user.service;

import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserInfoServiceTest {

    @Reference
    private UserInfoService userInfoService;

    @Test
    public void getUserInfoByIdTest() {
        System.out.println(userInfoService.getUserInfoById(1));
    }
}
