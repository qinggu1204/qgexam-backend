package com.qgexam.user.controller;

import com.qgexam.common.core.api.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PostMapping("/tea/test")
    public ResponseResult userLoginByUsername() {

        return ResponseResult.okResult(111);
    }


}
