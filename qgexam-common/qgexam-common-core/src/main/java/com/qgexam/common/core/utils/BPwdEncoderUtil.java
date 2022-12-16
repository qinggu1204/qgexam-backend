package com.qgexam.common.core.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @project qgexam
 * @description 密码加密工具类
 * @author yzw
 * @date 2022/12/16 09:47:42
 * @version 1.0
 */
public class BPwdEncoderUtil {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 匹配密码
     *
     * @param rawPassword     未加密密码
     * @param encodedPassword 加密密码
     * @return
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}
