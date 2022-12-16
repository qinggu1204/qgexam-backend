package com.qgexam.common.web.base;


import com.qgexam.common.core.context.BaseContextHandler;

/**
 * @project qgexam
 * @description 其中调用 BaseContextHandler 中的方法，获取当前用户的信息、学生信息、教师信息等，
 * 所有的 Controller 都需要继承该类，以便获取当前用户的信息
 * @author yzw
 * @date 2022/12/16 09:46:47
 * @version 1.0
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
