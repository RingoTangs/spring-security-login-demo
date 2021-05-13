package com.ymy.boot.code.config;

import com.ymy.boot.code.config.properties.ImageCodeProperties;
import com.ymy.boot.code.image.ImageCodeGenerator;
import com.ymy.boot.code.sms.SmsCodeGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * 验证码的配置类
 *
 * @author Ringo
 * @date 2021/5/9 21:31
 */
@Configuration
@EnableConfigurationProperties(ImageCodeProperties.class)
public class ValidateCodeConfig {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public SmsCodeGenerator smsCodeGenerator() {
        return new SmsCodeGenerator(stringRedisTemplate);
    }

    @Bean
    public ImageCodeGenerator imageCodeGenerator() {
        return new ImageCodeGenerator(stringRedisTemplate);
    }
}
