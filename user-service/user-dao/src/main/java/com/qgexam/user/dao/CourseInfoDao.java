package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.DTO.CourseTeacherDTO;
import com.qgexam.user.pojo.PO.CourseInfo;
import com.qgexam.user.pojo.VO.CourseTeacherListVO;
import com.qgexam.user.pojo.VO.GetCourseListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 课程信息表(CourseInfo)表数据库访问层
 *
 * @author tageshi
 * @since 2022-12-16 19:43:20
 */
public interface CourseInfoDao extends BaseMapper<CourseInfo> {

    List<GetCourseListVO> getCourseListByTeacher(@Param("teacherId")Integer teacherId, @Param("subjectId") Integer subjectId, @Param("year") String year, @Param("semester") String semester);
    List<GetCourseListVO> getCourseListByStudent(@Param("studentId")Integer teacherId, @Param("subjectId") Integer subjectId, @Param("year") String year, @Param("semester") String semester);
    List<GetCourseListVO> getCourseListByAdmin(@Param("subjectId") Integer subjectId, @Param("year") String year, @Param("semester") String semester);
    List<GetCourseListVO> getCourseListByNeteacher(@Param("schoolId")Integer schoolId, @Param("subjectId") Integer subjectId, @Param("year") String year, @Param("semester") String semester);
    Integer insertCourseInfo(CourseInfo courseInfo);

    Integer insertCourseTeacher(CourseTeacherDTO courseTeacherDTO);
    List<CourseTeacherListVO> getCourseTeacherList(Integer userId);
    List<CourseInfo> getCourseListBySubject(@Param("subjectId") Integer subjectId);

}

