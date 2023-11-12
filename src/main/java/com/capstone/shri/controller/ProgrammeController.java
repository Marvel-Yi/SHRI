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

    @PostMapping("/programme/apply")
    public String apply(@RequestBody UserAppFormData appFormData, @RequestParam(name = "programmeId") int programmeId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommonUtil.getJsonRes(-1, "please login", null);
        }

        return programmeService.apply(appFormData, programmeId, user);
    }

    @GetMapping("/programme/form")
    public String getFormData(@RequestParam("userId") int userId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommonUtil.getJsonRes(-1, "please login", null);
        }

        if (user.getUserType() == USER_TYPE_ADMIN || user.getId() == userId) {
            UserAppFormData formData = programmeService.getFormData(user.getId());
            return CommonUtil.getJsonRes(0, "ok", formData);
        }
        return CommonUtil.getJsonRes(-1, "access denied", null);
    }

    @PostMapping("/programme/application/update")
    public String updateAppFormData(@RequestBody UserAppFormData appFormData) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommonUtil.getJsonRes(-1, "please login", null);
        }

        if (user.getId() != appFormData.getUserId()) {
            return CommonUtil.getJSONString(-1, "access denied");
        }

        return programmeService.updateUserAppFormData(appFormData);
    }

    @GetMapping("/programme/application")
    public String getApplications(@RequestParam(name = "status") int status, @RequestParam(name = "current") int current, @RequestParam(name = "limit") int limit) {
        User user = hostHolder.getUser();
        if (user == null || user.getUserType() == USER_TYPE_ORDINARY) {
            return CommonUtil.getJsonRes(-1, "access denied", null);
        }

        return programmeService.getApplications(status, current, limit);
    }

    @GetMapping("programme/application/{userId}")
    public String getUserApplications(@PathVariable(name = "userId") int userId) {
        User user = hostHolder.getUser();
        if (user == null || user.getId() != userId) {
            return CommonUtil.getJsonRes(-1, "access denied", null);
        }

        return programmeService.getUserApplications(userId);
    }
}
