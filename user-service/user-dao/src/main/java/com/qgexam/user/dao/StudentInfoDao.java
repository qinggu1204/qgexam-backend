package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.UserInfo;

/**
 * 学生表(StudentInfo)表数据库访问层
 *
 * @author peter guo
 * @since 2022-12-15 14:27:46
 */
public interface StudentInfoDao extends BaseMapper<StudentInfo> {
    Integer updateStudentInfo(StudentInfo studentInfo);
}

