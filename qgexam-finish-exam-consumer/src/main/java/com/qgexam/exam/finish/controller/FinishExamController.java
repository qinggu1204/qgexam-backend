package com.qgexam.exam.finish.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import com.qgexam.exam.finish.service.FinishExamService;
import com.qgexam.rabbit.constants.FinishExamRabbitConstants;
import com.qgexam.rabbit.pojo.DTO.FinishExamDTO;
import com.qgexam.rabbit.service.RabbitService;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
        /*封装消息队列*/
        FinishExamDTO finishExamDTO=new FinishExamDTO();
        finishExamDTO.setStudentId(getStudentId());
        finishExamDTO.setExaminationId(saveOrSubmitDTO.getExaminationId());
        finishExamDTO.setSubmitTime(new Date());
        /*判断是否在考试结束时间前提交*/
        if(finishExamService.isCorrectSubmitted(finishExamDTO.getExaminationId(),finishExamDTO.getSubmitTime())){
            /*发送一条保存学生作答消息*/
            rabbitService.sendMessage(FinishExamRabbitConstants.EXAM_FINISH_EXCHANGE_NAME,
                    FinishExamRabbitConstants.EXAM_FINISH_ROUTING_KEY,finishExamDTO);
        }

        return ResponseResult.okResult(finishExamService.saveOrSubmit(saveOrSubmitDTO,getStudentId()));
    }
}