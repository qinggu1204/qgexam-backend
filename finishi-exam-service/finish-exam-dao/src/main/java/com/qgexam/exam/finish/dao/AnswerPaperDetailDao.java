package com.qgexam.exam.finish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.finish.pojo.PO.AnswerPaperDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 答卷明细表(AnswerPaperDetail)表数据库访问层
 *
 * @author tageshi
 * @since 2023-01-12 16:56:56
 */
public interface AnswerPaperDetailDao extends BaseMapper<AnswerPaperDetail> {
    public Integer updateAnswerPaperDetail(@Param("answerPaperId")Integer answerPaperId,
                                           @Param("questionId")Integer questionId,
                                           @Param("studentAnswer")String studentAnswer,
                                           @Param("score")Integer score);
    public Integer insertAnswerPaperDetail(@Param("answerPaperId")Integer answerPaperId,
                                        @Param("questionId")Integer questionId,
                                        @Param("studentAnswer")String studentAnswer,
                                        @Param("score")Integer score);
    public Integer getAnswerPaperDetailId(@Param("answerPaperId")Integer answerPaperId,@Param("questionId") Integer questionId);
}

