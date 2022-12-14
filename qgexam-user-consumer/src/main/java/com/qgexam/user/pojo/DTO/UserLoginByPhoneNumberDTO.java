package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author yzw
 * @description 接收手机号验证码登录参数
 * @date 2022/12/14 16:12:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginByPhoneNumberDTO {

    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号格式不正确")
    private String phoneNumber;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
