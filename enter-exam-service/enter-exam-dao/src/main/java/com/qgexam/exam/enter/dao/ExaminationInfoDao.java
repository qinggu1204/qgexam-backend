package com.qgexam.exam.enter.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.exam.enter.pojo.DTO.GetExamListDTO;
import com.qgexam.user.pojo.PO.ExaminationInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 考试信息表(ExaminationInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-06 14:16:57
 */
public interface ExaminationInfoDao extends BaseMapper<ExaminationInfo> {

    IPage<ExaminationInfo> selectAllExaminationInfo(IPage<ExaminationInfo> page, @Param("examListDTO") GetExamListDTO getExamListDTO);

}

