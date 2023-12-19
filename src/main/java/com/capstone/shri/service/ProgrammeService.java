package com.capstone.shri.service;

import com.capstone.shri.controller.dto.ProgrammeFilterReq;
import com.capstone.shri.dao.AppFormDataMapper;
import com.capstone.shri.dao.ApplicationMapper;
import com.capstone.shri.dao.DocumentMapper;
import com.capstone.shri.dao.ProgrammeMapper;
import com.capstone.shri.entity.*;
import com.capstone.shri.util.CommonUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProgrammeService {
    @Autowired
    private ProgrammeMapper programmeMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private AppFormDataMapper appFormDataMapper;

    @Autowired
    private DocumentMapper documentMapper;

    public String getProgrammeById(int programmeId, User user) {
        Programme programme = programmeMapper.selectProgrammeById(programmeId);
        boolean hasApplied = (user != null && applicationMapper.hasApplied(user.getId(), programmeId) > 0);
        boolean hasFormData = appFormDataMapper.hasUserFormData(user.getId()) > 0;
        Map<String, Object> map = new HashMap<>();
        map.put("programme", programme);
        map.put("hasApplied", hasApplied);
        map.put("hasFormData", hasFormData);
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
    public String apply(UserAppFormData appFormData, int programmeId, User user) {
        if (appFormData.getPassportNo() != null) {
            appFormData.setUserId(user.getId());
            appFormDataMapper.insertAppFormData(appFormData);
        }

        Programme programme = programmeMapper.selectProgrammeById(programmeId);
        UserApplication app = new UserApplication();
        app.setUserId(user.getId());
        app.setUserName(user.getUserName());
        app.setUserEmail(user.getEmail());
        app.setProgrammeId(programmeId);
        app.setProgrammeName(programme.getName());
        app.setApplyDate(new Date());
        applicationMapper.insertApplication(app);
        return CommonUtil.getJsonRes(0, "ok", app);
    }

    public UserAppFormData getFormData(int userId) {
        return appFormDataMapper.selectFormDataByUserId(userId);
    }

    public String getApplications(int status, int current, int limit) {
        List<UserApplication> userApplications = applicationMapper.selectApplications(status, CommonUtil.getOffset(current, limit), limit);
        return CommonUtil.getJsonRes(0, "ok", userApplications);
    }

    public String getUserApplications(int userId) {
        List<UserApplication> applications = applicationMapper.selectApplicationsByUserId(userId);
        List<Map<String, Object>> appVOList = new ArrayList<>();
        for (UserApplication app : applications) {
            Map<String, Object> appVO = new HashMap<>();
            appVO.put("application", app);

            List<Document> userAppDocs = documentMapper.selectUserAppDocNames(app.getUserId(), app.getId());
            List<String> docNames = userAppDocs.stream().map(doc -> doc.getName()).collect(Collectors.toList());
            appVO.put("docNames", docNames);

            appVOList.add(appVO);
        }
        return CommonUtil.getJsonRes(0, "ok", appVOList);
    }

    public String updateUserAppFormData(UserAppFormData data) {
        appFormDataMapper.update(data);
        return CommonUtil.getJsonRes(0, "ok", data);
    }

    public String changeAppStatus(int appId, int status) {
        applicationMapper.updateStatus(appId, status);
        return CommonUtil.getJSONString(0, "ok");
    }
}
