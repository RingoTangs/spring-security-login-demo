package com.ymy.boot;

import com.ymy.boot.mapper.PersistentLoginMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static com.ymy.boot.constant.AuthConstant.*;
/**
 * @author Ringo
 * @date 2021/5/16 21:30
 */
@Slf4j
@SpringBootTest
public class RememberMeTest {

    @Resource
    private PersistentLoginMapper persistentLoginMapper;

    @Resource
    private PersistentTokenRepository tokenRepository;

    @Test
    void queryExpire() {
        List<String> list = persistentLoginMapper.queryExpireIn(COOKIE_EXPIRE_IN);
        log.info(list + "");
    }

    @Test
    void parseRememberMe()  {
        PersistentRememberMeToken token = tokenRepository.getTokenForSeries("1ZoYPVHLrldwh4VtKpQg+Q==");
        log.info(token.getDate()+"");
        log.info(token.getTokenValue());
    }
}
