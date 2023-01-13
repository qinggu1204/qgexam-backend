package com.qgexam.exam.viewresults.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.exam.viewresults.pojo.PO.OptionInfo;
import com.qgexam.exam.viewresults.pojo.VO.OptionResultVO;

import java.util.List;

/**
 * 选项表(OptionInfo)表数据库访问层
 *
 * @author ljy
 * @since 2023-01-10 12:57:03
 */
public interface OptionInfoDao extends BaseMapper<OptionInfo> {
    List<OptionResultVO> selectOptionInfoListByQuestionInfoId(Integer questionInfoId);
}

