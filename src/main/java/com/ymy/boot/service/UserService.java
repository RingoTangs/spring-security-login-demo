package com.ymy.boot.service;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ymy.boot.auth.sms.SmsAuthenticationProvider;
import com.ymy.boot.entity.User;
import com.ymy.boot.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.ymy.boot.constant.AuthConstant.MOBILE_REGEX;

/**
 * @author Ringo
 * @date 2021/5/10 10:42
 */
@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    /**
     * 该方法在 {@link SmsAuthenticationProvider} {@link DaoAuthenticationProvider}中被调用。
     * 作用：查询用户是否存在, 无论是username还是mobile都是用户的唯一标识。
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper<User> wrapper = new QueryWrapper<>();

        // 1: 区分username是用户名还是手机号
        if (ReUtil.isMatch(MOBILE_REGEX, username)) {
            // 手机号登录
            log.info("mobile login...");
            wrapper.eq("mobile", username);
        } else {
            // 用户名登录
            log.info("username login..");
            wrapper.eq("username", username);
        }

        User user = userMapper.selectOne(wrapper);

        // 2: 用户不存在直接抛出异常
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在, 请先注册~");
        }

        // 4: 查到用户信息
        // 设置角色 user.setAuthorities(List<>) ....

        return user;
    }
}
