package com.qgexam.user.aspect;


import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.web.context.BaseContextHandler;
import com.qgexam.user.pojo.VO.GetUserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @project qgexam
 * @description 获取session中的用户信息
 * @author yzw
 * @date 2022/12/14 11:26:15
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
     * @description 前置通知，如果用户已经登录，使用BaseContextHandler向threadlocal中设置用户信息
     * @param joinPoint
     * @return void
     * @author yzw
     * @date 2022/12/14 11:30:12
     */
    @Before("controllerMethod()")
    public void logRequestInfo(JoinPoint joinPoint) throws Exception {
        // 如果用户已经登录，使用BaseContextHandler向threadlocal中设置用户信息
        if (StpUtil.isLogin()){
            SaSession session = StpUtil.getSession(false);
            GetUserInfoVO userInfoVO = session.getModel(SystemConstants.SESSION_KEY, GetUserInfoVO.class);
            BaseContextHandler.setUserID(userInfoVO.getUserId());
            BaseContextHandler.setUsername(userInfoVO.getLoginName());
        }
    }

    /**
     * 后置通知
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
