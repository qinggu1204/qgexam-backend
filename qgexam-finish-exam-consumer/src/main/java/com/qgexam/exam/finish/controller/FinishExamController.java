package com.qgexam.exam.finish.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import com.qgexam.exam.finish.service.FinishExamService;
import com.qgexam.rabbit.constants.FinishExamRabbitConstants;
import com.qgexam.rabbit.constants.SaveExamRabbitConstants;
import com.qgexam.rabbit.pojo.DTO.FinishExamDTO;
import com.qgexam.rabbit.service.RabbitService;
import com.qgexam.user.service.StudentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Validated
@RestController
@RequestMapping("/stu/finishExam")
public class FinishExamController extends BaseController {
    @DubboReference
    private FinishExamService finishExamService;
    @DubboReference
    private StudentInfoService studentInfoService;
    @DubboReference(registry = "rabbitmqRegistry")
    private RabbitService rabbitService;

    @PostMapping("/saveOrSubmit")
    public ResponseResult saveOrSubmit(@Validated @RequestBody SaveOrSubmitDTO saveOrSubmitDTO){
        /*获取时间*/
        LocalDateTime submitTime=LocalDateTime.now();
        /*封装消息队列*/
        FinishExamDTO finishExamDTO=new FinishExamDTO();
        finishExamDTO.setStudentId(getStudentId());
        finishExamDTO.setExaminationId(saveOrSubmitDTO.getExaminationId());
        finishExamDTO.setSubmitTime(submitTime);
        /*判断是否在考试结束时间前提交（考试是否合法）*/
        if(submitTime.compareTo(finishExamService.getEndTime(saveOrSubmitDTO.getExaminationId()))<0){
            /*发送一条提交学生作答消息*/
            rabbitService.sendMessage(FinishExamRabbitConstants.EXAM_FINISH_EXCHANGE_NAME,
                    FinishExamRabbitConstants.EXAM_FINISH_ROUTING_KEY,finishExamDTO);
        }
        return ResponseResult.okResult(finishExamService.saveOrSubmit(saveOrSubmitDTO,getStudentId()));
    }
    @PostMapping("/save")
    public ResponseResult save(@Validated @RequestBody SaveOrSubmitDTO saveOrSubmitDTO){
        /*获取时间*/
        LocalDateTime submitTime=LocalDateTime.now();
        /*封装消息队列*/
        FinishExamDTO finishExamDTO=new FinishExamDTO();
        finishExamDTO.setStudentId(getStudentId());
        finishExamDTO.setExaminationId(saveOrSubmitDTO.getExaminationId());
        finishExamDTO.setSubmitTime(submitTime);
        return ResponseResult.okResult(finishExamService.save(saveOrSubmitDTO,getStudentId()));
    }
}