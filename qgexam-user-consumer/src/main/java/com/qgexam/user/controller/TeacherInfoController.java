package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.DTO.UpdateTeacherInfoDTO;
import com.qgexam.user.service.TeacherInfoService;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author tageshi
 * @date 2022/12/16 15:49
 */
@RestController
@RequestMapping("/tea")
public class TeacherInfoController extends BaseController {
    @Reference
    TeacherInfoService teacherInfoService;
    @Reference
    private UserInfoService userInfoService;

    @GetMapping("/getTeacherInfo")
    public ResponseResult getTeacherInfo(){
        SaSession session = StpUtil.getSession();
        return ResponseResult.okResult(teacherInfoService.getTeacherInfo(session));
    }

    @PutMapping("/updateTeacherInfo")
    public ResponseResult updateTeacherInfo(@RequestBody @Validated UpdateTeacherInfoDTO updateTeacherInfoDTO) {
        userInfoService.updateTeacherInfo(getUserId(), updateTeacherInfoDTO);
        return ResponseResult.okResult();
    }

}
