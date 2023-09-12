package com.capstone.shri.controller;

import com.capstone.shri.controller.dto.ProgrammeFilterReq;
import com.capstone.shri.entity.Programme;
import com.capstone.shri.service.ProgrammeService;
import com.capstone.shri.util.CommonConstant;
import com.capstone.shri.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProgrammeController implements CommonConstant {
    @Autowired
    private ProgrammeService programmeService;

    @GetMapping("/programmeDetail/{programmeId}")
    public String getProgrammeById(@PathVariable("programmeId") int programmeId) {
        Programme programme = programmeService.getProgrammeById(programmeId);
        return CommonUtil.getJsonRes(0, "succeed", programme);
    }

    @PostMapping("/programmeList")
    public String getProgrammeListByFilter(@RequestBody ProgrammeFilterReq programmeFilterReq) {
        List<Programme> programmes = programmeService.getAllProgrammesWithFilters(programmeFilterReq);
        return CommonUtil.getJsonRes(0, "succeed", programmes);
    }
}
