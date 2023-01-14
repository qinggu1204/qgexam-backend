package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.ExaminationPaper;

/**
 * 试卷信息表(ExaminationPaper)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-09 11:36:35
 */
public interface ExaminationPaperDao extends BaseMapper<ExaminationPaper> {

    ExaminationPaper selectExaminationPaperById(Integer examinationPaperId);
}

