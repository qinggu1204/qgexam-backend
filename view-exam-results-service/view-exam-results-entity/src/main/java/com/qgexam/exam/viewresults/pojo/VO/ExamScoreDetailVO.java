package com.qgexam.exam.viewresults.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 查询考试成绩明细接口返回信息
 *
 * @author ljy
 * @since 2022-1-6 19:46:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamScoreDetailVO implements Serializable {
    // 试卷总分
    private Integer totalScore;
    // 考生答卷总分
    private Integer stuTotalScore;
    // 单选题
    private SingleVO single;
    // 多选题
    private MultiVO multi;
    // 判断题
    private JudgeVO judge;
    // 填空题
    private CompletionVO completion;
    // 综合题
    private ComplexVO complex;
}
