package com.qgexam.exam.enter.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.rabbit.constants.ExamRecordRabbitConstant;
import com.qgexam.rabbit.pojo.PO.ExamRecordDTO;
import com.qgexam.exam.enter.pojo.DTO.GetExamListDTO;
import com.qgexam.exam.enter.pojo.VO.GetExaminationPaperVO;
import com.qgexam.exam.enter.service.EnterExamService;
import com.qgexam.exam.enter.service.ExaminationInfoService;
import com.qgexam.rabbit.config.ExamRecordRabbitConfig;
import com.qgexam.rabbit.service.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author yzw
 * @description
 * @date 2023/1/6 14:37:41
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/stu/enterExam")
public class EnterExaminationController extends BaseController {

    @DubboReference
    private ExaminationInfoService examinationInfoService;

    @DubboReference
    private EnterExamService enterExamService;

    @DubboReference(registry = "rabbitmqRegistry")
    private RabbitService rabbitService;

    /**
     * 获取考试列表
     *
     * @param courseId
     * @param examinationName
     * @param currentPage
     * @param pageSize
     * @return ResponseResult
     * @author yzw
     * @date 2023/1/6 15:59
     */

    @GetMapping("/getExamList")
    public ResponseResult getExamList(Integer courseId, String examinationName,
                                      @NotNull(message = "currentPage不能为空") Integer currentPage,
                                      @NotNull(message = "pageSize不能为空") Integer pageSize) {
        return ResponseResult.okResult(examinationInfoService.getExamList(new GetExamListDTO(courseId, examinationName, currentPage, pageSize)));

    }

    @GetMapping("/joinExam")
    public ResponseResult joinExam(@NotNull Integer examinationId) {
        // 获取试卷
        GetExaminationPaperVO examinationPaper = enterExamService.getExaminationPaper(examinationId);
        // 向学生考试记录消息队列中发送一条消息
        //封装队列消息
        ExamRecordDTO examRecordDTO = new ExamRecordDTO();
        examRecordDTO.setExaminationId(examinationId);
        examRecordDTO.setStudentId(getStudentId());
        examRecordDTO.setEnterTime(LocalDateTime.now());

        // 消息队列发送一条消息
        rabbitService.sendMessage(ExamRecordRabbitConstant.EXAM_RECORD_EXCHANGE_NAME,
                ExamRecordRabbitConstant.EXAM_RECORD_ROUTING_KEY,
                examRecordDTO);
        return ResponseResult.okResult(examinationPaper);
    }

    @GetMapping("/getExamInfo")
    public ResponseResult getExaminationInfo(@NotNull Integer examinationId) {
        return ResponseResult.okResult(enterExamService.getExaminationInfo(examinationId));
    }


}
