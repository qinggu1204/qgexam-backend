package com.qgexam.user.pojo.VO;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询学生信息接口返回信息
 *
 * @author peter guo
 * @since 2022-12-14 16:15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStudentInfoVO implements Serializable {

    //姓名(用户名)
    private String userName;
    //学号
    private String studentNumber;
    //学校名称(冗余)
    private String schoolName;
    //登录名
    private String loginName;
    //头像url
    private String headImg;
    //人脸图片url
    private String faceImg;
}
