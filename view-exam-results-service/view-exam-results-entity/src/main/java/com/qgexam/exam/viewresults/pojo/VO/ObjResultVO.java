package com.qgexam.exam.viewresults.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
/**
 * 客观题返回信息
 *
 * @author ljy
 * @since 2022-1-6 19:46:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjResultVO implements Serializable {
    private Integer questionId;
    //题干
    private String description;
    // 题目分数，平均算
    private Integer questionScore;
    // 考生得分
    private Integer score;
    //考生答案
    private String answer;
    //参考答案
    private String questionAns;
    // 题目选项
    private List<OptionResultVO> optionInfo;
}