package com.qgexam.common.web.configure;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Value("${ignored}")
    private List<String> ignored;
    // 注册 Sa-Token 的拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println(ignored);
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {

            // 登录校验 -- 拦截所有路由
            SaRouter.match("/**", r -> StpUtil.checkLogin());

            // 角色校验 -- 拦截以 tea 开头的路由，必须具备 tea 角色才可以通过认证
            SaRouter.match("/tea/**", r -> StpUtil.checkRoleAnd("teacher"));
            SaRouter.match("/stu/**", r -> StpUtil.checkRoleAnd("student"));
            SaRouter.match("/netea/**", r -> StpUtil.checkRoleAnd("neteacher"));
            SaRouter.match("/admin/**", r -> StpUtil.checkRoleAnd("admin"));
            SaRouter.match("/user/common/**", r ->StpUtil.checkRoleOr("teacher", "student", "neteacher", "admin"));
            SaRouter.match("/common/**", r ->StpUtil.checkRoleOr("teacher", "student", "neteacher", "admin"));
            SaRouter.match("/alltea/**", r -> StpUtil.checkRoleOr("teacher", "neteacher"));


        })).addPathPatterns("/**")
                .excludePathPatterns(ignored);
    }
}
