package com.qgexam.user.service;

import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.UserProviderApplication;
import com.qgexam.user.pojo.PO.ExaminationInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yzw
 * @date 2023年01月09日 10:59
 */
@SpringBootTest(classes = UserProviderApplication.class)
public class RedisCacheTest {
    @Autowired
    private RedisCache redisCache;
    @Test
    public void test() {
        ExaminationInfo examinationInfo = new ExaminationInfo();
        examinationInfo.setExaminationId(1);

        redisCache.setCacheObject("exi", examinationInfo);
        System.out.println("-----------------------------");
        ExaminationInfo examinationInfo1 = redisCache.getCacheObject("exi");
        System.out.println(examinationInfo1);

    }
}
