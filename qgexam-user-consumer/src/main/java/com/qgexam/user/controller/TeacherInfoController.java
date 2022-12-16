package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.user.pojo.VO.UserInfoVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tageshi
 * @date 2022/12/16 15:49
 */
@Validated
@RestController
@RequestMapping("/tea")
public class TeacherInfoController {

    /**
     * @description 获取教师信息
     * @return com.qgexam.common.core.api.ResponseResult
     * @aythor tageshi
     * @date 2022/12/16 15:58:20
     */
    @GetMapping("/getTeacherInfo")
    public ResponseResult getTeacherInfo(){
        SaSession session = StpUtil.getSession();
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        return ResponseResult.okResult(userInfoVO);
    }
}
