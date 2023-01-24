package com.qgexam.exam.enter.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.enter.pojo.PO.StudentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生表(StudentInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-24 10:48:39
 */
public interface StudentInfoDao extends BaseMapper<StudentInfo> {

    List<Integer> selectUserIdListByStudentIdList(@Param("studentIdList") List<Integer> studentIdList);
}

