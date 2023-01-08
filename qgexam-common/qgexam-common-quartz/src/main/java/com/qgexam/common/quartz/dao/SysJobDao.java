package com.qgexam.common.quartz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import com.qgexam.common.quartz.pojo.SysJob;
import org.apache.ibatis.annotations.Mapper;


/**
 * 定时任务调度表(SysJob)表数据库访问层
 *
 * @author lamb007
 * @since 2023-01-07 20:33:27
 */
@Mapper
public interface SysJobDao extends BaseMapper<SysJob> {
    SysJob getJobByNameAndGroup(SysJob sysJob);
}

