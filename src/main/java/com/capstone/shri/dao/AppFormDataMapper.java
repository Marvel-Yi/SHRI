package com.capstone.shri.dao;

import com.capstone.shri.entity.UserAppFormData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppFormDataMapper {
    int insertAppFormData(UserAppFormData data);

    int hasUserFormData(int userId);

    UserAppFormData selectFormDataByUserId(int userId);

    int update(int userId, UserAppFormData data);
}
