package com.qgexam.user.controller;

import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.user.pojo.DTO.TeacherRegisterDTO;
import com.qgexam.user.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PostMapping("/tea/test")
    public ResponseResult userLoginByUsername() {

        return ResponseResult.okResult(111);
    }
}
