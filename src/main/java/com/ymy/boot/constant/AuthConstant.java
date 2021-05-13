package com.ymy.boot.constant;

/**
 * 权限验证系统中的常量
 *
 * @author Ringo
 * @date 2021/5/10 20:23
 */
public interface AuthConstant {

    // ~ 短信验证码相关

    /**
     * 请求中携带短信验证码的参数
     * 例: http://locahost:8080/login/mobile?smsCode=123456
     */
    String SMS_CODE_REQUEST_PARAM = "smsCode";

    /**
     * 手机号
     */
    String MOBILE = "mobile";

    /**
     * 使用手机号登录的地址
     */
    String MOBILE_LOGIN_PROCESSOR_URL = "/login/mobile";

    /**
     * 获取短信验证码的 HTTP 接口
     */
    String SMS_CODE_API = "/smsCode";

    /**
     * 手机号正则表达式
     */
    String MOBILE_REGEX = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";


    // ~ 用户名密码登录、图片验证码相关
    // ======================================================================


    /**
     * 用户名 密码登录的地址
     */
    String USERNAME_LOGIN_PROCESSOR_URL = "/doLogin";

    /**
     * 获取图片验证码的 HTTP 接口
     */
    String IMAGE_CODE_API = "/imageCode";

    /**
     * 用户名
     */
    String USERNAME = "username";

    /**
     * 密码
     */
    String PASSWORD = "password";

    /**
     * 请求中携带图片验证码的参数
     */
    String IMAGE_CODE_REQUEST_PARAM = "imageCode";


    /**
     * 注销登录
     */
    String LOGOUT_URL = "/logout";
}
