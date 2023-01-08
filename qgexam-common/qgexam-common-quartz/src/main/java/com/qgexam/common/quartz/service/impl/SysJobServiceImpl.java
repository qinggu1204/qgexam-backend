package com.qgexam.common.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.qgexam.common.quartz.constants.ScheduleConstants;
import com.qgexam.common.quartz.dao.SysJobDao;
import com.qgexam.common.quartz.pojo.SysJob;
import com.qgexam.common.quartz.service.SysJobService;
import com.qgexam.common.quartz.utils.ScheduleUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务调度表(SysJob)表服务实现类
 *
 * @author lamb007
 * @since 2023-01-07 20:37:56
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobDao, SysJob> implements SysJobService {
    @Autowired
    private SysJobDao sysJobDao;
    @Autowired
    private Scheduler scheduler;

    /**
     * 运行一次任务 立即执行
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void run(Integer jobId) throws SchedulerException {
        SysJob tmpObj = getById(jobId);
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, tmpObj);
        scheduler.triggerJob(ScheduleUtils.getJobKey(tmpObj.getJobName(), tmpObj.getJobGroup()), dataMap);
    }

    /**
     * 删除job
     *
     * @param id
     * @throws SchedulerException
     */
    public void deleteJobById(Integer id) throws SchedulerException {
        SysJob job = getById(id);
        deleteJob(job);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @throws SchedulerException
     */
    public void deleteJobByIds(List<Integer> ids) throws SchedulerException {
        for (Integer jobId : ids) {
            SysJob job = getById(jobId);
            deleteJob(job);
        }
    }

    private void deleteJob(SysJob job) throws SchedulerException {
        Integer jobId = job.getId();
        String jobGroup = job.getJobGroup();
        if (removeById(jobId)) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(job.getJobName(), jobGroup));
        }
    }


    /**
     * 切换状态
     *
     * @param job
     * @return
     */
    public Boolean changeStatus(SysJob job) throws SchedulerException {
        if (ScheduleConstants.Status.NORMAL.getValue().equals(job.getJobStatus())) {
            resumeJob(job);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(job.getJobStatus())) {
            pauseJob(job);
        }
        return true;
    }

    /**
     * 恢复任务
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    private void resumeJob(SysJob job) throws SchedulerException {

        String jobGroup = job.getJobGroup();
        // 查询Job
        SysJob retJob = sysJobDao.getJobByNameAndGroup(job);
        if (retJob == null) {
            return;
        }

        retJob.setJobStatus(ScheduleConstants.Status.NORMAL.getValue());
        if (updateById(retJob)) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(job.getJobName(), jobGroup));
        }
    }


    /**
     * 暂停任务
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    private void pauseJob(SysJob job) throws SchedulerException {
        String jobGroup = job.getJobGroup();
        // 查询Job
        SysJob retJob = sysJobDao.getJobByNameAndGroup(job);
        if (retJob == null) {
            return;
        }
        retJob.setJobStatus(ScheduleConstants.Status.PAUSE.getValue());
        if (updateById(retJob)) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(job.getJobName(), jobGroup));
        }
    }

    /**
     * 添加job
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    public Boolean saveJob(SysJob job) throws SchedulerException {
        SysJob sysJob = sysJobDao.getJobByNameAndGroup(job);
        if (sysJob != null) {
            return false;
        }
        if (save(job)) {
            SysJob retJob = sysJobDao.getJobByNameAndGroup(job);
            ScheduleUtils.createScheduleJob(scheduler, retJob);
        }
        return true;
    }

    /**
     * 修改job
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    public Boolean updateJob(SysJob job) throws SchedulerException {
        SysJob sysJob = sysJobDao.getJobByNameAndGroup(job);
        if (sysJob == null) {
            return false;
        }
        job.setId(sysJob.getId());
        if (updateById(job)) {
            SysJob retJob = sysJobDao.getJobByNameAndGroup(job);
            updateSchedulerJob(retJob, retJob.getJobGroup());
        }
        return null;
    }

    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    private void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException {
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(job.getJobName(), jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }
}

