package com.qgexam.quartz.job;

import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.quartz.dao.ExaminationInfoDao;
import com.qgexam.user.pojo.PO.ExaminationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ljy
 * @date 2023年01月08日 23:42
 */
@Slf4j
@Component("examFinishJob")
public class ExamFinishJob {
    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private RedisCache redisCache;

    public void execute(Integer examinationId) {
        log.info("###########examFinishJob.execute()###########");
        examinationInfoDao.updateStatus(examinationId,3);
        // 根据考试Id查询考试信息
        ExaminationInfo examinationInfo = examinationInfoDao.getByExaminationId(examinationId);
        // 将查询成绩时间存入redis
        if(examinationInfo.getResultQueryTime() == null)
            redisCache.setCacheObject(ExamConstants.EXAMRESULT_QUERYTIME_HASH_KEY_PREFIX + examinationId, "123", 2, TimeUnit.MINUTES);
        else
            redisCache.setCacheObject(ExamConstants.EXAMRESULT_QUERYTIME_HASH_KEY_PREFIX + examinationId, examinationInfo.getResultQueryTime(), 2, TimeUnit.MINUTES);
    }
}
