package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月19日 18:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetExamListDTO implements Serializable {
    private Integer courseId;
    private Integer currentPage;
    private Integer pageSize;
}
