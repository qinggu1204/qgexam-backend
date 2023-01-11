package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.AnswerPaperInfo;

import java.util.List;

/**
 * 答卷表(AnswerPaperInfo)表数据库访问层
 *
 * @author peter guo
 * @since 2022-12-25 22:18:21
 */
public interface AnswerPaperInfoDao extends BaseMapper<AnswerPaperInfo> {

    List<Integer> getCheatPaperIdList(Integer examinationId);

}

