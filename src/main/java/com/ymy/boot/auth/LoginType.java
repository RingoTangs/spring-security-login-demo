package com.ymy.boot.auth;

/**
 * 登录类型:
 * 1、手机号登录。
 * 2、username password 登录。
 *
 * @author Ringo
 * @date 2021/5/10 14:08
 */
public enum LoginType {

    MOBILE_LOGIN("mobile_login"),

    USERNAME_PASSWORD_LOGIN("username_password_login");

    public String value;

    LoginType(String value) {
        this.value = value;
    }
}
