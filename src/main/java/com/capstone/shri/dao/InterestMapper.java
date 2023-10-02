package com.capstone.shri.dao;

import com.capstone.shri.entity.Interest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterestMapper {
    int insertInterest(Interest interest);

    int update(int id, int status, String res);

    List<Interest> selectInterests(int status, int offset, int limit);

    Interest selectInterestById(int id);
}
