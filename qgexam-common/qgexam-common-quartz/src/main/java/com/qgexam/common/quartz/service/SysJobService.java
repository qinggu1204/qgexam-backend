package com.qgexam.common.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.qgexam.common.quartz.pojo.SysJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务调度表(SysJob)表服务接口
 *
 * @author lamb007
 * @since 2023-01-07 20:37:23
 */
public interface SysJobService extends IService<SysJob> {
    void run(Integer jobId) throws SchedulerException;
    void deleteJobById(Integer id) throws SchedulerException;
    void deleteJobByIds(List<Integer> ids) throws SchedulerException;
    Boolean changeStatus(SysJob job) throws SchedulerException;
    Boolean saveJob(SysJob job) throws SchedulerException;
    Boolean updateJob(SysJob job) throws SchedulerException;
}

