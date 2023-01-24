package com.qgexam.exam.enter.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.qgexam.exam.enter.pojo.PO.OptionInfo;
import com.qgexam.exam.enter.pojo.VO.OptionInfoVO;


import java.util.List;

/**
 * 选项表(OptionInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-09 12:30:43
 */
public interface OptionInfoDao extends BaseMapper<OptionInfo> {
    List<OptionInfoVO> selectOptionInfoListByQuestionInfoId(Integer questionInfoId);
}

