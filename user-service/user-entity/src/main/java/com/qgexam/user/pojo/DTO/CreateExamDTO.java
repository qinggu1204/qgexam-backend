package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ljy
 * @description 接收创建考试参数
 * @date 2022/12/2 20:50:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExamDTO implements Serializable {
    @NotNull(message = "试卷编号不能为空")
    private Integer examinationPaperId;

    @NotBlank(message = "考试名称不能为空")
    private String examinationName;

    @NotNull(message = "考试开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "考试结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull(message = "限时时间不能为空")
    private Integer limitTime;

    @NotNull(message = "是否题目乱序不能为空")
    private Integer isQuestionResort;

    @NotNull(message = "是否选项乱序不能为空")
    private Integer isOptionResort;

    @NotNull(message = "学科编号不能为空")
    private Integer subjectId;
}
