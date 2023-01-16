package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author yzw
 * @date 2023年01月16日 0:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviewQuestionInfoVO implements Serializable {
    private Integer questionId;
    private String description;
    private Integer subOrObj;
    private String questionAns;
    List<PreviewOptionInfoVO> optionInfo;
    List<PreviewSubQuestionInfoVO> subQuestionInfo;
}
