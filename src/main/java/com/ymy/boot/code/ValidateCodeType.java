package com.ymy.boot.code;

/**
 * 验证码类型
 *
 * @author Ringo
 * @date 2021/5/9 21:02
 */
public enum ValidateCodeType {

    SMS_CODE("sms_code"), IMAGE_CODE("image_code");

    public String value;

    ValidateCodeType(String value) {
        this.value = value;
    }
}
