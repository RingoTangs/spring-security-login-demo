package com.ymy.boot.code.sms;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import com.ymy.boot.code.ValidateCodeGenerator;
import com.ymy.boot.code.ValidateCodeType;
import com.ymy.boot.exception.ValidateCodeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

import static com.ymy.boot.constant.AuthConstant.MOBILE_REGEX;

/**
 * 短信验证码生成器
 *
 * @author Ringo
 * @date 2021/5/9 21:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsCodeGenerator extends ValidateCodeGenerator<SmsCode> {

    private StringRedisTemplate redisTemplate;

    @Override
    public SmsCode generate(int expireIn, int length) {
        String code = RandomUtil.randomNumbers(length);
        return new SmsCode(code, expireIn);
    }

    @Override
    protected SmsCode store(ValidateCodeType type, String mobile, int expireIn, int length) throws ValidateCodeException {

        if (!ReUtil.isMatch(MOBILE_REGEX, mobile)) {
            // 手机号不合法
            throw new ValidateCodeException("请填写正确的手机号！");
        }
        String key = keyGenerator(mobile, type);
        if (redisTemplate.hasKey(key).booleanValue()) {
            throw new ValidateCodeException("短信验证码已经存在, 请" + redisTemplate.getExpire(key) + "秒后再试！");
        }
        SmsCode smsCode = generate(expireIn, length);
        redisTemplate.opsForValue().setIfAbsent(key, smsCode.getCode(), expireIn, TimeUnit.SECONDS);
        return smsCode;
    }

    /**
     * 存储短信验证码并获得
     *
     * @param mobile   手机号
     * @param expireIn 过期时间
     * @param length   验证码长度
     * @return 短信验证码
     * @throws ValidateCodeException
     */
    public SmsCode storeAndGet(String mobile, int expireIn, int length) throws ValidateCodeException {
        SmsCode smsCode = store(ValidateCodeType.SMS_CODE, mobile, expireIn, length);

        // 给用户手机发短信(调用短信服务)
        // send message to mobile ...
        // sendTo(String mobile)

        return smsCode;
    }
}
