package com.qgexam.user.pojo.PO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 角色表(RoleInfo)表实体类
 *
 * @author lamb007
 * @since 2022-12-11 21:16:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleInfo implements Serializable {
    //角色编号
    @TableId
    private Integer roleId;
    //角色名称
    private String roleName;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0未删除 1已删除
    private Integer isDeleted;

}

