package com.qgexam.common.core.constants;

public class ExamConstants {
    /**
     * 考试未开始
     */
    public static final String EXAM_STATUS_NOT_START = "NOT_START";

    /**
     * 考试进行中
     */
    public static final String EXAM_STATUS_UNDERWAY = "UNDERWAY";

    /**
     * 考试已结束
     */
    public static final String EXAM_STATUS_OVER = "OVER";

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
     * 题目难度等级最小值
     */
    public static final Integer DIFFICULTY_LEVEL_MINIMUM = 1;

    /**
     * 题目难度等级最大值
     */
    public static final Integer DIFFICULTY_LEVEL_MAXIMUM = 10;

    /**
     * 储存的答卷的键的前缀
     */
    public static final String ANSWER_PAPER_KEY = "before:marking:answerPaper:";

    /**
     * 答卷id列表
     */
    public static final String ANSWER_PAPER_ID_LIST_KEY = "after:marking:answerPaperIdList";
}
