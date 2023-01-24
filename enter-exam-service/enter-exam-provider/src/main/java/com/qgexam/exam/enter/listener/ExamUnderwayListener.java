package com.qgexam.exam.enter.listener;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.enter.dao.ExaminationInfoDao;
import com.qgexam.exam.enter.pojo.PO.ExaminationInfo;
import com.qgexam.rabbit.constants.ExamUnderwayRabbitConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author yzw
 * @date 2023年01月24日 16:22
 */
@Slf4j
@Component
public class ExamUnderwayListener {

    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private RedisCache redisCache;

    @RabbitListener(queues = ExamUnderwayRabbitConstants.EXAM_UNDERWAY_QUEUE_NAME)
    public void listenExamUnderwayQueue(Integer examinationId, Channel channel, Message message) throws IOException {
        examinationInfoDao.updateStatus(examinationId,1);
        ExaminationInfo examinationInfo = examinationInfoDao.getByExaminationId(examinationId);
        // 如果examinationInfo为空，抛出BusinessException
        if (examinationInfo == null) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "考试不存在");
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
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
