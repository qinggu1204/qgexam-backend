package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.ExaminationInfo;

import java.util.List;

/**
 * 考试信息表(ExaminationInfo)表数据库访问层
 *
 * @author tageshi
 * @author peter guo
 * @since 2022-12-25 22:52:10
 */
public interface ExaminationInfoDao extends BaseMapper<ExaminationInfo> {
    ExaminationInfo getByExaminationId(Integer examinationId);
    Integer insertExaminationInfo(ExaminationInfo examinationInfo);

    List<Integer> getTeacherIdList(Integer examinationId);
}

