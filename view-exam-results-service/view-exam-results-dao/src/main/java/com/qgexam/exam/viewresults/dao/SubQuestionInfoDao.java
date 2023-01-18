package com.qgexam.exam.viewresults.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.viewresults.pojo.PO.SubQuestionInfo;
import com.qgexam.exam.viewresults.pojo.VO.SubQuestionResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 小题信息表(SubQuestionInfo)表数据库访问层
 *
 * @author ljy
 * @since 2023-01-10 13:01:38
 */
public interface SubQuestionInfoDao extends BaseMapper<SubQuestionInfo> {
    List<SubQuestionResultVO> selectSubQuestionInfoList(@Param("answerPaperDetailId")Integer answerPaperDetailId, @Param("questionId")Integer questionId);
    List<SubQuestionResultVO> selectSubQuestionInfoListByQuestionInfoId(@Param("questionId")Integer questionId);
}

