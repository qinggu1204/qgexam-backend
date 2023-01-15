package com.qgexam.exam.finish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 试卷题目关系表(ExaminationPaperQuestion)表数据库访问层
 *
 * @author tageshi
 * @since 2023-01-13 17:32:33
 */
public interface ExaminationPaperQuestionDao extends BaseMapper<PO.ExaminationPaperQuestion> {
    Integer getScore(@Param("examinationPaperId")Integer examinationPaperId, @Param("questionId")Integer questionId);
}

