package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月15日 23:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviewOptionInfoVO implements Serializable {
    private Integer optionId;
    private String optionName;
    private String optionDesc;
}
