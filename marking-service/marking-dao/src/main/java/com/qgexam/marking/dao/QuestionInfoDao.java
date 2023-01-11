package com.qgexam.marking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.marking.pojo.PO.QuestionInfo;
import com.qgexam.marking.pojo.VO.QuestionAnsVO;
import com.qgexam.marking.pojo.VO.SubQuestionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目信息表(QuestionInfo)表数据库访问层
 *
 * @author peter guo
 * @since 2022-12-30 21:08:45
 */
public interface QuestionInfoDao extends BaseMapper<QuestionInfo> {

    List<QuestionAnsVO> getHasSubQuestionAnsList(@Param("hasSubQuestionIdList") List<Integer> hasSubQuestionIdList);

    List<SubQuestionVO> getSubQuestionAnsList(Integer questionId);

}

