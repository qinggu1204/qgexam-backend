package com.qgexam.user.controller;

import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import com.qgexam.user.pojo.DTO.GetMessageCodeDTO;
import com.qgexam.user.pojo.DTO.UserLoginByPhoneNumberDTO;
import com.qgexam.user.service.MessageCodeService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/user")
public class MessageCodeController {

    @Reference
    private MessageCodeService messageCodeService;

    /**
     * @description 发送短信验证码
     * @param codeDTO
     * @return com.qgexam.common.core.api.ResponseResult
     * @author ljy
     * @date 2022/12/14 21:21:43
     */
    @GetMapping("/sendCode")
    public ResponseResult loginByCode(@Validated GetMessageCodeDTO codeDTO) {
        String phoneNumber = codeDTO.getPhoneNumber();
        if (messageCodeService.sendCode(phoneNumber)) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * @description 校验短信验证码
     * @param codeDTO
     * @return com.qgexam.common.core.api.ResponseResult
     * @author ljy
     * @date 2022/12/14 22:57:13
     */
    @PostMapping("/validateCode")
    public ResponseResult loginByCode(@Validated @RequestBody UserLoginByPhoneNumberDTO codeDTO) {
        String phoneNumber = codeDTO.getPhoneNumber();
        String code = codeDTO.getCode();
        if (messageCodeService.validateCode(phoneNumber,code)) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.CODE_ERROR);
    }
}
