package com.qgexam.exam.finish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 考试信息表(ExaminationInfo)表数据库访问层
 *
 * @author tageshi
 * @since 2023-01-13 17:37:34
 */
public interface ExaminationInfoDao extends BaseMapper<com.qgexam.user.PO.ExaminationInfo> {
    Integer getExaminationPaperId(@Param("examinationId") Integer examinationId);
    Date getEndTimeDate(@Param("examinationId") Integer examinationId);
}

