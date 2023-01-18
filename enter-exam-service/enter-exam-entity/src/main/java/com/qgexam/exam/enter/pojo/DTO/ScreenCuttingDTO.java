package com.qgexam.exam.enter.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月13日 10:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenCuttingDTO implements Serializable {
    @NotNull(message = "examinationId不能为空")
    private Integer examinationId;

}
