package com.qgexam.user.controller;

import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.service.MessageInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController extends BaseController {

    @DubboReference
    private MessageInfoService messageInfoService;

    @GetMapping("/getBadgeNumber")
    public ResponseResult getBadgeNumber() {
        return ResponseResult.okResult(messageInfoService.getBadgeNumber(getUserId()));
    }

}
