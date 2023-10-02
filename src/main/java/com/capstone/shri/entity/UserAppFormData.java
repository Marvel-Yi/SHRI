package com.capstone.shri.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserAppFormData {
    private int id;

    private int userId;

    private String passportNo;

    private String passportName;

    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birth;

    private String country;

    private String passType;

    private String finNo;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date passExpire;

    private String address;

    private int postalCode;

    private String academicInstitutionName;

    private String academicQualificationName;

    private int qualificationCompleteYear;

    private String companyName;

    private String companyIndustry;

    private int sponsorType; // 0: self, 1: company

    private String infoSource;
}
