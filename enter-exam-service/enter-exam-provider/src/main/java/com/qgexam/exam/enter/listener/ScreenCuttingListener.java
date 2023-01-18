package com.qgexam.exam.enter.listener;

import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.exam.enter.dao.StudentExamActionDao;
import com.qgexam.exam.enter.pojo.PO.StudentExamAction;
import com.qgexam.rabbit.constants.ExamRecordRabbitConstant;
import com.qgexam.rabbit.pojo.DTO.ScreenCuttingRabbitMessageDTO;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author yzw
 * @date 2023年01月17日 22:13
 */
@Slf4j
@Component
public class ScreenCuttingListener {

    @Autowired
    private StudentExamActionDao studentExamActionDao;


    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(queues = ExamRecordRabbitConstant.EXAM_ACTION_QUEUE_NAME)
    public void listenExamActionQueue(ScreenCuttingRabbitMessageDTO screenCuttingRabbitMessageDTO,
                                      Channel channel, Message message) throws IOException, InterruptedException {
        System.out.println("==================RabbitMessageListener.listenExamActionQueue===========");
        Integer studentId = screenCuttingRabbitMessageDTO.getStudentId();
        Integer examinationId = screenCuttingRabbitMessageDTO.getExaminationId();

        // 更新记录
        StudentExamAction studentExamAction = new StudentExamAction();
        studentExamAction.setStudentId(studentId);
        studentExamAction.setExaminationId(examinationId);
        studentExamAction.setType(ExamConstants.SCREEN_CUTTING_ACTION);
        int count = studentExamActionDao.insert(studentExamAction);
        if (count > 0) {
            log.info("学生{}切屏，更新记录成功", studentId);
            // 手动ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            log.info("学生{}切屏，更新记录失败", studentId);
            // 手动reject
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
