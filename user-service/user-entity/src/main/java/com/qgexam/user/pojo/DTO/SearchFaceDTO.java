package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description 识别人脸信息DTO
 * @author peter guo
 * @date 2023/01/06 20:33:44
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFaceDTO implements Serializable {
    @NotBlank(message = "人脸特征不能为空")
    private String file;
}
