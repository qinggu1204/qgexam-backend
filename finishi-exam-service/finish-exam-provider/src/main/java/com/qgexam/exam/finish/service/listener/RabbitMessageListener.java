package com.qgexam.exam.finish.service.listener;

import com.qgexam.exam.finish.dao.*;
import com.qgexam.rabbit.constants.FinishExamRabbitConstants;
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

@Slf4j
@Component
public class RabbitMessageListener {
    @Autowired
    private ExamSubmitRecordDao examSubmitRecordDao;

    @Transactional
    @RabbitListener(queues = FinishExamRabbitConstants.EXAM_FINISH_NAME)
    public void listenExamFinishQueue(FinishExamDTO finishExamDTO, Channel channel,
                                      Message message)throws IOException{
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