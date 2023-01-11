package com.qgexam.user.task;

import com.qgexam.common.core.constants.JobConstants;
import com.qgexam.common.core.constants.RabbitMQConstants;
import com.qgexam.common.core.utils.QuartzManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class BeginCacheTask implements Job {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        rabbitTemplate.convertAndSend(RabbitMQConstants.BEGIN_CACHE_EXCHANGE_NAME,
                RabbitMQConstants.BEGIN_CACHE_ROUTING_KEY,
                RabbitMQConstants.BEGIN_CACHE_MESSAGE);
        //删掉任务
        QuartzManager.removeJob(JobConstants.BEGIN_CACHE_JOB_NAME, JobConstants.JOB_GROUP_NAME,
                JobConstants.BEGIN_CACHE_TRIGGER_NAME, JobConstants.TRIGGER_GROUP_NAME);
    }
}
