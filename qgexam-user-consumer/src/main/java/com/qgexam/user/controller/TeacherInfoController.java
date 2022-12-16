package com.qgexam.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.CreateCourseDTO;
import com.qgexam.user.pojo.DTO.UpdateTeacherInfoDTO;
import com.qgexam.user.service.TeacherInfoService;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 教师信息controller
 * @author yzw
 */
@RestController
@Validated
@RequestMapping("/tea")
public class TeacherInfoController extends BaseController {

    @Reference
    private UserInfoService userInfoService;

    @Reference
    private TeacherInfoService teacherInfoService;

    /**
     * 教师修改个人信息
     * @author yzw
     * @param updateTeacherInfoDTO
     * @return
     */
    @PutMapping("/updateTeacherInfo")
    public ResponseResult updateTeacherInfo(@RequestBody @Validated UpdateTeacherInfoDTO updateTeacherInfoDTO) {
        userInfoService.updateTeacherInfo(getUserId(), updateTeacherInfoDTO);
        return ResponseResult.okResult();
    }

    /**
     * 教师创建课程
     * @author yzw
     * @param createCourseDTO
     * @return
     */
    @PostMapping("/createCourse")
    public ResponseResult createCourse(@RequestBody @Validated CreateCourseDTO createCourseDTO) {
        teacherInfoService.createCourse(getUserId(), getUserName(), createCourseDTO);
        return ResponseResult.okResult();
    }

}
