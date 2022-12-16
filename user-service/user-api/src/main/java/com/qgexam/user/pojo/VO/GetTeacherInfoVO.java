package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tageshi
 * @date 2022/12/16 16:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTeacherInfoVO {
    private String teacherNumber;

    private String loginName;

    private String userName;
    private String schoolName;
    private String headImg;

}
