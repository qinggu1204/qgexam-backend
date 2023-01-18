package com.qgexam.user.constants;
/**
 * @author ljy
 * @date 2023年01月08日 23:40
 */
public class ExamFinishJobConstants {
    public static final String JOB_NAME = "examFinishJob";
    public static final String JOB_GROUP = "examFinishJob";

    private static String invoke_target = "examFinishJob.execute(%d)";

    public static String getInvokeTarget(Integer examinationId) {
        return String.format(invoke_target, examinationId);
    }
}
