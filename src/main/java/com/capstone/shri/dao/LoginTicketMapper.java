package com.capstone.shri.dao;

import com.capstone.shri.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface LoginTicketMapper {
    int insertTicket(LoginTicket ticket);

    LoginTicket selectByUuid(String uuid);

    LoginTicket selectByUserId(int userId);

    int updateTicket(int loginStatus, Date expired);
}
