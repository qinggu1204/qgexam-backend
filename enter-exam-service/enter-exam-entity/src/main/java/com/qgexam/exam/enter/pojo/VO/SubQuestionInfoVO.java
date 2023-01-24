package com.qgexam.exam.enter.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月09日 20:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubQuestionInfoVO implements Serializable {
    private Integer subQuestionId;
    private String subQuestionDesc;
}
