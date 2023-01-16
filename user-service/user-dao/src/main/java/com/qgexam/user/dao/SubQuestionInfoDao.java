package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.SubQuestionInfo;
import com.qgexam.user.pojo.VO.PreviewSubQuestionInfoVO;
import com.qgexam.user.pojo.VO.SubQuestionInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 小题信息表(SubQuestionInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-09 12:30:24
 */
public interface SubQuestionInfoDao extends BaseMapper<SubQuestionInfo> {
    List<SubQuestionInfoVO> selectSubQuestionInfoListByQuestionInfoId(Integer questionInfoId);
    List<PreviewSubQuestionInfoVO> selectPreviewSubQuestionInfoListByQuestionInfoId(Integer questionInfoId);

    Integer insertSubQuestionInfoBatch(@Param("subQuestionInfoList") List<SubQuestionInfo> subQuestionInfoList);
}

