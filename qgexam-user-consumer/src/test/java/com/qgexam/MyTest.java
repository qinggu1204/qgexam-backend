package com.qgexam;

import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.UserConsumerApplication;
import com.qgexam.user.controller.UserInfoController;
import com.qgexam.user.pojo.DTO.TeacherRegisterDTO;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yzw
 * @description test
 * @date 2022/12/14 18:22:56
 */
@SpringBootTest(classes = UserConsumerApplication.class)
public class MyTest {

    @Autowired
    private RedisCache redisCache;

    @Test
    public void test() {
        String name = redisCache.getCacheObject("username");
        System.out.println(name);
    }

}
