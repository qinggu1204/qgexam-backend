package com.qgexam.answerPaper.marking.aspect;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.context.BaseContextHandler;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.PO.UserInfo;
import com.qgexam.user.pojo.VO.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @project qgexam
 * @description 所有controller的切面类，用于获取用户信息，设置到BaseContextHandler中，方便后续获取用户信息
 * @author yzw
 * @date 2022/12/16 00:45:12
 * @version 1.0
 */
@Aspect
@Slf4j
@Component
@Order(-200)
public class BaseContextAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(public * com.qgexam.*.controller..*.*(..))")
    public void controllerMethod() {
    }

    /**
     * 前置通知
     * @param joinPoint
     * @throws Exception
     */
    @Before("controllerMethod()")
    public void logRequestInfo(JoinPoint joinPoint) throws Exception {
        // 设置用户信息
        if (StpUtil.isLogin()){
            SaSession session = StpUtil.getSession(false);
            UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
            UserInfo userInfo = userInfoVO.getUserInfo();
            TeacherInfo teacherInfo = userInfoVO.getTeacherInfo();
            StudentInfo studentInfo = userInfoVO.getStudentInfo();
            BaseContextHandler.setUserID(userInfo == null ? null : userInfo.getUserId());
            BaseContextHandler.setUserName(userInfo == null ? null : userInfo.getUserName());
            BaseContextHandler.setStudentID(studentInfo == null ? null : studentInfo.getStudentId());
            BaseContextHandler.setTeacherID(teacherInfo == null ? null : teacherInfo.getTeacherId());
        }
    }

    /**
     * 后置通知
     * @param rvt
     * @throws Exception
     */
    @AfterReturning(returning = "rvt", pointcut = "controllerMethod()")
    public void logResultVoInfo(Object rvt) throws Exception {
        BaseContextHandler.remove();
    }

    /**
     * 异常通知
     *
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "controllerMethod()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        BaseContextHandler.remove();
    }

}
