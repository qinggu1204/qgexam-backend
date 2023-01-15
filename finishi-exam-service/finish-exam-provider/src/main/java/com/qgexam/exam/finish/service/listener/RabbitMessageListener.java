package com.qgexam.exam.finish.service.listener;

import com.qgexam.exam.finish.dao.*;
import com.qgexam.exam.finish.pojo.DTO.QuestionDTO;
import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import com.qgexam.exam.finish.pojo.DTO.SubQuestionDTO;
import com.qgexam.rabbit.constants.FinishExamRabbitConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class RabbitMessageListener {
    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private ExamSubmitRecordDao examSubmitRecordDao;

    @Transactional
    @RabbitListener(queues = FinishExamRabbitConstants.EXAM_FINISH_NAME)
    public void listenExamFinishQueue(SaveOrSubmitDTO saveOrSubmitDTO,Integer studentId,Channel channel, Message message)throws IOException{
        boolean flag=true;
        //获取考试信息
        Integer examinationId=saveOrSubmitDTO.getExaminationId();
        //判断提交时间是否早于考试结束时间
        Date submitTime=new Date();
        Date endTime=examinationInfoDao.getEndTimeDate(saveOrSubmitDTO.getExaminationId());
        Integer cmp=submitTime.compareTo(endTime);
        //按时提交
        if(cmp<=0){
            /*插入提交记录*/
            if(examSubmitRecordDao.insertRecord(studentId,examinationId,submitTime)==0){
                flag=false;
            }
            if(flag){
                log.info("作答已上传");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
            else{
                log.info("作答上传失败");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            }
        }
        //超时提交
        else {
            log.info("超时提交作答，上传失败");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}