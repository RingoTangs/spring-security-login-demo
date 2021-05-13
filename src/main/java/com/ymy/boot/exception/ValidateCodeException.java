package com.ymy.boot.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常
 *
 * @author Ringo
 * @date 2021/5/9 22:22
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String message) {
        super(message);
    }

}
