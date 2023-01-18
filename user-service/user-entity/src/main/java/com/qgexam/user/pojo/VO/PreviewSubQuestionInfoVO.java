package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月15日 23:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviewSubQuestionInfoVO implements Serializable {
    private Integer subQuestionId;
    private String subQuestionDesc;
    private String subQuestionAns;
}
