package com.capstone.shri.entity;

import lombok.Data;

@Data
public class UserApplication {
    private int id;
    private int userId;
    private int programmeId;
    private String signature;
    private String managementSignature;
    private String programmeInChargeSignature;
}
