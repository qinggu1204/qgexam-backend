package com.qgexam.exam.enter.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.exam.enter.pojo.DTO.GetExamListDTO;
import com.qgexam.exam.enter.service.ExaminationInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author yzw
 * @description
 * @date 2023/1/6 14:37:41
 */
@Validated
@RestController
@RequestMapping("/stu/enterExam")
public class EnterExaminationController {

    @DubboReference
    private ExaminationInfoService examinationInfoService;

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

    @GetMapping("/test")
    public ResponseResult test() throws SchedulerException {
        return ResponseResult.okResult(examinationInfoService.test());
    }
}
