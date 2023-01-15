package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.GetStudentVO;
import com.qgexam.user.pojo.VO.GetTeacherListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生表(StudentInfo)表数据库访问层
 *
 * @author ljy
 * @since 2022-12-16 14:27:46
 */
public interface StudentInfoDao extends BaseMapper<StudentInfo> {
    Integer updatefaceImgByUserId(@Param("faceImg") String faceImg, @Param("userId") Integer userId);
    Integer joinCourse(@Param("studentId") Integer studentId, @Param("userName") String userName,@Param("studentNumber") String studentNumber, @Param("courseId") Integer courseId);
    IPage<GetStudentVO> getStudentList(@Param("schoolId")Integer schoolId,
                                       @Param("loginName")String loginName, IPage<GetStudentVO> page);
    Integer updateStudentNumber(@Param("studentId")Integer studentId,@Param("newStudentNUmber")String newStudentNumber);
    Integer updateStudentNumberInStudentCourse(@Param("studentId")Integer studentId,@Param("newStudentNUmber")String newStudentNumber);
}

