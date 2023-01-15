package com.qgexam.exam.viewresults.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 选项返回信息
 *
 * @author ljy
 * @since 2022-1-6 19:46:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorSubResultVO implements Serializable {
    private Integer questionId;
    //题干
    private String description;
    //章节名称
    private String chapterName;
    //题目难度等级（1-10 越大越难)
    private Integer difficultyLevel;
    // 题目分数，平均算
    private Integer questionScore;
    // 考生得分
    private Integer score;
    //是否有小题
    private boolean hasSubQuestion;
    //考生答案
    private String answer;
    //参考答案
    private String questionAns;
    //题目小题
    private List<SubQuestionResultVO> subQuestionInfo;
}
