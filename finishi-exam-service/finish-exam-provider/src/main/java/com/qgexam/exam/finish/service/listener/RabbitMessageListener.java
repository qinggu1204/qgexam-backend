package com.qgexam.exam.finish.service.listener;

import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.exam.finish.dao.*;
import com.qgexam.rabbit.constants.FinishExamRabbitConstants;
import com.qgexam.rabbit.constants.SaveExamRabbitConstants;
import com.qgexam.rabbit.pojo.DTO.FinishExamDTO;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

import static com.qgexam.common.core.constants.ExamConstants.*;

@Slf4j
@Component
public class RabbitMessageListener {
    @Autowired
    private ExamSubmitRecordDao examSubmitRecordDao;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AnswerPaperInfoDao answerPaperInfoDao;
    @Autowired
    private ExaminationInfoDao examinationInfoDao;

    @Transactional
    @RabbitListener(queues = FinishExamRabbitConstants.EXAM_FINISH_NAME)
    public void listenExamFinishQueue(FinishExamDTO finishExamDTO, Channel channel,
                                      Message message)throws IOException{
        /*将提交信号考试id和学生id放入缓存中*/
        String submitPrefix=EXAMINATIONANSWER_SUBMIT_KEY_PREFIX+
                finishExamDTO.getExaminationId()+":"+finishExamDTO.getStudentId();
        redisCache.setCacheObject(submitPrefix,"学生"+finishExamDTO.getStudentId()+"已提交考试"+finishExamDTO.getExaminationId());
        boolean flag=true;
        /*插入提交记录*/
        if(examSubmitRecordDao.insertRecord(finishExamDTO.getStudentId(), finishExamDTO.getExaminationId(),Date.from(finishExamDTO.getSubmitTime().atZone(ZoneId.systemDefault()).toInstant()))==0){
            flag=false;
        }
        if(flag){
            log.info("作答已上传");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        else{
            log.info("作答上传失败");
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}