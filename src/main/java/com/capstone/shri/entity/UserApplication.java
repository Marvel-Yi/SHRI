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
    private int status; // 0: files not uploaded, 1: under review, 2: pending, 3: rejected, 4: admitted
    private Date applyDate;
}
