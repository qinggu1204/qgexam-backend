package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 考试过程中验证人脸信息DTO
 * @author peter guo
 * @date 2023/01/06 20:33:44
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamFaceComparisonDTO implements Serializable {
    @NotNull(message = "考试id不能为空")
    private Integer examinationId;
    @NotBlank(message = "人脸特征不能为空")
    private String file;
}
