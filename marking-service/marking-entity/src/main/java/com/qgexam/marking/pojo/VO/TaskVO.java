package com.qgexam.marking.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 任务视图对象
 * @author peter guo
 * @since 2022-12-28 20:31:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskVO implements Serializable {
    private Integer examinationId;
    private String examinationName;
}
