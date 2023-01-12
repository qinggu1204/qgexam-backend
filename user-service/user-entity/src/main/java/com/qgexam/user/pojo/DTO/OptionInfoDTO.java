package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月11日 20:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionInfoDTO implements Serializable {
    @NotBlank(message = "选项名称不能为空")
    private String optionName;
    @NotBlank(message = "选项内容不能为空")
    private String optionDesc;
}
