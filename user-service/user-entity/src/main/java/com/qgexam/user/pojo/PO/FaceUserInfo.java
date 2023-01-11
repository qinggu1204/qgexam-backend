package com.qgexam.user.pojo.PO;


import lombok.Data;

import java.io.Serializable;

@Data
public class FaceUserInfo implements Serializable {

    private Integer id;
    private String name;
    private Integer similarValue;
    private byte[] faceFeature;

}
