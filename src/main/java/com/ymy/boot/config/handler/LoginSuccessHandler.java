package com.ymy.boot.config.handler;

import com.ymy.boot.entity.RespBean;
import com.ymy.boot.entity.User;
import com.ymy.boot.utils.AppUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ringo
 * @date 2021/5/10 12:40
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        // Principal 就是 实体类 User 的信息
        User user = (User) authentication.getPrincipal();
        user.setPassword(null);

        RespBean respBean = RespBean.builder().ok().message("登录成功！").data(user).build();
        AppUtil.printToScreen(response, respBean);
    }
}
