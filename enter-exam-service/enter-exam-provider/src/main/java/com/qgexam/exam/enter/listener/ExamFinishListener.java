package com.qgexam.exam.enter.listener;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.enter.dao.ExaminationInfoDao;
import com.qgexam.exam.enter.pojo.PO.ExaminationInfo;
import com.qgexam.rabbit.constants.ExamBeginRabbitConstants;
import com.qgexam.rabbit.constants.ExamFinishRabbitConstants;
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
 * @date 2023年01月24日 14:28
 */
@Slf4j
@Component
public class ExamFinishListener {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ExaminationInfoDao examinationInfoDao;


    @RabbitListener(queues = ExamFinishRabbitConstants.EXAM_FINISH_QUEUE_NAME)
    public void listenExamRecordQueue(Integer examinationId, Channel channel, Message message) throws IOException {
        examinationInfoDao.updateStatus(examinationId,3);
        // 根据考试Id查询考试信息
        ExaminationInfo examinationInfo = examinationInfoDao.getByExaminationId(examinationId);
        // 获取考试结束时间
        LocalDateTime endTime = examinationInfo.getEndTime();
        // 获取考试结束时间和当前时间的时间差
        Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), endTime);
        // 获取时间差的毫秒数，作为redis超时时间，单位为毫秒
        long timeout = duration.toMillis() + 60000;

        // 将考试信息存入redis
        redisCache.setCacheObject(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationInfo.getExaminationId(), examinationInfo);
        redisCache.expire(ExamConstants.EXAMINATION_INFO_HASH_KEY_PREFIX + examinationInfo.getExaminationId(), timeout, TimeUnit.MILLISECONDS);
        // 将查询成绩时间存入redis
        if(examinationInfo.getResultQueryTime() == null)
            redisCache.setCacheObject(ExamConstants.EXAMRESULT_QUERYTIME_HASH_KEY_PREFIX + examinationId, "123");
        else
            redisCache.setCacheObject(ExamConstants.EXAMRESULT_QUERYTIME_HASH_KEY_PREFIX + examinationId, examinationInfo.getResultQueryTime());

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
