package com.ymy.boot.auth.sms;

import com.ymy.boot.auth.LoginType;
import com.ymy.boot.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 校验 {@link SmsAuthenticationToken}
 *
 * @author Ringo
 * @date 2021/5/10 9:40
 */
@Getter
@Setter
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    // 校验 AuthenticationToken 的方法
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (!supports(authentication.getClass())) {
            return null;
        }

        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;

        // 1: 通过手机号去查询用户
        // 认证之前: Authentication principal中存的是用户登录信息
        User user = (User) userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());


        // ~ 代码到这里证明: 用户是存在的！

        // 2: 认证通过: 构造一个认证通过的Token，包含了用户信息和用户权限
        user.setLoginType(LoginType.MOBILE_LOGIN.value);
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    /**
     * 判断 authentication 是否是 SmsAuthenticationToken 类型的对象
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
