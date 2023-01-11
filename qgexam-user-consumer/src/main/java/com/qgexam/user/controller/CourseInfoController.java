package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.GetCourseListDTO;
import com.qgexam.user.service.CourseInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class CourseInfoController extends BaseController {
    @Reference
    private CourseInfoService courseInfoService;

    @GetMapping("/common/getCourseList")
    public ResponseResult getCourseList(@Validated GetCourseListDTO getCourseListDTO, Integer currentPage, Integer pageSize){
        SaSession session= StpUtil.getSession();
        return ResponseResult.okResult(courseInfoService.getCourseList(session,getCourseListDTO.getSubjectId(),getCourseListDTO.getYear(),getCourseListDTO.getSemester(),currentPage,pageSize));
    }
}