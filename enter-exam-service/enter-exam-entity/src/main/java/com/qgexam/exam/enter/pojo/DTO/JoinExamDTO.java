package com.qgexam.exam.enter.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yzw
 * @date 2023年01月13日 16:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinExamDTO implements Serializable {
    private Integer examinationId;
    private LocalDateTime joinTime;
    private Integer studentId;
}
