package com.ymy.boot.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 验证码类
 *
 * @author Ringo
 * @date 2021/5/9 20:19
 */
@Getter
@Setter
public abstract class ValidateCode {

    /**
     * 验证码
     */
    protected String code;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime expireTime;

    /**
     * 构造方法
     *
     * @param code     验证码
     * @param expireIn 过期时间(单位: 秒)
     */
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    @Override
    public String toString() {
        return "ValidateCode{" +
                "code='" + code + '\'' +
                ", expireTime=" +
                expireTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + '}';
    }
}
