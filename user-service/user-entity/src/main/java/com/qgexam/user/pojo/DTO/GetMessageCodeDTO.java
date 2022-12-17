package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author ljy
 * @description 接收手机号参数
 * @date 2022/12/14 22:50:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMessageCodeDTO implements Serializable {
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号格式不正确")
    private String phoneNumber;
}
