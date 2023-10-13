package com.capstone.shri.controller;

import com.capstone.shri.entity.User;
import com.capstone.shri.service.DocumentService;
import com.capstone.shri.util.CommonUtil;
import com.capstone.shri.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentController {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private DocumentService documentService;

    @PostMapping("/application/sign")
    public String studentSign(int appId, String signature) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommonUtil.getJsonRes(-1, "please login", null);
        }

        return documentService.submitStuSignature(appId, signature);
    }
}
