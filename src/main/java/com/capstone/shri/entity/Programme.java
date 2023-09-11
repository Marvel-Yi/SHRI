package com.capstone.shri.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Programme {
    private int id;
    private String name;
    private String studyMode;
    private String certificateType;
    private int commitmentMonths;
    private int intakes;
    private Date intakeDate;
    private int cpdHours;
    private int fee;
    private int graduateStuCnt;
    private String objective;
    private String whoShouldAttend;
    private LocalDateTime courseBeginTime;
    private LocalDateTime courseEndTime;
    private String programmeNotes;
    private String assessment;
    private String deliveryMode;
    private int curStudentCnt;
    private int curTeacherCnt;
    private String certifiedBy;
}
