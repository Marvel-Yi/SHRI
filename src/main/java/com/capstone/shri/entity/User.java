package com.capstone.shri.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int id;
    private String userName;
    private String password;
    private String email;
    private int userType;
    private Date createTime;
}
