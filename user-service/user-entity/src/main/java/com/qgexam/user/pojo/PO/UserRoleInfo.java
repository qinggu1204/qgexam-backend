package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 用户角色关联表(UserRoleInfo)表实体类
 *
 * @author tageshi
 * @since 2022-12-16 18:37:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleInfo implements Serializable {

    @TableId
    private Integer id;
    //角色编号
    private Integer roleId;
    //角色名称(冗余)
    private String roleName;
    //用户编号
    private Integer userId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

