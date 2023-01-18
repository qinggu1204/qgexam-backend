package com.qgexam.rabbit.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamFaceComparisonRabbitDTO implements Serializable {
    private Integer examinationId;
    private Integer studentId;
}
