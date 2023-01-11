package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.VO.ScoreVO;
import com.qgexam.user.pojo.VO.StudentVO;
import org.apache.ibatis.annotations.Param;

/**
 * 教师表(TeacherInfo)表数据库访问层
 *
 * @author tageshi
 * @since 2022-12-16 17:37:04
 */
public interface TeacherInfoDao extends BaseMapper<TeacherInfo> {
    IPage<StudentVO> getStudentPage(@Param("courseId") Integer courseId, IPage<StudentVO> page);

    IPage<ScoreVO> getScorePage(@Param("courseId") Integer courseId, IPage<ScoreVO> page);
}

