package com.qgexam.user.service;

import com.qgexam.user.pojo.DTO.UserLoginByUsernameDTO;
import com.qgexam.user.pojo.VO.UserInfoVO;
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
    public void testUserLogin() {
        UserInfoVO userInfoVO = userInfoService.userLogin(new UserLoginByUsernameDTO("17682405206", "zhangsan"));
        System.out.println(userInfoVO);
    }
}
