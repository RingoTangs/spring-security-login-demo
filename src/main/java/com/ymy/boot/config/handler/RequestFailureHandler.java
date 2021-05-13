package com.ymy.boot.config.handler;

import com.ymy.boot.entity.RespBean;
import com.ymy.boot.utils.AppUtil;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求失败处理器
 *
 * @author Ringo
 * @date 2021/5/10 21:27
 */
@Component
public class RequestFailureHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        RespBean respBean;

        if (authException instanceof InsufficientAuthenticationException) {
            respBean = RespBean.builder().failure().message("尚未登录, 请登录!").build();
        } else {
            respBean = RespBean.builder().failure().message("访问失败!").build();
        }

        AppUtil.printToScreen(response, respBean);
    }
}
