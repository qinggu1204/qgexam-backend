package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询学生信息接口返回信息
 *
 * @author peter guo
 * @since 2022-12-16 19:46:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentVO implements Serializable {
    //姓名(用户名)
    private String userName;
    //学号
    private String studentNumber;
}
