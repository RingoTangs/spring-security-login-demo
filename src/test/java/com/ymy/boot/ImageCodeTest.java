package com.ymy.boot;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Console;
import com.ymy.boot.code.image.ImageCode;
import com.ymy.boot.code.image.ImageCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Ringo
 * @date 2021/5/10 23:12
 */
@Slf4j
@SpringBootTest
public class ImageCodeTest {

    @Resource
    private ImageCodeGenerator imageCodeGenerator;

    @Test
    void testStoreImageCode() {
        ImageCode imageCode = imageCodeGenerator.storeAndGet("15732128977", 60, 6);
        log.info(imageCode.toString());
    }

    @Test
    void testGenerateImageCode() {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(150, 50);
        String code = lineCaptcha.getCode();
        BufferedImage image = (BufferedImage)lineCaptcha.createImage(code);
        Console.log(code);

        Console.log(image);
    }
}
