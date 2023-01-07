package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ljy
 * @date 2022/1/6 15:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNeteacherDTO implements Serializable {
    @NotNull(message = "用户编号不能为空")
    private Integer userId;
}
