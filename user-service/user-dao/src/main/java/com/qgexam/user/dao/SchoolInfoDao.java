package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.SchoolInfo;

/**
 * 学校表(SchoolInfo)表数据库访问层
 *
 * @author peter guo
 * @since 2022-12-14 11:12:31
 */
public interface SchoolInfoDao extends BaseMapper<SchoolInfo> {
    SchoolInfo queryById(Integer schoolId);
}

