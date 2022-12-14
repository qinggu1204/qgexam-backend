package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author tageshi
 * @date 2022/12/13 0:42
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRegisterDTO {
    @NotBlank(message = "姓名不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String rePassword; /*重复密码，验证两次是否为空*/
    private String phoneNumber;
    private String teacherNumber; /*工号*/
    private Integer schoolId;
    private String qualificationImg; /*教师资格证图片*/
}
