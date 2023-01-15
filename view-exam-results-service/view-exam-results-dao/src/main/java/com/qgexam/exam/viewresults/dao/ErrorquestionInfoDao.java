package com.qgexam.exam.viewresults.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.viewresults.pojo.PO.ErrorquestionInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 错题信息表(ErrorquestionInfo)表数据库访问层
 *
 * @author ljy
 * @since 2023-01-15 11:55:31
 */
public interface ErrorquestionInfoDao extends BaseMapper<ErrorquestionInfo> {
    List<ErrorquestionInfo> selectErrorquestionInfoList(@Param("courseId")Integer courseId, @Param("studentId")Integer studentId);
}

