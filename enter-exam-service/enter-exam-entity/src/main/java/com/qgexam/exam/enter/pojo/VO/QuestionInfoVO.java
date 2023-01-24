package com.qgexam.exam.enter.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author yzw
 * @date 2023年01月09日 15:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionInfoVO implements Serializable {

    private Integer questionId;
    //题目类型(SINGLE MULTI JUDGE COMPLETION COMPLEX)
    private String type;
    //题干
    private String description;

    // 题目分数，平均算
    private Integer questionScore;
    // 题目选项
    private List<OptionInfoVO> optionInfo;

    // 题目小题
    private List<SubQuestionInfoVO> subQuestionInfo;


}


