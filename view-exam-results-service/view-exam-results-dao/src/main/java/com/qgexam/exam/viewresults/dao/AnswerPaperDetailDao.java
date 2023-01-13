package com.qgexam.exam.viewresults.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.viewresults.pojo.PO.AnswerPaperDetail;
import com.qgexam.exam.viewresults.pojo.PO.AnswerPaperInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 答卷明细表(AnswerPaperDetail)表数据库访问层
 *
 * @author ljy
 * @since 2022-12-17 21:34:50
 */
public interface AnswerPaperDetailDao extends BaseMapper<AnswerPaperDetail> {
    AnswerPaperDetail selectStuAnswerDetail(@Param("answerPaperId")Integer answerPaperId, @Param("questionId")Integer questionId);
}

