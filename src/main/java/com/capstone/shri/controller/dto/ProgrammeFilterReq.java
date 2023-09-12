package com.capstone.shri.controller.dto;

import com.capstone.shri.util.CommonUtil;
import lombok.Data;

import java.util.List;

@Data
public class ProgrammeFilterReq {
    private int current;
    private int limit;
    private String studyMode;
    private List<String> certificateTypes;

    public int getOffset() {
        return CommonUtil.getOffset(current, limit);
    }
}
