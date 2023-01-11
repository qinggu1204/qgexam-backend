package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 题目详情DTO
 * @author peter guo
 * @date 2022/12/22 00:05:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO implements Serializable {
    // 题目类型
    @NotBlank(message = "题目类型不能为空")
    private String questionType;
    // 题目数量
    @NotNull(message = "题目数量不能为空")
    private Integer questionNumber;
    // 题目总分
    @NotNull(message = "题目总分不能为空")
    private Integer questionTotalScore;
    // 题目难度
    @NotNull(message = "题目难度不能为空")
    @Min(value = 1,message = "题目难度在1-10之间")
    @Max(value = 10,message = "题目难度在1-10之间")
    private Integer difficultyLevel;
}
