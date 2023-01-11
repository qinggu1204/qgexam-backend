package com.qgexam.quartz.pojo.VO;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author yzw
 * @date 2023年01月09日 15:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationInfoVO implements Serializable {
    private Integer examinationId;
    private String examinationName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Integer limitTime;
    //是否题目乱序(0表示否，1表示是)
    private Integer isQuestionResort;
    //是否选项乱序(0表示否，1表示是)
    private Integer isOptionResort;

}
