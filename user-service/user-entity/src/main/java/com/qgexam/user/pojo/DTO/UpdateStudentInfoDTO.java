package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 修改学生信息接口传入信息
 *
 * @author ljy
 * @since 2022-12-16 16:25:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentInfoDTO implements Serializable {
    //登录名
    private String loginName;
    //头像url
    private String headImg;
    //人脸照片url
    private String faceImg;
}
