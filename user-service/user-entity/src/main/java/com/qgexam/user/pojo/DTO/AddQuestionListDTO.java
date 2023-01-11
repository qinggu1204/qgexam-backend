package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author yzw
 * @date 2023年01月11日 14:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddQuestionListDTO implements Serializable {
    @NotNull(message = "需要传递题目")
    @NotEmpty(message = "需要传递题目")
    @Valid
    private List<AddQuestionDTO> question;
}
