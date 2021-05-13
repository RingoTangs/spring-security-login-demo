package com.ymy.boot.controller;

import com.ymy.boot.code.image.ImageCode;
import com.ymy.boot.code.image.ImageCodeGenerator;
import com.ymy.boot.code.sms.SmsCode;
import com.ymy.boot.code.sms.SmsCodeGenerator;
import com.ymy.boot.constant.AuthConstant;
import com.ymy.boot.entity.RespBean;
import com.ymy.boot.exception.ValidateCodeException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ringo
 * @date 2021/5/9 20:17
 */
@RestController
public class HelloController {

    @Resource
    private SmsCodeGenerator smsCodeGenerator;

    @Resource
    private ImageCodeGenerator imageCodeGenerator;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * 获取短信验证码
     *
     * @param mobile   手机号
     * @param expireIn 短信验证码过期时间(默认60s)
     * @param length   短信验证码长度(默认6位)
     */
    @GetMapping(AuthConstant.SMS_CODE_API)
    public RespBean createSmsCode(@RequestParam(AuthConstant.MOBILE) String mobile,
                                  @RequestParam(value = "expireIn", defaultValue = "60") int expireIn,
                                  @RequestParam(value = "length", defaultValue = "6") int length) {
        try {

            SmsCode smsCode = smsCodeGenerator.storeAndGet(mobile, expireIn, length);
            return RespBean.builder().ok().message(mobile + " 获取短信验证码成功！").data(smsCode).build();
        } catch (ValidateCodeException e) {
            return RespBean.builder().failure().message(e.getMessage()).build();
        }
    }


    /**
     * 获得图片验证码
     *
     * @param response HTTP 响应
     * @param id       本次请求的唯一标识
     * @param expireIn 图片验证码过期时间(默认60s)
     * @param length   图片验证码长度(默认6位)
     */
    @GetMapping(AuthConstant.IMAGE_CODE_API)
    public RespBean createImageCode(HttpServletResponse response,
                                    @RequestParam(value = "id") String id,
                                    @RequestParam(value = "expireIn", defaultValue = "60") int expireIn,
                                    @RequestParam(value = "length", defaultValue = "4") int length) {
        RespBean respBean = null;
        try {
            ImageCode imageCode = imageCodeGenerator.storeAndGet(id, expireIn, length);

            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(imageCode.getImage(), "jpg", outputStream);
        } catch (ValidateCodeException | IOException e) {
            if (e instanceof ValidateCodeException) {
                respBean = RespBean.builder().failure().message(e.getMessage()).build();
            } else if (e instanceof IOException) {
                respBean = RespBean.builder().failure().message("图片验证码获取失败！").build();
            }
        }
        return respBean;
    }
}
