package com.ymy.boot.config.handler;

import com.ymy.boot.entity.RespBean;
import com.ymy.boot.entity.User;
import com.ymy.boot.utils.AppUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ringo
 * @date 2021/5/10 13:10
 */
@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        User user = null;
        if (authentication != null) {
            user = (User) authentication.getPrincipal();
        }

        RespBean respBean = RespBean.builder().ok().message("成功注销!").data(user).build();
        AppUtil.printToScreen(response, respBean);
    }
}
