package com.qgexam.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.user.service.StudentInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stu")
public class StudentInfoController {

    @Reference
    private StudentInfoService studentInfoService;

    /**
     * @return com.qgexam.common.core.api.ResponseResult
     * @auther peter guo
     * @date 2022/12/15 14:37:25
     */
    @GetMapping("/getStudentInfo")
    public ResponseResult getStudentInfo() {
        //根据token获取用户编号
        Integer userId = StpUtil.getLoginIdAsInt();
        studentInfoService.getStudentInfoByUserId(userId);
        return ResponseResult.okResult();
    }
}
