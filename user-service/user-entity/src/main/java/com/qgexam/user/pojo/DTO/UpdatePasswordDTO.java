package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author tageshi
 * @date 2022/12/13 2:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO implements Serializable {
    @NotBlank(message = "手机号不能为空")
    private String loginName;
    @NotBlank(message = "验证码不能为空")
    private String code;
    @NotBlank(message = "新密码不能为空")
    private String password;
}
