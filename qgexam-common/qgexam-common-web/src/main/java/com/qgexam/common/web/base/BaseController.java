package com.qgexam.common.web.base;


import com.qgexam.common.web.context.BaseContextHandler;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/20 16:30
 */
public class BaseController {

    /**
     * 获取用户id
     */
    protected Integer getUserId() {
        return BaseContextHandler.getUserID();
    }

    /**
     * 获取用户名称
     */
    protected String getLoginName() {
        return BaseContextHandler.getLoginName();
    }



}
