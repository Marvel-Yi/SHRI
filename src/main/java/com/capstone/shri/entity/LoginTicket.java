package com.capstone.shri.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String uuid;
    private int loginStatus; // 0 means login, 1 means logout
    private Date expired;
}
