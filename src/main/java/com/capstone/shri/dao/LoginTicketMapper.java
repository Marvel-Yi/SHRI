package com.capstone.shri.dao;

import com.capstone.shri.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTicketMapper {
    int insertTicket(LoginTicket ticket);

    LoginTicket selectByUuid(String uuid);
}
