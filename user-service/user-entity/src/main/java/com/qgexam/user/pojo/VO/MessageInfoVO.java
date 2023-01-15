package com.qgexam.user.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tageshi
 * @date 2023/1/8 22:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfoVO implements Serializable {
    String examination_name;
    String start_time;
    String end_time;
}
