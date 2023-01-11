package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzw
 * @date 2023年01月11日 10:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSchoolListDTO implements Serializable {
    private Integer currentPage;
    private Integer pageSize;
}
