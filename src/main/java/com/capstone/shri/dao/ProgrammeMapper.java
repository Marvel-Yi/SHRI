package com.capstone.shri.dao;

import com.capstone.shri.entity.Programme;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProgrammeMapper {
    List<Programme> selectProgrammeList(int offset, int limit);

    Programme selectProgrammeById(int id);

    List<Programme> selectProgrammeByFilters(int offset, int limit, String studyMode, List<String> certificateTypes);

    List<Programme> selectProgrammeByCertificate(int offset, int limit, List<String> certificateTypes);

    List<Programme> selectProgrammeByMode(int offset, int limit, String studyMode);
}
