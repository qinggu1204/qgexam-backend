package com.qgexam.user.service.impl;

import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.utils.SMSUtils;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.service.MessageCodeService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证码服务实现类
 *
 * @author ljy
 * @since 2022-12-15 11:12:05
 */
@Service
public class MessageCodeServiceImpl implements MessageCodeService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean sendCode(String phoneNumber) {
        Random r = new Random();
        int num=r.nextInt(900000)+100000;//100000-999999
        redisCache.setCacheObject(SystemConstants.LOGIN_REDIS_PREFIX + phoneNumber,""+num,5, TimeUnit.MINUTES);
        return SMSUtils.sendMessage(phoneNumber,""+num);
    }

    @Override
    public boolean validateCode(String phoneNumber,String code){
        String redisCode = redisCache.getCacheObject(SystemConstants.LOGIN_REDIS_PREFIX + phoneNumber);
        redisCache.deleteObject(SystemConstants.LOGIN_REDIS_PREFIX + phoneNumber);
        //验证码不为空且验证码正确返回true,否则返回false
        return redisCode != null && redisCode.equals(code);
    }
}
