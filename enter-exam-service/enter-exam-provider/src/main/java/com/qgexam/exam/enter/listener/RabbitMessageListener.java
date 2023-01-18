package com.qgexam.exam.enter.listener;

import com.qgexam.exam.enter.dao.StudentExamActionDao;
import com.qgexam.exam.enter.dao.StudentExamRecordDao;
import com.qgexam.exam.enter.pojo.PO.StudentExamAction;
import com.qgexam.exam.enter.pojo.PO.StudentExamRecord;
import com.qgexam.rabbit.constants.ExamRecordRabbitConstant;
import com.qgexam.rabbit.pojo.DTO.ExamRecordDTO;
import com.qgexam.rabbit.pojo.DTO.ScreenCuttingRabbitMessageDTO;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author yzw
 * @date 2023年01月10日 13:54
 */
@Slf4j
@Component
public class RabbitMessageListener {
    @Autowired
    private StudentExamRecordDao studentExamRecordDao;


    @RabbitListener(queues = ExamRecordRabbitConstant.EXAM_RECORD_QUEUE_NAME)
    public void listenExamRecordQueue(ExamRecordDTO examRecordDTO, Channel channel, Message message) throws IOException {
        System.out.println("==================RabbitMessageListener.listenExamRecordQueue===========");
        Integer studentId = examRecordDTO.getStudentId();
        Integer examinationId = examRecordDTO.getExaminationId();
        LocalDateTime enterTime = examRecordDTO.getEnterTime();
        String remoteIp = examRecordDTO.getRemoteIp();

        StudentExamRecord studentExamRecord = new StudentExamRecord();
        studentExamRecord.setStudentId(studentId);
        studentExamRecord.setExaminationId(examinationId);
        studentExamRecord.setEnterTime(enterTime);
        studentExamRecord.setRemoteIp(remoteIp);

        int count = studentExamRecordDao.insert(studentExamRecord);
        if (count > 0) {
            log.info("学生{}进入考试{}，记录成功", studentId, examinationId);
            // 手动ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            log.info("学生{}进入考试{}，记录失败", studentId, examinationId);
            // 手动reject
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }



}
