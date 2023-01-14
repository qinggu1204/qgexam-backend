package com.qgexam.exam.enter.pojo.VO;

import com.qgexam.user.pojo.VO.QuestionInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author yzw
 * @date 2023年01月09日 20:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleVO implements Serializable {
    private String type;
    private List<QuestionInfoVO> singleList;
}
