package com.qgexam.exam.enter.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qgexam.exam.enter.pojo.DTO.GetExamListDTO;
import com.qgexam.exam.enter.pojo.VO.GetExamListVO;
import com.qgexam.user.pojo.PO.ExaminationInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试信息表(ExaminationInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-06 14:16:57
 */
public interface ExaminationInfoDao extends BaseMapper<ExaminationInfo> {

    IPage<GetExamListVO> selectAllExaminationInfo(IPage<ExaminationInfo> page, @Param("courseIdList") List<Integer> courseIdList);

}

