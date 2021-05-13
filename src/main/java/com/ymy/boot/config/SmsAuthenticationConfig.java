package com.ymy.boot.config;

import com.ymy.boot.auth.sms.SmsAuthenticationFilter;
import com.ymy.boot.auth.sms.SmsAuthenticationProvider;
import com.ymy.boot.config.handler.LoginFailureHandler;
import com.ymy.boot.config.handler.LoginSuccessHandler;
import com.ymy.boot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * 短信验证码 + 手机号登录配置类
 *
 * @author Ringo
 * @date 2021/5/10 13:18
 */
@Configuration
public class SmsAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    /**
     * 登录成功处理器
     */
    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    /**
     * 登录失败处理器
     */
    @Resource
    private LoginFailureHandler loginFailureHandler;

    /**
     * {@link UserDetailsService} 会在 AuthenticationProvider 中使用
     */
    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 配置 AuthenticationProvider 需要有 UserDetailsService。
    @Bean
    public SmsAuthenticationProvider smsAuthenticationProvider() {
        SmsAuthenticationProvider provider = new SmsAuthenticationProvider();
        provider.setUserDetailsService(userService);
        return provider;
    }

    // 组装手机号登录验证逻辑
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置 AuthenticationFilter 需要 AuthenticationManager、登录成功处理器、登录失败处理器。
        SmsAuthenticationFilter filter = SmsAuthenticationFilter.builder()
                .authenticationManager(http.getSharedObject(AuthenticationManager.class))
                .authenticationSuccessHandler(loginSuccessHandler)
                .authenticationFailureHandler(loginFailureHandler)
                .redisTemplate(stringRedisTemplate)
                .build();

        /**
         * http.authenticationProvider() 添加 AuthenticationProvider
         * 等同于 {@link SecurityConfig#configure(AuthenticationManagerBuilder)}
         */
        http.authenticationProvider(smsAuthenticationProvider())
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
