package com.qgexam;

import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.finish.UserConsumerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

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
