package com.qgexam.user.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description 验证码测试
 * @author ljy
 * @date 2022/12/14 19:20:04
 */

@SpringBootTest
public class MessagecodeTest {

    @DubboReference
    private MessageCodeService messageCodeService;

    @Test
    public void getMessagecodeTest() {
        System.out.println(messageCodeService.sendCode("17816139690"));
    }

    @Test
    public void validateMessagecodeTest() {
        System.out.println(messageCodeService.validateCode("17816139690","739700"));
    }

}
