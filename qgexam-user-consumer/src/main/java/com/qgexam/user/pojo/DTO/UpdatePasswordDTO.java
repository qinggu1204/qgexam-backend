package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tageshi
 * @date 2022/12/13 2:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {
    private String phone;
    private String code;
    private String newPassword;
    private String reNewPassword; //重复密码
}
