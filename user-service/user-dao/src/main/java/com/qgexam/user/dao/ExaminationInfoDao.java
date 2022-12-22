package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.ExaminationInfo;

/**
 * 考试信息表(ExaminationInfo)表数据库访问层
 *
 * @author tageshi
 * @since 2022-12-22 22:33:50
 */
public interface ExaminationInfoDao extends BaseMapper<ExaminationInfo> {
    ExaminationInfo getByExaminationId(Integer examinationId);
}

