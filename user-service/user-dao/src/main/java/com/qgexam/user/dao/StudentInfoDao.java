package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 学生表(StudentInfo)表数据库访问层
 *
 * @author ljy
 * @since 2022-12-16 14:27:46
 */
public interface StudentInfoDao extends BaseMapper<StudentInfo> {
    Integer updatefaceImgByUserId(@Param("faceImg") String faceImg, @Param("userId") Integer userId);
}

