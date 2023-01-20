package com.qgexam.test;

import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.enter.EnterExamProviderApplication;
import com.qgexam.user.pojo.PO.ExaminationInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yzw
 * @date 2023年01月19日 23:09
 */
@SpringBootTest(classes = EnterExamProviderApplication.class)
public class RedisTest {
    @Autowired
    private RedisCache redisCache;

    @Test
    public void test() {
        ExaminationInfo examinationInfo = new ExaminationInfo();
        examinationInfo.setExaminationId(1);
        examinationInfo.setExaminationName("test");
        examinationInfo.setIsOptionResort(1);
        examinationInfo.setIsOptionResort(1);
        redisCache.setCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + 999, examinationInfo);

        ExaminationInfo examinationInfo1 = redisCache.getCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + 999);
        System.out.println("===========================");
        System.out.println(examinationInfo1);

    }
}
