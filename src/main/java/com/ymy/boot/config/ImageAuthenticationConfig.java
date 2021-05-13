package com.ymy.boot.config;

import com.ymy.boot.auth.image.ImageAuthenticationFilter;
import com.ymy.boot.code.config.properties.ImageCodeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * 图片验证码 + 用户名密码登录配置类
 *
 * @author Ringo
 * @date 2021/5/11 18:32
 */
@Configuration
public class ImageAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AuthenticationSuccessHandler loginSuccessHandler;

    @Resource
    private AuthenticationFailureHandler loginFailureHandler;

    @Autowired
    private ImageCodeProperties imageCodeProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ImageAuthenticationFilter filter = ImageAuthenticationFilter.builder()
                .authenticationManager(http.getSharedObject(AuthenticationManager.class))
                .authenticationSuccessHandler(loginSuccessHandler)
                .authenticationFailureHandler(loginFailureHandler)
                .redisTemplate(stringRedisTemplate)
                .imageCodeProperties(imageCodeProperties)
                .build();
        http.addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
