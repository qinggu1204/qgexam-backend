package com.qgexam.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.db.Session;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.common.web.base.BaseController;
import com.qgexam.user.service.MessageInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/common")
public class CommonController extends BaseController {

    @DubboReference
    private MessageInfoService messageInfoService;

    /**
     * @description 获取消息列表
     * @return com.qgexam.common.core.api.ResponseResult
     * @author ljy
     * @date 2022/12/30 21:21:43
     */
    @GetMapping("/getMessageList")
    public ResponseResult getMessageList(Integer currentPage, Integer pageSize) {
        return ResponseResult.okResult(messageInfoService.getMessageList(getUserId(),currentPage,pageSize));
    }
    @GetMapping("/getMessage")
    public ResponseResult getMessage(@Validated Integer messageId){
        return ResponseResult.okResult(messageInfoService.getMessage(getUserId(),messageId));
    }
}
