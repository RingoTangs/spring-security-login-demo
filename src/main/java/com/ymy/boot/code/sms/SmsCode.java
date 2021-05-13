package com.ymy.boot.code.sms;

import com.ymy.boot.code.ValidateCode;

/**
 * 短信验证码类
 *
 * @author Ringo
 * @date 2021/5/9 20:40
 */
public class SmsCode extends ValidateCode {

    /**
     * 构造方法
     *
     * @param code     验证码
     * @param expireIn 过期时间(单位: 秒)
     */
    public SmsCode(String code, int expireIn) {
        super(code, expireIn);
    }
}
