package com.ymy.boot.auth.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * <code>SmsAuthenticationToken</code> 包含一个principal属性。
 * 从它的两个构造函数可以看出，在认证之前 principal 存的是手机号，认证之后存的是用户信息(UserDetails)。
 *
 * <code>UsernamePasswordAuthenticationToken</code> 原来还包含一个 <code>credentials</code> 属性用于存放密码，这里不需要就去掉了。
 *
 * @author Ringo
 * @date 2021/5/10 8:55
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 校验前: 用户名(手机号)
     * 校验成功: principal 封装的是用户信息(UserDetailsService)
     */
    private final Object principal;

    /**
     * 请求中的验证码
     */
//    @Getter
//    private String code;

    /**
     * 校验之前: 构造一个 <code>SmsAuthenticationToken</code> 就用这个构造方法
     *
     * @param mobile 手机号
     */
    public SmsAuthenticationToken(String mobile) {
        super(null);
        this.principal = mobile;
        setAuthenticated(false);
    }

    /**
     * 只有 <code>AuthenticationManager</code> 或者 <code>AuthenticationProvider</code>
     * 校验通过才使用这个构造方法！
     *
     * @param principal   用户信息
     * @param authorities 用户角色
     */
    public SmsAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }
}
