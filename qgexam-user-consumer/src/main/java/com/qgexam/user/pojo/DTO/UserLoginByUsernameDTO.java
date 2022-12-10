package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginByUsernameDTO {

    @NotBlank(message = "登录名不能为空")
    private String loginName;
    @NotBlank(message = "密码不能为空")
    private String password;
}
