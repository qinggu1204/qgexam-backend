package com.qgexam.common.core.constants;

public class ExamConstants {
    /**
     * 考试未开始
     */
    public static final Integer EXAM_STATUS_NOT_START = 2;

    /**
     * 考试进行中
     */
    public static final Integer EXAM_STATUS_UNDERWAY = 1;

    /**
     * 考试已结束
     */
    public static final Integer EXAM_STATUS_OVER = 3;

    /**
     * 考试未开始
     */
    public static final String EXAM_STATUS_NOT_START_VO = "未开始";

    /**
     * 考试进行中
     */
    public static final String EXAM_STATUS_UNDERWAY_VO = "进行中";

    /**
     * 考试已结束
     */
    public static final String EXAM_STATUS_OVER_VO = "已结束";

    /**
     * 题目类型：单选题
     */
    public static final String QUESTION_TYPE_SINGLE = "SINGLE";

    /**
     * 题目类型：多选题
     */
    public static final String QUESTION_TYPE_MULTI = "MULTI";

    /**
     * 题目类型：填空题
     */
    public static final String QUESTION_TYPE_COMPLETION = "COMPLETION";

    /**
     * 题目类型：综合题
     */
    public static final String QUESTION_TYPE_COMPLEX = "COMPLEX";


    /**
     * 题目类型：判断题
     */
    public static final String QUESTION_TYPE_JUDGE = "JUDGE";


    public static final Integer QUESTION_RESORT = 1;
    public static final Integer QUESTION_NOT_RESORT = 0;

    public static final Integer OPTION_RESORT = 1;
    public static final Integer OPTION_NOT_RESORT = 0;


    /**
     *
     */
    public static final String EXAMINATION_SINGLE_QUESTION_HASH_FIELD = "before:examination:question:single:";
    public static final String EXAMINATION_MULTI_QUESTION_HASH_FIELD = "before:examination:question:multi:";
    public static final String EXAMINATION_JUDGE_QUESTION_HASH_FIELD = "before:examination:question:judge:";
    public static final String EXAMINATION_COMPLETION_QUESTION_HASH_FIELD = "before:examination:question:completion:";
    public static final String EXAMINATION_COMPLEX_QUESTION_HASH_FIELD = "before:examination:question:complex:";

    public static final String EXAMINATION_INFO_HASH_KEY_PREFIX = "before:examination:info:";




}
