package com.qgexam.exam.viewresults.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 选项返回信息
 *
 * @author ljy
 * @since 2022-1-6 19:46:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubQuestionResultVO implements Serializable {
    private Integer subQuestionId;
    //小题题干
    private String subQuestionDesc;
    //学生小题答案
    private String subQuestionAnswer;
    //小题答案
    private String subQuestionAns;
}
