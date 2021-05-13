package com.ymy.boot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ymy.boot.entity.User;
import com.ymy.boot.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ymy.boot.auth.sms.SmsAuthenticationProvider;

import javax.annotation.Resource;

/**
 * @author Ringo
 * @date 2021/5/10 10:42
 */
@Service
public class UserService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    /**
     * 该方法在 {@link SmsAuthenticationProvider} 中被调用。
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1: 先按照 username 查询, 用户名查不到再按照 mobile(手机号) 查
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", username).or().eq("username", username);
        User user = userMapper.selectOne(wrapper);

        // 2: username和mobile都查不到直接抛出异常
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在, 请先注册~");
        }

        // 4: 查到用户信息
        // 设置角色 user.setAuthorities(List<>) ....

        return user;
    }
}
