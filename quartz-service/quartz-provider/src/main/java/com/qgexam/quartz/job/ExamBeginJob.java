package com.qgexam.quartz.job;

import com.qgexam.rabbit.constants.ExamBeginRabbitConstants;
import com.qgexam.rabbit.service.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author yzw
 * @date 2023年01月08日 23:42
 */
@Slf4j
@Component("examBeginJob")
public class ExamBeginJob {


    @DubboReference
    private RabbitService rabbitService;

    public void execute(Integer examinationId) {
        rabbitService.sendMessage(ExamBeginRabbitConstants.EXAM_BEGIN_EXCHANGE_NAME,
                ExamBeginRabbitConstants.EXAM_BEGIN_ROUTING_KEY, examinationId);
    }


}
