package com.ymy.boot.code;

import com.ymy.boot.exception.ValidateCodeException;
import lombok.extern.slf4j.Slf4j;

/**
 * 验证码生成器
 *
 * @author Ringo
 * @date 2021/5/9 20:42
 */
@Slf4j
public abstract class ValidateCodeGenerator<T extends ValidateCode> {

    /**
     * 生成验证码
     *
     * @param expireIn 验证码过期时间(单位: 秒)
     * @param length   验证码的长度 8848 => 4位
     * @return 验证码实体类
     */
    public abstract T generate(int expireIn, int length);


    /**
     * 验证码可以存在其他地方。
     * 调用这个方法, 既可以生成验证码还可以将验证码存储指定位置。
     * 例如: Redis。天生就具备过期时间。
     *
     * @param type     验证码类型
     * @param id       本次获取验证码的唯一标识
     * @param expireIn 过期时间
     * @param length   验证码位数
     * @return 验证码实体类
     */
    protected abstract T store(ValidateCodeType type, String id, int expireIn, int length) throws ValidateCodeException;


    /**
     * 生成验证码的key, 用于存储在服务器中(Redis、Session...)
     *
     * @param id   本次获取验证码的唯一标识
     * @param type 验证码类型(短信验证码/图片验证码)
     * @return 存储验证码的key
     */
    public static String keyGenerator(String id, ValidateCodeType type) {
        return type.value + "_" + id;
    }
}
