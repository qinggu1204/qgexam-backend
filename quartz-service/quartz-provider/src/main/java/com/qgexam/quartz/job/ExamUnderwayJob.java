package com.qgexam.quartz.job;

import com.qgexam.rabbit.constants.ExamUnderwayRabbitConstants;
import com.qgexam.rabbit.service.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ljy
 * @date 2023年01月08日 23:42
 */
@Slf4j
@Component("examUnderwayJob")
public class ExamUnderwayJob {


    @DubboReference
    private RabbitService rabbitService;

    public void execute(Integer examinationId) {
        log.info("###########examUnderwayJob.execute()###########");
        rabbitService.sendMessage(ExamUnderwayRabbitConstants.EXAM_UNDERWAY_EXCHANGE_NAME,
                ExamUnderwayRabbitConstants.EXAM_UNDERWAY_ROUTING_KEY,
                examinationId);
    }

}
