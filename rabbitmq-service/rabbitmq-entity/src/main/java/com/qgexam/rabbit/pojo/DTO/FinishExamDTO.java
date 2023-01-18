package com.qgexam.rabbit.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author tageshi
 * @date 2023/1/14 14:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinishExamDTO implements Serializable {
    private Integer examinationId;
    private Integer studentId;
    private LocalDateTime submitTime;
}
