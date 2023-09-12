package com.capstone.shri.service;

import com.capstone.shri.controller.dto.ProgrammeFilterReq;
import com.capstone.shri.dao.ProgrammeMapper;
import com.capstone.shri.entity.Programme;
import com.capstone.shri.util.CommonUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgrammeService {
    @Autowired
    private ProgrammeMapper programmeMapper;

    public List<Programme> getAllProgrammes(int cur, int limit) {
        return programmeMapper.selectProgrammeList(CommonUtil.getOffset(cur, limit), limit);
    }

    public Programme getProgrammeById(int id) {
        return programmeMapper.selectProgrammeById(id);
    }

    public List<Programme> getAllProgrammesWithFilters(ProgrammeFilterReq req) {
        if (Strings.isBlank(req.getStudyMode()) && isBlankList(req.getCertificateTypes())) {
            return programmeMapper.selectProgrammeList(req.getOffset(), req.getLimit());
        }
        if (Strings.isBlank(req.getStudyMode())) {
            return programmeMapper.selectProgrammeByCertificate(req.getOffset(), req.getLimit(), req.getCertificateTypes());
        }
        if (isBlankList(req.getCertificateTypes())) {
            return programmeMapper.selectProgrammeByMode(req.getOffset(), req.getLimit(), req.getStudyMode());
        }
        return programmeMapper.selectProgrammeByFilters(req.getOffset(), req.getLimit(), req.getStudyMode(), req.getCertificateTypes());
    }

    private boolean isBlankList(List list) {
        return list == null || list.size() == 0;
    }
}
