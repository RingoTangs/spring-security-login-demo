package com.ymy.boot.auth.image;

import com.ymy.boot.code.CheckCode;
import com.ymy.boot.code.ValidateCodeType;
import com.ymy.boot.code.config.properties.ImageCodeProperties;
import com.ymy.boot.code.image.ImageCodeGenerator;
import com.ymy.boot.utils.AppUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.ymy.boot.constant.AuthConstant.*;

/**
 * @author Ringo
 * @date 2021/5/11 13:13
 */
@Slf4j
public class ImageAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String usernameParameter = USERNAME;

    private String passwordParameter = PASSWORD;

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher(USERNAME_LOGIN_PROCESSOR_URL, "POST");

    private boolean postOnly = true;

    @Getter
    @Setter
    private StringRedisTemplate redisTemplate;

    @Getter
    @Setter
    private ImageCodeProperties imageCodeProperties;

    public ImageAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public ImageAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    // ~ methods
    // ===========================================================================================================


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username;
        String password;
        String codeInRequest;

        String id;                   // 获取本次图片验证码的唯一标识

        // 1: 获取登录信息
        if (AppUtil.isJsonLogin(request)) {
            // JSON登录
            Map<String, String> map = AppUtil.readJson(request);
            username = map.get(USERNAME);
            password = map.get(PASSWORD);
            codeInRequest = map.get(IMAGE_CODE_REQUEST_PARAM);
            id = map.get("id");

        } else {
            // 表单登录
            username = obtainUsername(request);
            password = obtainPassword(request);
            codeInRequest = request.getParameter(IMAGE_CODE_REQUEST_PARAM);
            id = request.getParameter("id");
        }

        username = (username != null) ? username : "";
        username = username.trim();
        password = (password != null) ? password : "";
        codeInRequest = (codeInRequest != null) ? codeInRequest : "";
        id = (id != null) ? id : "";

        // 2: 图片验证码校验

        // 启用图片验证码功能
        if (imageCodeProperties.isEnabled()) {
            String key = ImageCodeGenerator.keyGenerator(id, ValidateCodeType.IMAGE_CODE);
            String codeInServer = redisTemplate.opsForValue().get(key);
            CheckCode.check(codeInRequest, codeInServer);

            logger.info("ImageAuthenticationFilter 图片验证码校验成功..");
        }

        // 3: 验证码校验成功, 用户名密码校验！
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);


        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }


    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return this.usernameParameter;
    }

    public final String getPasswordParameter() {
        return this.passwordParameter;
    }

    // ~ Builder
    // ==============================================================================

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

        private ImageCodeProperties imageCodeProperties;

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

        public Builder imageCodeProperties(ImageCodeProperties imageCodeProperties) {
            this.imageCodeProperties = imageCodeProperties;
            return this;
        }

        public ImageAuthenticationFilter build() {
            ImageAuthenticationFilter filter = new ImageAuthenticationFilter();
            filter.setAuthenticationManager(authenticationManager);
            filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
            filter.setAuthenticationFailureHandler(authenticationFailureHandler);
            filter.setRedisTemplate(redisTemplate);
            filter.setImageCodeProperties(imageCodeProperties);
            return filter;
        }
    }
}
