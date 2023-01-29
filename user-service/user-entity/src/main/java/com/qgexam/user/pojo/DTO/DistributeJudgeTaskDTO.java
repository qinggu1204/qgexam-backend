package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description 创建试卷DTO
 * @author peter guo
 * @date 2022/12/21 19:32:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributeJudgeTaskDTO implements Serializable {
    @NotNull(message = "考试编号不能为空")
    private Integer examinationId;
    @NotNull(message = "阅卷截止时间不能为空")
    private LocalDateTime endTime;
}
