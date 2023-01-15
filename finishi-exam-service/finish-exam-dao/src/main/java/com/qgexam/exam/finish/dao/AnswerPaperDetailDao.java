package com.qgexam.exam.finish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 答卷明细表(AnswerPaperDetail)表数据库访问层
 *
 * @author tageshi
 * @since 2023-01-12 16:56:56
 */
public interface AnswerPaperDetailDao extends BaseMapper<PO.AnswerPaperDetail> {
    public Integer insertAnswerPaperDetail(@Param("answerPaperId")Integer answerPaperId,
                                        @Param("questionId")Integer questionId,
                                        @Param("studentAnswer")String studentAnswer,
                                        @Param("score")Integer score);
    public Integer getAnswerPaperDetailId(@Param("answerPaperId")Integer answerPaperId,@Param("questionId") Integer questionId);
}

