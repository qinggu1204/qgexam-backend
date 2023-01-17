package com.qgexam.exam.finish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.finish.pojo.PO.ExamSubmitRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * (ExamSubmitRecord)表数据库访问层
 *
 * @author tageshi
 * @since 2023-01-13 18:01:03
 */
public interface ExamSubmitRecordDao extends BaseMapper<ExamSubmitRecord> {
    Integer insertRecord(@Param("studentId")Integer studentId,
                         @Param("examinationId")Integer examinationId,
                         @Param("submitTime")Date submitTime);
}

