package com.qgexam.user.constants;

/**
 * @author peter guo
 * @date 2023-01-14 19:53:38
 */
public class BeginCacheJobConstants {
    /**
     * 任务名称
     */
    public static String BEGIN_CACHE_JOB_NAME = "beginCacheJob";

    /**
     * 触发器名
     */
    public static String BEGIN_CACHE_TRIGGER_NAME = "beginCacheTrigger";

    /**
     * 任务组名
     */
    public static String BEGIN_CACHE_JOB_GROUP_NAME = "beginCacheJobGroup";

    /**
     * 触发器组名
     */
    public static String BEGIN_CACHE_TRIGGER_GROUP_NAME = "beginCacheTriggerGroup";

    private static String invoke_target = "beginCacheJob.execute(%d)";

    public static String getInvokeTarget(Integer examinationId) {
        return String.format(invoke_target, examinationId);
    }
}

