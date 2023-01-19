package com.qgexam.marking.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description 获取答卷视图对象
 * @author peter guo
 * @since 2022-12-30 12:02:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAnswerPaperVO implements Serializable {
    //是否有小题
    private Integer hasSubQuestion;
    // 题目编号
    private Integer questionId;
    //题目描述
    private String description;
    //题目分数
    private Double questionScore;
    //学生答案
    private String studentAnswer;
    //参考答案
    private String questionAns;
    //小题列表
    private List<SubQuestionVO> subQuestionInfo;
    //教师打分
    private Integer score;
}
