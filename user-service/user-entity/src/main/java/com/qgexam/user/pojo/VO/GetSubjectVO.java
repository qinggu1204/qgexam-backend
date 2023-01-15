package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2023/1/15 0:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSubjectVO implements Serializable {
    Integer subjectId;
    String subjectName;
}
