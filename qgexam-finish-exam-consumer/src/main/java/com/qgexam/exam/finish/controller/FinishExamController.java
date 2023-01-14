package com.qgexam.exam.finish.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.exam.finish.pojo.DTO.SaveOrSubmitDTO;
import com.qgexam.exam.finish.service.FinishExamService;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/stu")
public class FinishExamController extends BaseController {
    @DubboReference
    private FinishExamService finishExamService;
    @DubboReference
    private StudentInfoService studentInfoService;

    @PostMapping("/finishExam/saveOrSubmit")
    public ResponseResult saveOrSubmit(@Validated @RequestBody SaveOrSubmitDTO saveOrSubmitDTO){
        System.out.println(getStudentId());
        return ResponseResult.okResult(finishExamService.saveOrSubmit(saveOrSubmitDTO,getStudentId()));
    }
}