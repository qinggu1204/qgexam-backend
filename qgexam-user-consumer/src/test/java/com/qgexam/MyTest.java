package com.qgexam;

import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.UserConsumerApplication;
import com.qgexam.user.dao.QuestionInfoDao;
import com.qgexam.user.pojo.PO.QuestionInfo;
import com.qgexam.user.pojo.PO.UserInfo;
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
