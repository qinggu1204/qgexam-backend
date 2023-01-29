package com.qgexam.marking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.marking.pojo.PO.ExaminationInfo;
import com.qgexam.marking.pojo.VO.GetAnswerPaperVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试信息表(ExaminationInfo)表数据库访问层
 *
 * @author peter guo
 * @since 2022-12-28 23:47:50
 */
public interface ExaminationInfoDao extends BaseMapper<ExaminationInfo> {

    List<GetAnswerPaperVO> selectQuestionList(@Param("subQuestionIdList") List<Integer> subQuestionIdList,
                                              @Param("examinationPaperId") Integer examinationPaperId);

    List<Integer> selectSubQuestionIdList(Integer examinationPaperId);

    List<Integer> selectCourseIdList(Integer examinationId);

    Integer updateCourseScore(@Param("studentId") Integer studentId,
                              @Param("courseIdList") List<Integer> courseIdList,
                              @Param("totalScore") Integer totalScore);
}

