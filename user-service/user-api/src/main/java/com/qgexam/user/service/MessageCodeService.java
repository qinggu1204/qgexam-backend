package com.qgexam.user.service;
/**
 * 短信验证码服务接口
 *
 * @author ljy
 * @since 2022-12-15 11:12:05
 */
public interface MessageCodeService {
    boolean sendCode(String phoneNumber);
    boolean validateCode(String phoneNumber,String code);
}
