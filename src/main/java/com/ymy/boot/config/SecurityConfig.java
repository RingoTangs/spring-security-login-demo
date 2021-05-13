package com.ymy.boot.config;

import com.ymy.boot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;

import static com.ymy.boot.constant.AuthConstant.*;


/**
 * Spring Security 主配置类
 *
 * @author Ringo
 * @date 2021/5/9 20:11
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * {@link UserDetailsService} 实现类
     */
    @Resource
    private UserService userService;

    /**
     * 注销登录处理器
     */
    @Resource
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 请求失败处理器
     */
    @Resource
    private AuthenticationEntryPoint requestFailureHandler;

    /**
     * 短信验证码 + 手机号登录配置
     */
    @Resource
    private SmsAuthenticationConfig smsAuthenticationConfig;

    /**
     * 图片验证码 + 用户名密码登录配置
     */
    @Resource
    private ImageAuthenticationConfig imageAuthenticationConfig;


    /**
     * 密码加密解密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DaoAuthenticationProvider 配置 UsernameNotFoundException 向上抛出。
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Spring Security 原生的 AuthenticationProvider 需要在这里配置才会生效！
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 添加自定义的 AuthenticationProvider
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静态资源放行
        web.ignoring().antMatchers(
                "/css/**", "/js/**", "/index.html", "/img/**", "/webjars/**",
                "/fonts/**", "/favicon.ico", "/JsonLogin.html"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(SMS_CODE_API, IMAGE_CODE_API, MOBILE_LOGIN_PROCESSOR_URL, USERNAME_LOGIN_PROCESSOR_URL)
                .permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()

                .and()
                .logout()
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(requestFailureHandler)

                .and()
                .csrf().disable()

                // 短信验证码 + 手机号登录加到 Spring Security 中
                .apply(smsAuthenticationConfig)

                .and()
                // 图片验证码 + 用户名密码登录加入到 Spring Security 中
                .apply(imageAuthenticationConfig);
    }
}
