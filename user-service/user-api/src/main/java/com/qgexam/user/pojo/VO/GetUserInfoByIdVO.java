package com.qgexam.user.pojo.VO;

import com.qgexam.user.pojo.PO.RoleInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据id查询用户信息接口返回信息
 *
 * @author peter guo
 * @since 2022-12-14 16:15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoByIdVO implements Serializable {
    private Integer userId;
    //姓名(用户名)
    private String userName;
    //头像url
    private String headImg;
    // 角色列表
    private List<RoleInfo> roleList;
}
