package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2022/12/22 15:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetExaminationPaperVO implements Serializable {
    //试卷id
    private Integer examinationPaperId;
    //试卷标题
    private String title;
}
