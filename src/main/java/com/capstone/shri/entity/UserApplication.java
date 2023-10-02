package com.capstone.shri.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserApplication {
    private int id;
    private int userId;
    private String userName;
    private String userEmail;
    private int programmeId;
    private String programmeName;
    private String signature;
    private String managementSignature;
    private String programmeInChargeSignature;
    private int status; // 0: under review, 1: pending, 2: rejected, 3: admitted
    private Date applyDate;
}
