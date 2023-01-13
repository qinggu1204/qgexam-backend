package com.qgexam.exam.enter.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.enter.pojo.PO.StudentExamAction;
import org.apache.ibatis.annotations.Param;

/**
 * (StudentExamAction)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-13 15:20:13
 */
public interface StudentExamActionDao extends BaseMapper<StudentExamAction> {

    StudentExamAction selectByStudentIdAndExaminationId(@Param("studentId") Integer studentId,
                                                        @Param("examinationId") Integer examinationId);

    Integer insertStudentExamAction(StudentExamAction insertStudentExamAction);

    Integer updateScreenCuttingCount(Integer id);
}

