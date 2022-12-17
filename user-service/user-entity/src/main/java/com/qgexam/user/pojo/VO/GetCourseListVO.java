package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author tageshi
 * @date 2022/12/16 19:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCourseListVO implements Serializable {
    Integer isDeleted;
    Integer courseId;
    List<CourseTeacherListVO> teacherList;
    String year;
    String courseName;
    String courseUrl;
    String semester;
}
