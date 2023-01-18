package com.qgexam.exam.viewresults.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询错题集接口返回信息
 *
 * @author ljy
 * @since 2022-1-6 19:46:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorQuestionListVO implements Serializable {
    // 单选题
    private ErrorSingleVO single;
    // 多选题
    private ErrorMultiVO multi;
    // 判断题
    private ErrorJudgeVO judge;
    // 填空题
    private ErrorCompletionVO completion;
    // 综合题
    private ErrorComplexVO complex;
}
