package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.pojo.VO.GetTeacherInfoVO;
import com.qgexam.user.pojo.VO.UserInfoVO;
import com.qgexam.user.service.TeacherInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tageshi
 * @date 2022/12/16 15:49
 */
@RestController
@RequestMapping("/tea")
public class TeacherInfoController extends BaseController {
    @Reference
    TeacherInfoService teacherInfoService;

    /**
     * @description 获取教师信息
     * @return com.qgexam.common.core.api.ResponseResult
     * @aythor tageshi
     * @date 2022/12/16 15:58:20
     */
    @GetMapping("/getTeacherInfo")
    public ResponseResult getTeacherInfo(){
        SaSession session = StpUtil.getSession();
        return ResponseResult.okResult(teacherInfoService.getTeacherInfo(session));
    }
}
