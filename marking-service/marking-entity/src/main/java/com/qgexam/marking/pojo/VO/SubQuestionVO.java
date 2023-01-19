package com.qgexam.marking.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 获取答卷视图对象
 * @author peter guo
 * @since 2022-12-30 18:25:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubQuestionVO implements Serializable {
    //小题编号
    private Integer subQuestionId;
    //小题描述
    private String subQuestionDesc;
    //学生小题答案
    private String subQuestionAnswer;
    //参考答案
    private String subQuestionAns;
}
