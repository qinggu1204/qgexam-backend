package com.qgexam.quartz;

import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.quartz.pojo.VO.ExaminationInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author yzw
 * @date 2023年01月11日 12:54
 */
@SpringBootTest(classes = QuartzProviderApplication.class)
public class RedisTest {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void test() {
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
        System.out.println(valueSerializer);
        ExaminationInfoVO examinationInfoVO = new ExaminationInfoVO();
        examinationInfoVO.setExaminationId(1);
        examinationInfoVO.setExaminationName("测试");
        redisCache.setCacheObject("test", examinationInfoVO);
        ExaminationInfoVO test = redisCache.getCacheObject("test");
        System.out.println(test);
    }
}
