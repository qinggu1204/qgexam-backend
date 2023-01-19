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
public class QuestionAnsVO implements Serializable {
    private Integer questionId;
    private String description;
    //是否有小题(0没有小题 1有小题)
    private Integer hasSubQuestion;
    private String questionAns;
    private List<SubQuestionVO> subQuestionAnsList;
}
