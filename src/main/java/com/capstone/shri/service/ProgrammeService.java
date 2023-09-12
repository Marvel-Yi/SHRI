package com.capstone.shri.service;

import com.capstone.shri.controller.dto.ProgrammeFilterReq;
import com.capstone.shri.dao.ProgrammeMapper;
import com.capstone.shri.entity.Programme;
import com.capstone.shri.util.CommonUtil;
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
        if (req.getStudyMode() == null && req.getCertificateTypes() == null) {
            return programmeMapper.selectProgrammeList(req.getOffset(), req.getLimit());
        }
        if (req.getStudyMode() == null) {
            return programmeMapper.selectProgrammeByCertificate(req.getOffset(), req.getLimit(), req.getCertificateTypes());
        }
        if (req.getCertificateTypes() == null) {
            return programmeMapper.selectProgrammeByMode(req.getOffset(), req.getLimit(), req.getStudyMode());
        }
        return programmeMapper.selectProgrammeByFilters(req.getOffset(), req.getLimit(), req.getStudyMode(), req.getCertificateTypes());
    }
}
