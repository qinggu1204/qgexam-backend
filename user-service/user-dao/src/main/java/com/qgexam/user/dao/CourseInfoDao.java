package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.CourseInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 课程信息表(CourseInfo)表数据库访问层
 *
 * @author yzw
 * @since 2022-12-16 20:27:57
 */
public interface CourseInfoDao extends BaseMapper<CourseInfo> {

    Integer insertCourseInfo(CourseInfo courseInfo);

    Integer insertCourseTeacher(@Param("courseId") Integer courseId, @Param("teacherId") Integer teacherId, @Param("userName") String userName);
}

