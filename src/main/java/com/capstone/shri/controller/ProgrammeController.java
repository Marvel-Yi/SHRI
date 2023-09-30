package com.capstone.shri.controller;

import com.capstone.shri.controller.dto.ProgrammeFilterReq;
import com.capstone.shri.entity.Programme;
import com.capstone.shri.entity.User;
import com.capstone.shri.entity.UserAppFormData;
import com.capstone.shri.service.ProgrammeService;
import com.capstone.shri.util.CommonConstant;
import com.capstone.shri.util.CommonUtil;
import com.capstone.shri.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProgrammeController implements CommonConstant {
    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/programme/detail/{programmeId}")
    public String getProgrammeById(@PathVariable("programmeId") int programmeId) {
        return programmeService.getProgrammeById(programmeId, hostHolder.getUser());
    }

    @PostMapping("/programme/list")
    public String getProgrammeListByFilter(@RequestBody ProgrammeFilterReq programmeFilterReq) {
        List<Programme> programmes = programmeService.getAllProgrammesWithFilters(programmeFilterReq);
        return CommonUtil.getJsonRes(0, "succeed", programmes);
    }

    @PostMapping("/programme/apply/{programmeId}")
    public String apply(@RequestBody UserAppFormData appFormData, @PathVariable("programmeId") int programmeId, byte[] signature) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommonUtil.getJsonRes(-1, "please login", null);
        }

        return programmeService.apply(appFormData, programmeId, signature, user);
    }

    @GetMapping("/programme/form")
    public String getFormData() {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommonUtil.getJsonRes(-1, "please login", null);
        }

        UserAppFormData formData = programmeService.getFormData(user.getId());
        return CommonUtil.getJsonRes(0, "ok", formData);
    }
}
