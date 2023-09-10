package com.capstone.shri.controller;

import com.alibaba.fastjson.JSONObject;
import com.capstone.shri.entity.User;
import com.capstone.shri.service.UserService;
import com.capstone.shri.util.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController implements CommonConstant {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(String userName, String password) {
        JSONObject jsonObject = userService.login(userName, password);
        return jsonObject.toJSONString();
    }
}