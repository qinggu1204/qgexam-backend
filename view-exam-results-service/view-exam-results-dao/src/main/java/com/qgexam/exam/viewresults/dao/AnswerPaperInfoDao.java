package com.qgexam.exam.viewresults.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.viewresults.pojo.PO.AnswerPaperInfo;
import com.qgexam.user.pojo.PO.ExaminationPaper;
import org.apache.ibatis.annotations.Param;

/**
 * 答卷表(AnswerPaperInfo)表数据库访问层
 *
 * @author ljy
 * @since 2022-12-17 21:35:11
 */
public interface AnswerPaperInfoDao extends BaseMapper<AnswerPaperInfo> {
    AnswerPaperInfo selectStuAnswerPaper(@Param("examinationId")Integer examinationId, @Param("studentId")Integer studentId);
}

