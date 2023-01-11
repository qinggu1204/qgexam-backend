package com.qgexam.quartz.utils;




import cn.hutool.core.date.DateUtil;
import com.qgexam.quartz.constants.ScheduleConstants;

import com.qgexam.quartz.pojo.PO.SysJob;
import org.quartz.*;

import java.util.Date;

/**
 * 定时任务工具类
 *
 * @author ruoyi
 */
public class ScheduleUtils {
    /**
     * 得到quartz任务类
     *
     * @param sysJob 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(SysJob sysJob) {
        boolean isConcurrent = "0".equals(sysJob.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(String jobName, String jobGroup) {
        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(String jobName, String jobGroup) {
        return JobKey.jobKey(jobName, jobGroup);
    }

    /**
     * 创建定时任务
     *
     */
    public static void createScheduleJob(Scheduler scheduler, SysJob job ) throws SchedulerException {
        Class<? extends Job> jobClass = getQuartzJobClass(job);
        // 构建job信息
        String jobName = job.getJobName();
        String jobGroup = job.getJobGroup();

        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobName, jobGroup)).build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobName, jobGroup))
                .withSchedule(cronScheduleBuilder).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, job);

        // 判断是否存在
        if (scheduler.checkExists(getJobKey(jobName, jobGroup))) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(getJobKey(jobName, jobGroup));
        }

        Date date = scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("**********下一次执行时间为: " + " " + DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));

        if(job.getJobStatus() == null) {
            return;
        }
        // 暂停任务
        if (job.getJobStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobName, jobGroup));
        }
    }

    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob job, CronScheduleBuilder cb) {
        switch (job.getMisfirePolicy()) {
            case ScheduleConstants.MISFIRE_DEFAULT:
                return cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new RuntimeException("The task misfire policy '" + job.getMisfirePolicy()
                        + "' cannot be used in cron schedule tasks");
        }
    }


}