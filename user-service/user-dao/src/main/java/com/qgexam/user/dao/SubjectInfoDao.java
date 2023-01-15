package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.SubjectInfo;
import com.qgexam.user.pojo.VO.GetSubjectVO;

import java.util.List;

/**
 * 学科信息表(SubjectInfo)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-11 19:38:22
 */
public interface SubjectInfoDao extends BaseMapper<SubjectInfo> {
    List<GetSubjectVO>getSubjectInfo();
}

