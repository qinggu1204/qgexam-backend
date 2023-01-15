package com.qgexam.rabbit.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yzw
 * @date 2023年01月10日 11:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRecordDTO implements Serializable {
    private Integer examinationId;
    private Integer studentId;
    private LocalDateTime enterTime;
}
