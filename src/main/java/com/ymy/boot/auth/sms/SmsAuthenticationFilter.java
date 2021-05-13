package com.ymy.boot.auth.sms;

import com.ymy.boot.code.CheckCode;
import com.ymy.boot.code.ValidateCodeType;
import com.ymy.boot.code.sms.SmsCodeGenerator;
import com.ymy.boot.constant.AuthConstant;
import com.ymy.boot.utils.AppUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 手机号登录验证过滤器, 参照 {@link UsernamePasswordAuthenticationFilter}
 *
 * @author Ringo
 * @date 2021/5/10 9:19
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    // http请求需要传的参数 mobile=13855556666
    private String mobileParameter = AuthConstant.MOBILE;

    private boolean postOnly = true;

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher(AuthConstant.MOBILE_LOGIN_PROCESSOR_URL, "POST");

    @Setter
    @Getter
    private StringRedisTemplate redisTemplate;

    // ~ 构造方法
    public SmsAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public SmsAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    // ~ 方法
    // ==============================================================================================================

    // 过滤器核心方法
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException {
        // 1: 不是POST请求直接抛异常
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // 2: 拿到请求中的 mobile(手机号) codeInRequest(验证码)
        String mobile;                                          // 手机号
        String codeInRequest;                                   // 请求中的验证码

        if (AppUtil.isJsonLogin(request)) {
            // JSON登录
            Map<String, String> map = AppUtil.readJson(request);
            mobile = map.get(mobileParameter);
            codeInRequest = map.get(AuthConstant.SMS_CODE_REQUEST_PARAM);
        } else {
            // 表单登录
            mobile = obtainMobile(request);
            codeInRequest = request.getParameter(AuthConstant.SMS_CODE_REQUEST_PARAM);
        }
        mobile = (mobile != null) ? mobile : "";
        mobile = mobile.trim();

        // 3: 校验验证码
        codeInRequest = (codeInRequest != null) ? codeInRequest : "";
        String key = SmsCodeGenerator.keyGenerator(mobile, ValidateCodeType.SMS_CODE);
        String codeInServer = redisTemplate.opsForValue().get(key);
        CheckCode.check(codeInRequest, codeInServer);

        // 4: 封装 AuthenticationToken 为校验做准备
        SmsAuthenticationToken authRequest = new SmsAuthenticationToken(mobile);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(this.mobileParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setMobileParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "Mobile parameter must not be empty or null");
        this.mobileParameter = mobileParameter;
    }

    public String getMobileParameter() {
        return mobileParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }


    // ~ Builder
    // =============================================================================

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 建造者
     */
    public static class Builder {

        private AuthenticationManager authenticationManager;

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        private AuthenticationFailureHandler authenticationFailureHandler;

        private StringRedisTemplate redisTemplate;

        private Builder() {
        }

        public Builder authenticationManager(AuthenticationManager authenticationManager) {
            this.authenticationManager = authenticationManager;
            return this;
        }

        public Builder authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
            this.authenticationSuccessHandler = authenticationSuccessHandler;
            return this;
        }

        public Builder authenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
            this.authenticationFailureHandler = authenticationFailureHandler;
            return this;
        }

        public Builder redisTemplate(StringRedisTemplate redisTemplate) {
            this.redisTemplate = redisTemplate;
            return this;
        }

        public SmsAuthenticationFilter build() {
            SmsAuthenticationFilter filter = new SmsAuthenticationFilter();
            filter.setAuthenticationManager(authenticationManager);
            filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
            filter.setAuthenticationFailureHandler(authenticationFailureHandler);
            filter.setRedisTemplate(redisTemplate);
            return filter;
        }
    }
}
