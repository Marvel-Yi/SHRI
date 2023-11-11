package com.capstone.shri.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Document {
    private int id;
    private String path;
    private String name;
    private String suffix;
    private Date createTime;
    private int documentType; // 0: education certificate, 1: language certificate, 2: employment certificate
    private int userId;
    private int appId;
}
