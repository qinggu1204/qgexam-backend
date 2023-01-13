package com.qgexam.exam.viewresults.pojo.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.qgexam.exam.viewresults.pojo.VO.OptionResultVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 试卷信息表(ExaminationPaper)表实体类
 *
 * @author ljy
 * @since 2022-12-21 19:39:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamScoreDetail {
    // 试卷总分
    private Integer totalScore;
    // 考生答卷总分
    private Integer stuTotalScore;
    // 题目信息
    private List<QuestionInfo> questionInfoList;
}
