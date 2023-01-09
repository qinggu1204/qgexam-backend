package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yzw
 * @date 2023年01月09日 15:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationInfoVO {
    private String examinationName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
