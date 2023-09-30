package com.capstone.shri.dao;

import com.capstone.shri.entity.UserApplication;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationMapper {
    int insertApplication(UserApplication application);

    int hasApplied(int userId, int programmeId);
}
