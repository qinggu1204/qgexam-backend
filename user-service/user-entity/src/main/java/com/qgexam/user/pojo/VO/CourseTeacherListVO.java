package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2022/12/17 16:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseTeacherListVO implements Serializable {
    Integer teacherId;
    String userName;
}
