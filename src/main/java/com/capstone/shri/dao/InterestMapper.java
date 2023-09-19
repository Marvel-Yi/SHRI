package com.capstone.shri.dao;

import com.capstone.shri.entity.Interest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterestMapper {
    int insertInterest(Interest interest);

    int updateStatus(int id, int status, String response);

    List<Interest> selectInterests(int status, int offset, int limit);

    Interest selectInterestById(int id);
}
