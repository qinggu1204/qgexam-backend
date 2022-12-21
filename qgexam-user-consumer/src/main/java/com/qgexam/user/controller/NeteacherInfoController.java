package com.qgexam.user.controller;

import com.qgexam.common.core.api.ResponseResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/netea")
public class NeteacherInfoController {

    /**
     * @description 获取教师信息
     * @return com.qgexam.common.core.api.ResponseResult
     * @aythor peter guo
     * @date 2022/12/21 19:32:44
     */
    @PostMapping("/createPaper")
    public ResponseResult getStudentList(){
        return ResponseResult.okResult();
    }
}
