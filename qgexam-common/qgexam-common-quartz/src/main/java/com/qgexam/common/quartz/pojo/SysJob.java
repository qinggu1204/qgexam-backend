package com.qgexam.common.quartz.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("sys_job")
public class SysJob implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId
    private Integer id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    private String cronExpression;

    /**
     * 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
     */
    private String misfirePolicy;

    /**
     * 是否并发执行（0允许 1禁止）
     */
    private String concurrent;

    /**
     * 状态（0正常 1暂停）
     */
    private String jobStatus;


    public static final String ID = "id";

    public static final String JOB_NAME = "job_name";

    public static final String JOB_GROUP = "job_group";

    public static final String INVOKE_TARGET = "invoke_target";

    public static final String CRON_EXPRESSION = "cron_expression";

    public static final String MISFIRE_POLICY = "misfire_policy";

    public static final String CONCURRENT = "concurrent";

    public static final String JOB_STATUS = "job_status";

}
