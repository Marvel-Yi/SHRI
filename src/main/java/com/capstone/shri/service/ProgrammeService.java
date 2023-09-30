package com.capstone.shri.service;

import com.capstone.shri.controller.dto.ProgrammeFilterReq;
import com.capstone.shri.dao.AppFormDataMapper;
import com.capstone.shri.dao.ApplicationMapper;
import com.capstone.shri.dao.ProgrammeMapper;
import com.capstone.shri.entity.Programme;
import com.capstone.shri.entity.User;
import com.capstone.shri.entity.UserAppFormData;
import com.capstone.shri.entity.UserApplication;
import com.capstone.shri.util.CommonUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProgrammeService {
    @Autowired
    private ProgrammeMapper programmeMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private AppFormDataMapper appFormDataMapper;

    public List<Programme> getAllProgrammes(int cur, int limit) {
        return programmeMapper.selectProgrammeList(CommonUtil.getOffset(cur, limit), limit);
    }

    public String getProgrammeById(int programmeId, User user) {
        Programme programme = programmeMapper.selectProgrammeById(programmeId);
        boolean hasApplied = (user != null && applicationMapper.hasApplied(user.getId(), programmeId) > 0);
        Map<String, Object> map = new HashMap<>();
        map.put("programme", programme);
        map.put("has applied", hasApplied);
        return CommonUtil.getJSONString(0, "ok", map);
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

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public String apply(UserAppFormData appFormData, int programmeId, byte[] signature, User user) {
        if (appFormData != null) {
            appFormDataMapper.insertAppFormData(appFormData);
        }
        UserApplication app = new UserApplication();
        app.setUserId(user.getId());
        app.setProgrammeId(programmeId);
        app.setSignature(signature);
        applicationMapper.insertApplication(app);
        return CommonUtil.getJsonRes(0, "ok", null);
    }

    public UserAppFormData getFormData(int userId) {
        return appFormDataMapper.selectFormDataByUserId(userId);
    }
}
