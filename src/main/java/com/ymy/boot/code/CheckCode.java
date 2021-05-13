package com.ymy.boot.code;

import cn.hutool.core.util.StrUtil;
import com.ymy.boot.exception.ValidateCodeException;

/**
 * 验证码校验
 *
 * @author Ringo
 * @date 2021/5/10 19:12
 */
public class CheckCode {

    /**
     * @param codeInRequest 请求中的验证码
     * @param codeInServer  存在服务器中的验证码
     */
    public static void check(String codeInRequest, String codeInServer) throws ValidateCodeException {
        // 1: 请求中的验证码空校验
        if (StrUtil.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空！");
        }

        // 2: 服务器中的验证码空校验
        if (StrUtil.isBlank(codeInServer)) {
            throw new ValidateCodeException("当前用户的验证码不存在或已过期, 请重试！");
        }

        // 3: 验证码相等校验
        if (!StrUtil.equalsIgnoreCase(codeInRequest, codeInServer)) {
            throw new ValidateCodeException("验证码不匹配, 请重试！");
        }

        // 4: 校验通过
    }
}
