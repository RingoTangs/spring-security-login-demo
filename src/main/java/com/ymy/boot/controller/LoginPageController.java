package com.ymy.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ringo
 * @date 2021/5/11 19:32
 */
@Controller
public class LoginPageController {

    @GetMapping("/JsonLogin.html")
    public String jsonLogin() {
        return "JsonLogin";
    }

}
