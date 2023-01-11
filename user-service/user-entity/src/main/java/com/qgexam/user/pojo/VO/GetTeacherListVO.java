package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ljy
 * @date 2022/1/7 16:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTeacherListVO implements Serializable {
    private Integer teacherId;
    private Integer userId;
    private String teacherName;
    private String loginName;
}
