package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @description
 * @date 2023/1/6 10:47:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetInvigilationInfoVO implements Serializable {
    private Integer id;
    private String courseName;
    private String teacherName;
}
