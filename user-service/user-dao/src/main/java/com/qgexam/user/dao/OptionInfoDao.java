package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.OptionInfo;
import com.qgexam.user.pojo.VO.OptionInfoVO;
import com.qgexam.user.pojo.VO.PreviewOptionInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 选项表(OptionInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-09 12:30:43
 */
public interface OptionInfoDao extends BaseMapper<OptionInfo> {
    List<OptionInfoVO> selectOptionInfoListByQuestionInfoId(Integer questionInfoId);
    List<PreviewOptionInfoVO> selectPreviewOptionInfoListByQuestionInfoId(Integer questionInfoId);

    Integer insertOptionInfoBatch(@Param("optionInfoList") List<OptionInfo> optionInfoList);

}

