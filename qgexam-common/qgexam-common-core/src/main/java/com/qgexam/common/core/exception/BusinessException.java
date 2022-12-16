package com.qgexam.common.core.exception;

import com.qgexam.common.core.api.AppHttpCodeEnum;
import lombok.*;

/**
 * @author yzw
 * @description 自定义业务异常
 * @date 2022/12/14 20:31:49
 */

@Setter
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static BusinessException newInstance(AppHttpCodeEnum appHttpCodeEnum) {
        return new BusinessException(appHttpCodeEnum.getCode(), appHttpCodeEnum.getMsg());
    }
    public BusinessException() {
    }


}
