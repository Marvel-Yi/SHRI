package com.capstone.shri.service;

import com.alibaba.fastjson.JSONObject;
import com.capstone.shri.dao.LoginTicketMapper;
import com.capstone.shri.dao.UserMapper;
import com.capstone.shri.entity.LoginTicket;
import com.capstone.shri.entity.User;
import com.capstone.shri.util.CommonConstant;
import com.capstone.shri.util.CommonUtil;
import com.capstone.shri.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService implements CommonConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MailClient mailClient;

    public String register(User user) {
        // null or blank
        if (user == null) {
            throw new IllegalArgumentException("user is null"); // runtime exception
        }
        if (StringUtils.isBlank(user.getUserName())) {
            return CommonUtil.getJSONString(1, "username is blank");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return CommonUtil.getJSONString(1, "password is blank");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            return CommonUtil.getJSONString(1, "email is blank");
        }

        User u = userMapper.selectByName(user.getUserName());
        if (u != null) {
            return CommonUtil.getJSONString(1, "username already exists");
        }
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            return CommonUtil.getJSONString(1, "email already exists");
        }

        user.setPassword(CommonUtil.md5(user.getPassword()));
        user.setUserType(0);
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // send an email for activation
        String greeting = "Hi " + user.getUserName() + ", you have registered successfully!";
        mailClient.send(user.getEmail(), EMAIL_SUBJECT_REGISTRATION, greeting);

        return CommonUtil.getJSONString(0, "registration succeeded");
    }

    public JSONObject login(String userName, String password) {
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(userName)) {
            json.put("code", 1);
            json.put("msg", "user name is blank");
            return json;
        }

        if (StringUtils.isBlank(password)) {
            json.put("code", 1);
            json.put("msg", "password is blank");
            return json;
        }

        User user = userMapper.selectByName(userName);
        if (user == null) {
            json.put("code", 1);
            json.put("msg", "user name does not exist or password is incorrect");
            return json;
        }

        if (!user.getPassword().equals(CommonUtil.md5(password))) {
            json.put("code", 1);
            json.put("msg", "user name does not exist or password is incorrect");
            return json;
        }

        // generate login ticket
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setUuid(CommonUtil.generateUUID());
        loginTicket.setLoginStatus(0); // 0 means status login
        loginTicket.setExpired(new Date(System.currentTimeMillis() + LOGIN_EXPIRE_SECONDS * 1000));
        loginTicketMapper.insertTicket(loginTicket);
        json.put("code", 0);
        json.put("msg", loginTicket.getUuid());
        json.put("userName", user.getUserName());
        json.put("userMail", user.getEmail());
        json.put("userType", user.getUserType());
        return json;
    }

    public LoginTicket getLoginTicket(String uuid) {
        return loginTicketMapper.selectByUuid(uuid);
    }

    public User getUserById(int userId) {
        return userMapper.selectById(userId);
    }
}
