package com.qgexam.exam.finish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.finish.pojo.DTO.SubQuestionDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 小题答案明细表(SubQuestionAnswerDetail)表数据库访问层
 *
 * @author tageshi
 * @since 2023-01-13 17:50:58
 */
public interface SubQuestionAnswerDetailDao extends BaseMapper<PO.SubQuestionAnswerDetail> {
    public Integer insert(@Param("answerPaperDetailId")Integer answerPaperDetailId,
                          @Param("subQuestionId")Integer subQuestionId,
                          @Param("subQuestionAnswer")String subQuestionAnswer);
}

