package com.qgexam.user.constants;

/**
 * @author yzw
 * @date 2023年01月08日 23:40
 */
public class ExamBeginJobConstants {
    public static final String JOB_NAME = "examBeginJob";
    public static final String JOB_GROUP = "examBeginJobGroup";

    private static String invoke_target = "examBeginJob.execute(%d)";

    public static final Long TIME_BEGIN_EXAM = -2L;

    public static String getInvokeTarget(Integer examinationId) {
        return String.format(invoke_target, examinationId);
    }


}
