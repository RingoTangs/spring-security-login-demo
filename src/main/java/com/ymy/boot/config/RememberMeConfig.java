package com.ymy.boot.config;

import com.ymy.boot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

import static com.ymy.boot.constant.AuthConstant.*;

/**
 * remember-me 配置
 *
 * @author Ringo
 * @date 2021/5/16 20:35
 */
@Configuration
public class RememberMeConfig {

    @Resource
    private UserService userService;

    /**
     * MySQL数据连接
     */
    @Resource
    private DataSource dataSource;

    /**
     * remember-me token的存放位置
     */
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }

    /**
     * remember-me 服务 {@link ImageAuthenticationConfig} {@link SmsAuthenticationConfig} 中会用到
     */
    @Bean
    public RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices
                = new PersistentTokenBasedRememberMeServices(KEY, userService, tokenRepository());
        rememberMeServices.setTokenValiditySeconds(COOKIE_EXPIRE_IN);
        rememberMeServices.setParameter(PARAMETER);
        rememberMeServices.setCookieName(COOKIE_NAME);
        return rememberMeServices;
    }
}
