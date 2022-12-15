package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 根据id查询用户信息接口返回信息
 *
 * @author peter guo
 * @since 2022-12-14 16:15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSchoolInfoVO implements Serializable {
    private Integer schoolId;
    private String schoolName;
}
