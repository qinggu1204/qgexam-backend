package com.qgexam.exam.finish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 题目信息表(QuestionInfo)表数据库访问层
 *
 * @author tageshi
 * @since 2023-01-09 19:40:57
 */
public interface QuestionInfoDao extends BaseMapper<PO.QuestionInfo> {
    public Integer hasSubQuestion(@Param("questionId")Integer questionId);
    public String geyTypeByQuestionId(@Param("questionId")Integer questionId);
    public String getCorrectAnswer(@Param("questionId")Integer questionId);
}

