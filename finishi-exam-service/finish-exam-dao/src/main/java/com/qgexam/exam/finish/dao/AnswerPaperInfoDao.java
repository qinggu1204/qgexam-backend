package com.qgexam.exam.finish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 答卷表(AnswerPaperInfo)表数据库访问层
 *
 * @author tageshi
 * @since 2023-01-09 19:19:33
 */
public interface AnswerPaperInfoDao extends BaseMapper<PO.AnswerPaperInfo> {
    public Integer getAnswerPaperId(@Param("studentId")Integer studentId, @Param("examinationId")Integer examinationId);
}

