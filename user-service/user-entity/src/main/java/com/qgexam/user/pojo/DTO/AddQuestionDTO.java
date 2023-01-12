package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author yzw
 * @date 2023年01月11日 14:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddQuestionDTO implements Serializable {
    @NotBlank(message = "题目类型不能为空")
    private String type;

    @NotBlank(message = "题目内容不能为空")
    private String description;

    @NotNull(message = "题目学科id不能为空")
    private Integer subjectId;

    @NotBlank(message = "题目学科名称不能为空")
    private String subjectName;

    @NotNull(message = "题目章节id不能为空")
    private Integer chapterId;

    @NotBlank(message = "题目章节名称不能为空")
    private String chapterName;

    @NotNull(message = "题目难度不能为空")
    private Integer difficultyLevel;

    @NotBlank(message = "题目答案不能为空")
    private String questionAns;

    @Valid
    List<SubQuestionInfoDTO> subQuestionInfo;

    @Valid
    List<OptionInfoDTO> optionInfo;
}
