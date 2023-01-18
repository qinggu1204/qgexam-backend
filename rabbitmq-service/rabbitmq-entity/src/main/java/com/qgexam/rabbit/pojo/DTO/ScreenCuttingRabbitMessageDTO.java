package com.qgexam.rabbit.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月13日 10:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenCuttingRabbitMessageDTO implements Serializable {
    private Integer examinationId;
    private Integer studentId;
}
