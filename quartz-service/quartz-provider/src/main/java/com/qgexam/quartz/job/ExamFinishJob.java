package com.qgexam.quartz.job;

import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.quartz.dao.ExaminationInfoDao;
import com.qgexam.rabbit.constants.ExamFinishRabbitConstants;
import com.qgexam.rabbit.service.RabbitService;
import com.qgexam.user.pojo.PO.ExaminationInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
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
    @DubboReference
    private RabbitService rabbitService;

    public void execute(Integer examinationId) {
        log.info("###########examFinishJob.execute()###########");
        rabbitService.sendMessage(ExamFinishRabbitConstants.EXAM_FINISH_EXCHANGE_NAME,
                ExamFinishRabbitConstants.EXAM_FINISH_ROUTING_KEY,
                examinationId);
    }
}
