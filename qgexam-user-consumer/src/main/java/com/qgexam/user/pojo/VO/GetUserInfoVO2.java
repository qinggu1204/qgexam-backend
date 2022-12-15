package com.qgexam.user.pojo.VO;

import com.qgexam.user.pojo.PO.RoleInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yzw
 * @description 返回用户个人信息
 * @date 2022/12/14 11:32:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoVO2 {
    private Integer userId;
    //登录名
    private String loginName;
    //姓名(用户名)
    private String userName;
    //头像url
    private String headImg;
    // 角色列表
    private List<RoleInfo> roleList;
}
