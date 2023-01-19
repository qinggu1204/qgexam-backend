package com.qgexam.user.pojo.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceUserInfo implements Serializable {

    private Integer id;
    private String userName;
    private Integer similarValue;
    private byte[] faceFeature;

}
