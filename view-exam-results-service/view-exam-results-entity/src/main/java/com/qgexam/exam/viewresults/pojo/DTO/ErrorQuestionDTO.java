package com.qgexam.exam.viewresults.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ljy
 * @date 2022/1/14 15:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorQuestionDTO implements Serializable {
    @NotNull(message = "考试编号不能为空")
    private Integer examinationId;
    @NotNull(message = "题目编号不能为空")
    private Integer questionId;
    @NotNull(message = "学生编号不能为空")
    private Integer studentId;
}
