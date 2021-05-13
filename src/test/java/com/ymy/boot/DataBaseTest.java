package com.ymy.boot;

import com.ymy.boot.entity.User;
import com.ymy.boot.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author Ringo
 * @date 2021/5/10 10:34
 */
@Slf4j
@SpringBootTest
public class DataBaseTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    void queryUser() {
        User user = userMapper.selectById("Ringo");
        log.info(user + "");
    }

    @Test
    void passwordEncoder() {
//        log.info(passwordEncoder.encode("123"));
        boolean ret = passwordEncoder.matches("123", "$2a$10$40eTJ/JPeEzvHGlQHodlIeC73VVSWd7haEHQ7uTqakdBFLDWXc9La");
        log.info(ret + "");
    }

}
