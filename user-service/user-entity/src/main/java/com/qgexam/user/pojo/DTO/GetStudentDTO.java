package com.qgexam.user.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2023/1/8 14:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStudentDTO implements Serializable {
    Integer schoolId;
    String loginName;
}
