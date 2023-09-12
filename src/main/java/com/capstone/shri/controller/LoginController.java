package com.capstone.shri.controller;

import com.alibaba.fastjson.JSONObject;
import com.capstone.shri.entity.User;
import com.capstone.shri.service.UserService;
import com.capstone.shri.util.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController implements CommonConstant {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse response) {
        JSONObject jsonObject = userService.login(user.getUserName(), user.getPassword());
        Object code = jsonObject.get("code");
        if (code instanceof Integer) {
            Integer c = (Integer) code;
            if (c.intValue() == 0) {
                Object uuid = jsonObject.get("msg");
                Cookie cookie = new Cookie("ticket", uuid.toString());
                cookie.setPath("/");
                cookie.setMaxAge(LOGIN_EXPIRE_SECONDS);
                response.addCookie(cookie);
            }
        }
        return jsonObject.toJSONString();
    }
}