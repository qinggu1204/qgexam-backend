package com.qgexam.common.core.exception;

/**
 * @author yzw
 * @description 自定义业务异常
 * @date 2022/12/14 20:31:49
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    private String msg;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public BusinessException(String message) {
        super(message);
        this.msg = message;
    }

    public BusinessException() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

}
