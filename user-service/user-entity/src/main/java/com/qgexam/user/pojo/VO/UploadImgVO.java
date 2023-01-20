package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月20日 15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImgVO implements Serializable {
    private String url;
    private String name;
}
