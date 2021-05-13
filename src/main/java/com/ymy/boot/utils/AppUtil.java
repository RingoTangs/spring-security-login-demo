package com.ymy.boot.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Ringo
 * @date 2021/5/10 18:44
 */
public class AppUtil {

    /**
     * 判断JSON登录, 还是表单登录
     *
     * @param request 请求
     * @return true: json登录; false: 表单登录
     */
    public static boolean isJsonLogin(HttpServletRequest request) {
        if (StrUtil.equalsIgnoreCase(request.getContentType(), "application/json;charset=UTF-8")) {
            return true;
        }
        if (StrUtil.equalsIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            return true;
        }
        return false;
    }

    /**
     * 从请求中读取JSON数据, 封装成Map
     */
    public static Map<String, String> readJson(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        try {
            Map<String, String> map = new ObjectMapper().readValue(inputStream, Map.class);
            return map;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }


    /**
     * 以JSON的形式将对象输出
     */
    public static void printToScreen(HttpServletResponse response, Object obj) throws IOException {
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSONUtil.toJsonStr(obj));
        printWriter.flush();
        printWriter.close();
    }
}
