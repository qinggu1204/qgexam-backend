package com.qgexam.exam.enter.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.enter.pojo.PO.CourseInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程信息表(CourseInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-20 22:42:09
 */
public interface CourseInfoDao extends BaseMapper<CourseInfo> {

    List<CourseInfo> selectCourseInfoListByStudentId(Integer studentId);
    List<Integer> selectStudentIdListByCourseIds(@Param("courseIdList") List<Integer> courseIdList);

}

