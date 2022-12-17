package com.qgexam.user.pojo.VO;

import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @description redis中存储的用户信息
 * @date 2022/12/15 23:03:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO implements Serializable {
    private UserInfo userInfo;
    private StudentInfo studentInfo;
    private TeacherInfo teacherInfo;
}
