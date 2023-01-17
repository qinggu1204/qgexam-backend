package com.qgexam.exam.finish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.finish.pojo.PO.AnswerPaperInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 答卷表(AnswerPaperInfo)表数据库访问层
 *
 * @author tageshi
 * @since 2023-01-09 19:19:33
 */
public interface AnswerPaperInfoDao extends BaseMapper<AnswerPaperInfo> {
    public Integer getAnswerPaperId(@Param("studentId")Integer studentId, @Param("examinationId")Integer examinationId);
    public Integer insertObjectiveScore(@Param("answerPaperId")Integer answerPaperId,@Param("objectiveScore")Integer objectiveScore);
}

