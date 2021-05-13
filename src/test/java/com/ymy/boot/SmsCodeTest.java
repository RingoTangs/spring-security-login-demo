package com.ymy.boot;

import cn.hutool.core.util.ReUtil;
import com.ymy.boot.code.sms.SmsCode;
import com.ymy.boot.code.sms.SmsCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ymy.boot.constant.AuthConstant.MOBILE_REGEX;

/**
 * 测试短信验证码
 *
 * @author Ringo
 * @date 2021/5/9 21:29
 */
@Slf4j
@SpringBootTest
public class SmsCodeTest {

    @Resource
    private SmsCodeGenerator smsCodeGenerator;

    @Test
    void testGenerateSmsCode() {
        SmsCode smsCode = smsCodeGenerator.generate(10, 6);
        log.info(smsCode.toString());
    }

    @Test
    void testMobileRegex() {
        String mobile = "15732128977";
//        System.out.println(ReUtil.isMatch(MOBILE_REGEX, mobile));
        System.out.println(mobile.matches(MOBILE_REGEX));
    }
}
