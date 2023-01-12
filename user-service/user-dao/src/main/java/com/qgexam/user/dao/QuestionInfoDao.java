package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.QuestionInfo;

import java.util.List;

/**
 * 题目信息表(QuestionInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-09 11:59:42
 */
public interface QuestionInfoDao extends BaseMapper<QuestionInfo> {
    Integer insertQuestionInfo(QuestionInfo questionInfo);
}

