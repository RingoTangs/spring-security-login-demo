package com.ymy.boot.code.image;

import com.ymy.boot.code.ValidateCode;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

/**
 * 图片验证码
 *
 * @author Ringo
 * @date 2021/5/10 22:49
 */
@Getter
@Setter
public class ImageCode extends ValidateCode {

    /**
     * 图片验证码（返回给前端）
     */
    private BufferedImage image;

    /**
     * 构造方法
     *
     * @param code     验证码
     * @param expireIn 过期时间(单位: 秒)
     */
    public ImageCode(String code, int expireIn, BufferedImage image) {
        super(code, expireIn);
        this.image = image;
    }
}
