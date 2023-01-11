package com.qgexam.marking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.marking.pojo.DTO.MarkingDTO;
import com.qgexam.marking.pojo.PO.AnswerPaperDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 答卷明细表(AnswerPaperDetail)表数据库访问层
 *
 * @author peter guo
 * @since 2022-12-30 20:53:59
 */
public interface AnswerPaperDetailDao extends BaseMapper<AnswerPaperDetail> {

    void updateScoreBatch(@Param("answerPaperId") Integer answerPaperId,@Param("questionList") List<MarkingDTO> questionList);
}

