package com.qgexam.user.pojo.VO;

import lombok.Data;

@Data
public class FaceSearchResVO {
    private String name;
    private Integer similarValue;
    private Integer age;
    private String gender;
    private String image;
}
