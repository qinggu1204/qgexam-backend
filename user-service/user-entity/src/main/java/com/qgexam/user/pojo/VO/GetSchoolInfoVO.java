package com.qgexam.user.pojo.VO;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yzw
 * @date 2023年01月11日 10:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSchoolInfoVO {
    private Integer schoolId;
    //学校名称
    private String schoolName;

}
