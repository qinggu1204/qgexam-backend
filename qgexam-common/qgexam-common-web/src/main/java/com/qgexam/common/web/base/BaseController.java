package com.qgexam.common.web.base;


import com.qgexam.common.core.context.BaseContextHandler;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/20 16:30
 */
public class BaseController {

    /**
     * 获取用户id
     */
    public Integer getUserId() {
        return BaseContextHandler.getUserID();
    }
    /**
     * 获取学生id
     */
    public Integer getStudentId() {
        return BaseContextHandler.getStudentID();
    }

    /**
     * 获取教师id
     */
    public Integer getTeacherId() {
        return BaseContextHandler.getTeacherID();
    }


}
