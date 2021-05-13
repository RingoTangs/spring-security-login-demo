package com.ymy.boot.code.image;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.StrUtil;
import com.ymy.boot.code.ValidateCodeGenerator;
import com.ymy.boot.code.ValidateCodeType;
import com.ymy.boot.exception.ValidateCodeException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * 图片验证码生成器
 *
 * @author Ringo
 * @date 2021/5/10 22:53
 */
@Getter
@Setter
@NoArgsConstructor
public class ImageCodeGenerator extends ValidateCodeGenerator<ImageCode> {

    /**
     * 图片的宽度
     */
    private int width = 150;

    /**
     * 图片的高度
     */
    private int height = 50;

    /**
     * 干扰线数量
     */
    private int thickness = 5;

    private StringRedisTemplate redisTemplate;

    public ImageCodeGenerator(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ImageCode generate(int expireIn, int length) {
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(width, height, length, thickness);
        String code = captcha.getCode();
        BufferedImage image = (BufferedImage) captcha.createImage(code);
        return new ImageCode(code, expireIn, image);
    }

    @Override
    protected ImageCode store(ValidateCodeType type, String id, int expireIn, int length) throws ValidateCodeException {
        if (StrUtil.isBlank(id.trim())) {
            throw new ValidateCodeException("参数[id]不能为空, id代表本次获取图片验证码的唯一标识！" +
                    "校验时请携带上该id。");
        }
        String key = keyGenerator(id, type);
        ImageCode imageCode = generate(expireIn, length);
        redisTemplate.opsForValue().set(key, imageCode.getCode(), expireIn, TimeUnit.SECONDS);
        return imageCode;
    }

    /**
     * 存储图片验证码并获得
     *
     * @param id       本次获取验证码的唯一标识(校验时也要用这个id)
     * @param expireIn 过期时间
     * @param length   验证码长度
     * @return 图片验证码
     */
    public ImageCode storeAndGet(String id, int expireIn, int length) {
        return store(ValidateCodeType.IMAGE_CODE, id, expireIn, length);
    }
}
