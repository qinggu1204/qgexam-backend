package com.qgexam.marking.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description 获取答卷列表视图对象
 * @author peter guo
 * @since 2022-12-30 12:02:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerPaperVO implements Serializable {
    private Integer answerPaperId;
    private Integer isMarked;
    private LocalDateTime updateTime;
}
