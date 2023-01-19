package com.qgexam.user.pojo.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime endTime;
    private Integer status;
}
