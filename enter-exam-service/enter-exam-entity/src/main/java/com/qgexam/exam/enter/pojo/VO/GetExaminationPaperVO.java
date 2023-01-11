package com.qgexam.exam.enter.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月09日 19:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetExaminationPaperVO implements Serializable {
    private SingleVO single;
    private MultipleVO multi;
    private JudgeVO judge;
    private CompletionVO completion;
    private ComplexVO complex;
}
