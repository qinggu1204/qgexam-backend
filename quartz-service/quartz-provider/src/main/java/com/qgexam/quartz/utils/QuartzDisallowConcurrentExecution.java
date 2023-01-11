package com.qgexam.quartz.utils;



import com.qgexam.quartz.pojo.PO.SysJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 *
 * @author ruoyi
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysJob job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
