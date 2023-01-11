package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @description 创建试卷DTO
 * @author peter guo
 * @date 2022/12/21 19:32:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaperDTO implements Serializable {
    // 试卷标题
    @NotBlank(message = "试卷标题不能为空")
    private String title;
    // 试卷总分
    @NotNull(message = "试卷总分不能为空")
    private Integer totalScore;
    // 学科编号
    @NotNull(message = "学科编号不能为空")
    private Integer subjectId;
    // 章节列表
    @NotEmpty(message = "章节列表不能为空")
    private List<Integer> chapterList;
    // 题目详情
    @NotEmpty(message = "题目详情不能为空")
    private List<QuestionDTO> questionDetail;
}