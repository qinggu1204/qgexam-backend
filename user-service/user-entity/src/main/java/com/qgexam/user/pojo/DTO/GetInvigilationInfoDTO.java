package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @description
 * @date 2023/1/6 10:44:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInvigilationInfoDTO implements Serializable {
    private Integer examinationId;
    private Integer currentPage;
    private Integer pageSize;
}
