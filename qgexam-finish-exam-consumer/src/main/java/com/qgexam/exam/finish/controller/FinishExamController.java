package com.qgexam.exam.finish.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import com.qgexam.exam.finish.service.FinishExamService;
import com.qgexam.rabbit.constants.FinishExamRabbitConstants;
import com.qgexam.rabbit.service.RabbitService;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        /*发送一条保存学生作答消息*/
        rabbitService.sendMessage(FinishExamRabbitConstants.EXAM_FINISH_EXCHANGE_NAME,
                FinishExamRabbitConstants.EXAM_FINISH_ROUTING_KEY,saveOrSubmitDTO);
        return ResponseResult.okResult(finishExamService.saveOrSubmit(saveOrSubmitDTO,getStudentId()));
    }
}