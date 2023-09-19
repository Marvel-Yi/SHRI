package com.capstone.shri.service;

import com.capstone.shri.dao.InterestMapper;
import com.capstone.shri.entity.Interest;
import com.capstone.shri.util.CommonConstant;
import com.capstone.shri.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterestService implements CommonConstant {
    @Autowired
    private InterestMapper interestMapper;

    @Autowired
    private MailClient mailClient;

    public int insertInterest(Interest interest) {
        String reply = "Hi, " + interest.getUserName() + " we have received your submission, we will send you a reply soon, please check your email box.";
        mailClient.send(interest.getUserEmail(), EMAIL_SUBJECT_INTEREST_SUBMISSION, reply);
        return interestMapper.insertInterest(interest);
    }

    public List<Interest> viewUnprocessedInterests(int current, int limit) {
        int offset = (current - 1) * limit;
        return interestMapper.selectInterests(TYPE_UNPROCESSED, offset, limit);
    }

    public List<Interest> viewProcessedInterests(int current, int limit) {
        int offset = (current - 1) * limit;
        return interestMapper.selectInterests(TYPE_PROCESSED, offset, limit);
    }

    public int replyInterest(Interest interest) {
        int res = interestMapper.updateStatus(interest.getId(), TYPE_PROCESSED, interest.getResponse());
        mailClient.send(interest.getUserEmail(), EMAIL_SUBJECT_INTEREST_RESPONSE, interest.getResponse());
        return res;
    }
}
