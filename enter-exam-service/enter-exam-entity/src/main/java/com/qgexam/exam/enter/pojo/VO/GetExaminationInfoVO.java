package com.qgexam.exam.enter.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yzw
 * @date 2023年01月11日 10:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetExaminationInfoVO implements Serializable {
    private Integer examinationId;
    private String examinationName;
    private LocalDateTime startTime;
    private Integer status;
    private LocalDateTime endTime;
}
