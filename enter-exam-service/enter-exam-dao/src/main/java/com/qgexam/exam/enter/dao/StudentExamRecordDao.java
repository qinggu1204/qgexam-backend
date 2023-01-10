package com.qgexam.exam.enter.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.enter.pojo.PO.StudentExamRecord;

/**
 * 考试信息表(StudentExamRecord)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-10 15:03:13
 */
public interface StudentExamRecordDao extends BaseMapper<StudentExamRecord> {
    StudentExamRecord selectByStudentIdAndExaminationId(@Param("studentId") Integer studentId,
                                                        @Param("examinationId") Integer examinationId);
}

