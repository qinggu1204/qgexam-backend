package com.qgexam.quartz.job;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.quartz.dao.ExaminationInfoDao;
import com.qgexam.user.pojo.PO.ExaminationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author ljy
 * @date 2023年01月08日 23:42
 */
@Slf4j
@Component("examUnderwayJob")
public class ExamUnderwayJob {
    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private RedisCache redisCache;

    public void execute(Integer examinationId) {
        log.info("###########examUnderwayJob.execute()###########");
        examinationInfoDao.updateStatus(examinationId,1);
        // 根据考试Id查询考试信息
        ExaminationInfo examinationInfo = examinationInfoDao.getByExaminationId(examinationId);
        // 如果examinationInfo为空，抛出BusinessException
        if (examinationInfo == null) {
            throw new RuntimeException("考试不存在");
        }
        // 获取考试结束时间
        LocalDateTime endTime = examinationInfo.getEndTime();
        // 获取考试结束时间和当前时间的时间差
        Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), endTime);
        // 获取时间差的毫秒数，作为redis超时时间，单位为毫秒
        long timeout = duration.toMillis() + 60000;

        // 将考试信息存入redis
        redisCache.setCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationInfo.getExaminationId(), examinationInfo);
        redisCache.expire(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationInfo.getExaminationId(), timeout, TimeUnit.MILLISECONDS);
    }

}
