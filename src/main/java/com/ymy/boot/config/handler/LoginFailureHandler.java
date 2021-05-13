package com.ymy.boot.config.handler;

import com.ymy.boot.entity.RespBean;
import com.ymy.boot.exception.ValidateCodeException;
import com.ymy.boot.utils.AppUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ringo
 * @date 2021/5/10 12:42
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        RespBean respBean;

        if (exception instanceof BadCredentialsException) {
            respBean = RespBean.builder().failure().message("密码错误!").build();
        } else if (exception instanceof UsernameNotFoundException) {
            respBean = RespBean.builder().failure().message(exception.getMessage()).build();
        } else if (exception instanceof AuthenticationServiceException) {
            respBean = RespBean.builder().failure().message("权限验证失败!").build();
        } else if (exception instanceof ValidateCodeException) {
            respBean = RespBean.builder().failure().message(exception.getMessage()).build();
        } else {
            respBean = RespBean.builder().failure().message("登录失败!").build();
        }
        AppUtil.printToScreen(response, respBean);
    }
}
