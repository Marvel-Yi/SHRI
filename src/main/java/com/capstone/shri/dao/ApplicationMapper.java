package com.capstone.shri.dao;

import com.capstone.shri.entity.UserApplication;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface ApplicationMapper {
    int insertApplication(UserApplication application);

    int hasApplied(int userId, int programmeId);

    List<UserApplication> selectApplications(int status, int offset, int limit);

    List<UserApplication> selectDecidedApps();

    List<UserApplication> selectApplicationsByUserId(int userId);

    UserApplication selectApplicationById(int id);

    int updateSignature(int appId, byte[] signature);

    int updateStatus(int appId, int status, String advice, Date updateDate);

    int updateHasSent(int appId);
}
