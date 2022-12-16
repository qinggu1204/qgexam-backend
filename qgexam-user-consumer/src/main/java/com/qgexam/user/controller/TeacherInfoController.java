package com.qgexam.user.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.UpdateTeacherInfoDTO;
import com.qgexam.user.service.TeacherInfoService;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/tea")
public class TeacherInfoController extends BaseController {

    @Reference
    private UserInfoService userInfoService;

    @Reference
    private TeacherInfoService teacherInfoService;

    @PutMapping("/updateTeacherInfo")
    public ResponseResult updateTeacherInfo(@RequestBody @Validated UpdateTeacherInfoDTO updateTeacherInfoDTO) {
        userInfoService.updateTeacherInfo(getUserId(), updateTeacherInfoDTO);
        return ResponseResult.okResult();
    }

    @GetMapping("/getStudentList/{courseId}")
    public ResponseResult getStudentList(@PathVariable Integer courseId,Integer currentPage,Integer pageSize) {
        return ResponseResult.okResult(teacherInfoService.getStudentList(courseId,currentPage,pageSize));
    }

}
