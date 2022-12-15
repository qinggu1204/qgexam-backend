package com.qgexam.user.service;

import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description UserInfoServiceTest
 * @author peter guo
 * @date 2022/12/14 19:20:04
 */
@SpringBootTest
public class UserInfoServiceTest {

    @Reference
    private UserInfoService userInfoService;

    @Test
    public void getUserInfoTest() {
        System.out.println(userInfoService.getUserInfo());
    }

}
