package com.qgexam.user.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.rabbit.constants.ExamActionRabbitConstant;
import com.qgexam.rabbit.pojo.DTO.ExamFaceComparisonRabbitDTO;
import com.qgexam.user.dao.StudentCheatRecordDao;
import com.qgexam.user.dao.StudentExamActionDao;
import com.qgexam.user.pojo.PO.StudentCheatRecord;
import com.qgexam.user.pojo.PO.StudentExamAction;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 *
 * @author peter guo
 * @since 2023-01-18 12:27:39
 */
@Slf4j
//@Component
public class RabbitMessageListener {

    @Autowired
    private StudentExamActionDao studentExamActionDao;

    @Autowired
    private StudentCheatRecordDao studentCheatRecordDao;

    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(queues = ExamActionRabbitConstant.EXAM_ACTION_FACE_QUEUE_NAME)
    public void listenExamActionQueue(ExamFaceComparisonRabbitDTO examFaceComparisonRabbitDTO,
                                      Channel channel, Message message) throws IOException {
        Integer studentId = examFaceComparisonRabbitDTO.getStudentId();
        Integer examinationId = examFaceComparisonRabbitDTO.getExaminationId();

        //插入记录
        StudentExamAction studentExamAction = new StudentExamAction();
        studentExamAction.setStudentId(studentId);
        studentExamAction.setExaminationId(examinationId);
        studentExamAction.setType(ExamConstants.FACE_ACTION);
        int count = studentExamActionDao.insert(studentExamAction);

        if (count > 0) {
            log.info("学生{}人脸验证失败记录插入成功", studentId);
            // 手动ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            log.info("学生{}人脸验证失败记录插入失败", studentId);
            // 手动reject
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

        LambdaQueryWrapper<StudentExamAction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentExamAction::getStudentId, studentId)
                .eq(StudentExamAction::getExaminationId, examinationId)
                .eq(StudentExamAction::getType, ExamConstants.FACE_ACTION);
        Long num = studentExamActionDao.selectCount(queryWrapper);
        if (num >= 5){
            StudentCheatRecord studentCheatRecord = new StudentCheatRecord();
            studentCheatRecord.setStudentId(studentId);
            studentCheatRecord.setExaminationId(examinationId);
            int insertCount = studentCheatRecordDao.insert(studentCheatRecord);
            if(insertCount < 1){
                throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
            }
        }
    }


}
