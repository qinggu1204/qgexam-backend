package com.qgexam.user.pojo.PO;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 用户信息表(UserInfo)表实体类
 *
 * @author lamb007
 * @since 2022-12-10 20:10:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {
    //用户编号
    private Integer userId;
    //登录名
    private String loginName;
    //密码
    private String password;
    //姓名(用户名)
    private String userName;
    //头像url
    private String headImg;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

