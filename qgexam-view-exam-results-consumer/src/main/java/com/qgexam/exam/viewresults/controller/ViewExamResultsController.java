package com.qgexam.exam.viewresults.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.exam.viewresults.service.CourseInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ljy
 * @description
 * @date 2023/1/6 14:37:41
 */
@Validated
@RestController
@RequestMapping("/stu/viewExamResults")
public class ViewExamResultsController extends BaseController {

    @DubboReference
    private CourseInfoService courseInfoService;

    /**
     * 学生查看课程成绩
     *
     * @return ResponseResult
     * @author ljy
     * @date 2023/1/6 15:59
     */

    @GetMapping("/getCourseScore")
    public ResponseResult getCourseScore(Integer courseId) {
        return ResponseResult.okResult(courseInfoService.getFinalScore(courseId,getStudentId()));
    }
}
