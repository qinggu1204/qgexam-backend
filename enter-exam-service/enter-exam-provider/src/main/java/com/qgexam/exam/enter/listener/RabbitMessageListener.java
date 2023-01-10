package com.qgexam.exam.enter.listener;

import cn.hutool.log.Log;
import com.qgexam.exam.enter.config.ExamRecordRabbitConfig;
import com.qgexam.exam.enter.dao.StudentExamRecordDao;
import com.qgexam.exam.enter.pojo.DTO.ExamRecordDTO;
import com.qgexam.exam.enter.pojo.PO.StudentExamRecord;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @RabbitListener(queues = ExamRecordRabbitConfig.EXAM_RECORD_QUEUE_NAME)
    public void listenExamRecordQueue(ExamRecordDTO examRecordDTO, Channel channel, Message message) throws IOException {
        Integer studentId = examRecordDTO.getStudentId();
        Integer examinationId = examRecordDTO.getExaminationId();
        LocalDateTime enterTime = examRecordDTO.getEnterTime();
        StudentExamRecord examRecord = studentExamRecordDao.selectByStudentIdAndExaminationId(studentId, examinationId);
        if (examRecord != null) {
            log.info("学生{}已经进入考试{}，不需要再次记录", studentId, examinationId);
            // 手动ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        StudentExamRecord studentExamRecord = new StudentExamRecord();
        studentExamRecord.setStudentId(studentId);
        studentExamRecord.setExaminationId(examinationId);
        studentExamRecord.setEnterTime(enterTime);

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
