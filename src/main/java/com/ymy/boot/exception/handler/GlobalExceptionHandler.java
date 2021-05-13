package com.ymy.boot.exception.handler;

import com.ymy.boot.entity.RespBean;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 全局异常处理器
 *
 * @author Ringo
 * @date 2021/5/10 12:20
 */
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理 IOException
    @ExceptionHandler({IOException.class})
    public RespBean handleIOException(IOException e) {
        return RespBean.builder().failure().message("IO异常！").build();
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public RespBean handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String msg = "请填写参数[" + e.getParameterName() + "]";
        return RespBean.builder().failure().message(msg).build();
    }
}
