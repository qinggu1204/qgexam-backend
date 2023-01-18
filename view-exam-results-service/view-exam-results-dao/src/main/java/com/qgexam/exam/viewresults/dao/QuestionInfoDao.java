package com.qgexam.exam.viewresults.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.viewresults.pojo.PO.QuestionInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 题目信息表(QuestionInfo)表数据库访问层
 *
 * @author ljy
 * @since 2023-01-10 13:00:38
 */
public interface QuestionInfoDao extends BaseMapper<QuestionInfo> {
    QuestionInfo selectQuestionInfoByIds(@Param("questionId") Integer questionId, @Param("examinationPaperId")Integer examinationPaperId);
}

