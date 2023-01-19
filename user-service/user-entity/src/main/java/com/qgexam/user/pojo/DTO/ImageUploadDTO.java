package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月19日 15:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadDTO implements Serializable {
    private String originalFilename;
}
