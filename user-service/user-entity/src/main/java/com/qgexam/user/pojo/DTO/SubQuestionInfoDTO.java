package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月11日 14:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubQuestionInfoDTO implements Serializable {
    @NotEmpty(message = "子题目内容不能为空")
    private String subQuestionDesc;
    @NotEmpty(message = "子题目答案不能为空")
    private String subQuestionAns;
}
