package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author tageshi
 * @date 2023/1/8 22:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfoVO implements Serializable {
    String examinationName;
    LocalDateTime startTime;
    LocalDateTime endTime;
}
