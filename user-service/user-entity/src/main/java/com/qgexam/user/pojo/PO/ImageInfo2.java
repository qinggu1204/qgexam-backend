package com.qgexam.user.pojo.PO;

import com.arcsoft.face.enums.ImageFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 人脸照片信息类
 *
 * @author peter guo
 * @since 2022-01-12 15:02:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo2 implements Serializable {
    private byte[] imageData;
    private Integer width;
    private Integer height;
    private ImageFormat imageFormat;

}