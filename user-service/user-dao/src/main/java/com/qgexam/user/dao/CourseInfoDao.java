package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.user.pojo.PO.CourseInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程信息表(CourseInfo)表数据库访问层
 *
 * @author tageshi
 * @since 2022-12-16 19:43:20
 */
public interface CourseInfoDao extends BaseMapper<CourseInfo> {
    IPage<CourseInfo> getCourseListByTeacher(@Param("teacherId") Integer teacherId,
                                             @Param("subjectId") Integer subjectId,
                                             @Param("year") String year,
                                             @Param("semester") String semester, IPage<CourseInfo> page);
}

