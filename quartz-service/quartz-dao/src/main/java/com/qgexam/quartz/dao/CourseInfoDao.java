package com.qgexam.quartz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.quartz.pojo.PO.CourseInfo;

import java.util.List;

/**
 * 课程信息表(CourseInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-20 10:16:38
 */
public interface CourseInfoDao extends BaseMapper<CourseInfo> {

    List<Integer> selectStudentIdListByCourseId(Integer courseId);
}

