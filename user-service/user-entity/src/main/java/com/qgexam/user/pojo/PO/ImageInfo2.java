package com.qgexam.user.pojo.PO;

import com.arcsoft.face.enums.ImageFormat;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImageInfo2 implements Serializable {
    private byte[] imageData;
    private Integer width;
    private Integer height;
    private ImageFormat imageFormat;

}