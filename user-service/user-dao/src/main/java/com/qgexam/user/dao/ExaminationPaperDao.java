package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.ExaminationPaper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

/**
 * 试卷信息表(ExaminationPaper)表数据库访问层
 *
 * @author peter guo
 * @since 2022-12-23 11:30:12
 */
public interface ExaminationPaperDao extends BaseMapper<ExaminationPaper> {

    /**
     * @param examinationPaperId
     * @param questionIdList
     * @param questionScore
     * @return Integer
     * @description 向试卷中批量添加试题
     * @auther peter guo
     */
    Integer savePaperQuestionBatch(@Param("examinationPaperId") Integer examinationPaperId,
                                   @Param("questionIdList") List<Integer> questionIdList,
                                   @Param("questionScore") Double questionScore);

    ExaminationPaper selectExaminationPaperById(Integer examinationPaperId);
}

