package com.qgexam.exam.enter.listener;

import com.qgexam.exam.enter.dao.StudentExamActionDao;
import com.qgexam.exam.enter.pojo.PO.StudentExamAction;
import com.qgexam.rabbit.constants.ExamRecordRabbitConstant;
import com.qgexam.rabbit.pojo.PO.ScreenCuttingRabbitMessageDTO;
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
 * @date 2023年01月13日 16:44
 */
//@Component
@Slf4j
public class ExamActionListener {

    @Autowired
    private StudentExamActionDao studentExamActionDao;

//    @RabbitListener(queues = ExamRecordRabbitConstant.EXAM_ACTION_QUEUE_NAME)
    public void listenExamActionQueue(ScreenCuttingRabbitMessageDTO screenCuttingRabbitMessageDTO,
                                      Channel channel, Message message) throws IOException {
        Integer studentId = screenCuttingRabbitMessageDTO.getStudentId();
        Integer examinationId = screenCuttingRabbitMessageDTO.getExaminationId();

        // 判断该学生的该场考试是否已经有记录
        StudentExamAction studentExamAction = studentExamActionDao.selectByStudentIdAndExaminationId(studentId, examinationId);
        // 如果没有记录，先创建记录
        if (studentExamAction == null) {
            studentExamAction = new StudentExamAction();
            studentExamAction.setStudentId(studentId);
            studentExamAction.setExaminationId(examinationId);
            studentExamActionDao.insertStudentExamAction(studentExamAction);
        }
        // 获取记录的主键
        Integer id = studentExamAction.getId();
        // 更新记录
        int count = studentExamActionDao.updateScreenCuttingCount(id);
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
