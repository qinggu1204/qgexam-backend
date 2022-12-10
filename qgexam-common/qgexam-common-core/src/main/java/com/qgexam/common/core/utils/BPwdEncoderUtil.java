package com.qgexam.common.core.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Auther: dcy
 * @Date: 2019/3/22 10:21
 * @Description: 密码加密
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
