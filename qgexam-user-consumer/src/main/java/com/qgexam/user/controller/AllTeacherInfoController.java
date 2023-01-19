package com.qgexam.user.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.user.service.TeacherInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alltea")
public class AllTeacherInfoController {

    @DubboReference
    private TeacherInfoService teacherInfoService;

    @GetMapping("/getScoreList")
    public ResponseResult getScoreList(Integer courseId,Integer currentPage,Integer pageSize) {
        return ResponseResult.okResult(teacherInfoService.getScoreList(courseId,currentPage,pageSize));
    }
}
