package com.qgexam.common.web.handler.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;


import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.api.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @project qgexam
 * @description 处理sa-token相关异常
 * @author yzw
 * @date 2022/12/16 09:46:13
 * @version 1.0
 */
@RestControllerAdvice
@Order(-10)
@Slf4j
public class TokenExceptionHandler {

    /**
     * token相关异常
     *
     * @param notLoginException
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotLoginException.class)
    public ResponseResult<String> handlerNotLoginException(NotLoginException notLoginException) {
        log.warn("handlerNotLoginException [{}]", notLoginException.toString());
        // 打印堆栈，以供调试
        notLoginException.printStackTrace();
        // 判断场景值，定制化异常信息
        String message = "";
        ResponseResult result = null;
        switch (notLoginException.getType()) {
            // 未提供Token
            case NotLoginException.NOT_TOKEN:
                message = NotLoginException.NOT_TOKEN_MESSAGE;
                result = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR, message);
                break;
            // Token无效
            case NotLoginException.INVALID_TOKEN:
                message = NotLoginException.INVALID_TOKEN_MESSAGE;
                result = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR, message);
                break;
            // Token已过期
            case NotLoginException.TOKEN_TIMEOUT:
                message = NotLoginException.TOKEN_TIMEOUT_MESSAGE;
                result = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR, message);
                break;
            // Token已被顶下线
            case NotLoginException.BE_REPLACED:
                result = ResponseResult.errorResult(AppHttpCodeEnum.SIGN_IN_ELSEWHERE);
                break;
            // Token已被踢下线
            case NotLoginException.KICK_OUT:
                message = NotLoginException.KICK_OUT_MESSAGE;
                result = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR, message);
                break;
            // 当前会话未登录
            default:
                message = NotLoginException.DEFAULT_MESSAGE;
                result = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR, message);
                break;
        }
        return result;
    }

    /**
     * 没有角色
     *
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotRoleException.class)
    public ResponseResult<String> handlerNotRoleException(NotRoleException exception) {
        log.warn("handlerNotRoleException [{}]", exception.toString());
        return ResponseResult.errorResult(AppHttpCodeEnum.USER_NOROLE_ERROR, exception.getMessage());
    }




}
