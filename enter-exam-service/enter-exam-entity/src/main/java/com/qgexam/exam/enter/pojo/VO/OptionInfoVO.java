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
public class OptionInfoVO implements Serializable {
    private String label;
    private String value;
}
