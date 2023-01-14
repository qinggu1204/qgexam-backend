package com.qgexam.user.constants;
/**
 * @author ljy
 * @date 2023年01月08日 23:40
 */
public class ExamUnderwayJobConstants {
    public static final String JOB_NAME = "examUnderwayJob";
    public static final String JOB_GROUP = "examUnderwayJobGroup";

    private static String invoke_target = "examUnderwayJob.execute(%d)";

    public static String getInvokeTarget(Integer examinationId) {
        return String.format(invoke_target, examinationId);
    }
}
