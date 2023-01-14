package com.qgexam.exam.viewresults.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.qgexam.exam.viewresults.pojo.PO.CourseInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 课程信息表(CourseInfo)表数据库访问层
 *
 * @author ljy
 * @since 2022-1-6 19:43:20
 */
public interface CourseInfoDao extends BaseMapper<CourseInfo> {
    Integer getFinalScore(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
    List<Integer> getCourseIdListByExaminationId(@Param("examinationId") Integer examinationId);
    List<Integer> getStudentIdListByCourseId(@Param("courseId") Integer courseId);
}

