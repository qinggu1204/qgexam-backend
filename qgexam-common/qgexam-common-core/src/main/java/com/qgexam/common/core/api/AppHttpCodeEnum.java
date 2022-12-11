package com.qgexam.common.core.api;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    TOKEN_ERROR(400, "TOKEN错误"),
    USER_NOROLE_ERROR(401, "没有角色"),
    SIGN_IN_ELSEWHERE(402, "在别处登录"),
    SYSTEM_ERROR(500, "出现错误"),
    USERNAME_EXIST(501, "用户名已存在"),
    PHONENUMBER_EXIST(502, "手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505, "用户名或密码错误"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传jpg文件"),
    CHECK_ERROR(506, "参数校验错误");

    final int code;
    final String msg;

    private AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}