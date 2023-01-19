package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yzw
 * @date 2023年01月19日 18:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationVO implements Serializable {
    private Integer examinationId;
    private String examinationName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
}
