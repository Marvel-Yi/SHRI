package com.capstone.shri.controller;

import com.capstone.shri.entity.Interest;
import com.capstone.shri.entity.User;
import com.capstone.shri.service.InterestService;
import com.capstone.shri.util.CommonConstant;
import com.capstone.shri.util.CommonUtil;
import com.capstone.shri.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InterestController implements CommonConstant {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private InterestService interestService;

    @PostMapping("/interest/enquire")
    public String submitInterest(@RequestBody Interest interest) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommonUtil.getJsonRes(-1, "please login", null);
        }

        int res = interestService.insertInterest(interest);
        return CommonUtil.getJsonRes(res, "submitted", null);
    }

    @GetMapping("/interest/unprocessed")
    public String viewUnprocessedInterests(@RequestParam(name = "current") int current, @RequestParam(name = "limit") int limit) {
        if (hostHolder.getUser() == null || hostHolder.getUser().getUserType() != USER_TYPE_ADMIN) {
            return CommonUtil.getJsonRes(-1, "Access denied", null);
        }

        List<Interest> interests = interestService.viewUnprocessedInterests(current, limit);
        return CommonUtil.getJsonRes(0, "ok", interests);
    }

    @GetMapping("/interest/processed")
    public String viewProcessedInterests(@RequestParam(name = "current") int current, @RequestParam(name = "limit") int limit) {
        if (hostHolder.getUser() == null || hostHolder.getUser().getUserType() != USER_TYPE_ADMIN) {
            return CommonUtil.getJsonRes(-1, "Access denied", null);
        }

        List<Interest> interests = interestService.viewProcessedInterests(current, limit);
        return CommonUtil.getJsonRes(0, "ok", interests);
    }

    @PostMapping("/interest/reply")
    public String replyInterest(@RequestBody Interest interest) {
        interestService.replyInterest(interest);
        return CommonUtil.getJsonRes(0, "reply ", null);
    }
}
