package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceSearchResVO implements Serializable {
    private String userName;
    private Integer similarValue;
    private Integer age;
    private String gender;
    private String image;
}
