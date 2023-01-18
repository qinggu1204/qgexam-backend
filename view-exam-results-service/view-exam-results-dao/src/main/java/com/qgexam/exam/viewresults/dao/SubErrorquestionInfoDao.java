package com.qgexam.exam.viewresults.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.viewresults.pojo.PO.SubErrorquestionInfo;
import com.qgexam.exam.viewresults.pojo.VO.SubQuestionResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 错题小题信息表(SubErrorquestionInfo)表数据库访问层
 *
 * @author ljy
 * @since 2023-01-15 11:55:48
 */
public interface SubErrorquestionInfoDao extends BaseMapper<SubErrorquestionInfo> {
    List<SubQuestionResultVO> selectSubQuestionInfoList(@Param("questionId")Integer questionId, @Param("studentId")Integer studentId);
}

