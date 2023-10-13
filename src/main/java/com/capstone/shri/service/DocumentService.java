package com.capstone.shri.service;

import com.capstone.shri.dao.ApplicationMapper;
import com.capstone.shri.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    @Autowired
    private ApplicationMapper applicationMapper;

    public String submitStuSignature(int appId, String signature) {
        applicationMapper.updateSignature(appId, signature.getBytes());
        return CommonUtil.getJsonRes(0, "ok", null);
    }
}
