package com.qgexam.user.service;

import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.UserProviderApplication;
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
        Map<String, UserInfo> userInfoMap = new HashMap<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1);
        userInfo.setLoginName("zhangssan");
        userInfoMap.put("1", userInfo);
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setUserId(2);
        userInfo2.setLoginName("lisi");
        userInfoMap.put("2", userInfo2);
        redisCache.setCacheMap("user:1", userInfoMap);
        Map<String, Object> cacheMap = redisCache.getCacheMap("user:1");
        cacheMap.forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });
    }
}
