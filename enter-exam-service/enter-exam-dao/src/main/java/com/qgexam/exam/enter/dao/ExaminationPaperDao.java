package com.qgexam.exam.enter.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.enter.pojo.PO.ExaminationPaper;


/**
 * 试卷信息表(ExaminationPaper)表数据库访问层
 *
 * @author peter guo
 * @since 2022-12-23 11:30:12
 */
public interface ExaminationPaperDao extends BaseMapper<ExaminationPaper> {
    ExaminationPaper selectExaminationPaperById(Integer examinationPaperId);


}

