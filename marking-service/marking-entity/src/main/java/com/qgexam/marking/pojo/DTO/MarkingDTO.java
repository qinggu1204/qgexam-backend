package com.qgexam.marking.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 获取答卷视图对象
 * @author peter guo
 * @since 2023-01-02 15:37:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkingDTO implements Serializable {
    //题目编号
    @NotNull(message = "题目编号不能为空")
    private Integer questionId;
    //得分(默认0)
    @NotNull(message = "得分不能为空")
    private Integer score;
}
