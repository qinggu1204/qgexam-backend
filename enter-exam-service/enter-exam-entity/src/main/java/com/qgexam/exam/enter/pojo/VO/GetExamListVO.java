package com.qgexam.exam.enter.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author yzw
 * @date 2023年01月06日 16:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetExamListVO implements Serializable {
    private Integer examinationId;
    //考试名称
    private String examinationName;

    //考试开始时间
    private LocalDateTime startTime;
    //考试结束时间
    private LocalDateTime endTime;
    //考试状态(1表示进行中 2表示未开始 3表示已结束)
    private Integer status;

}
