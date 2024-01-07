package com.capstone.shri.job;

import com.capstone.shri.dao.ApplicationMapper;
import com.capstone.shri.entity.UserApplication;
import com.capstone.shri.util.CommonConstant;
import com.capstone.shri.util.MailClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class DecisionSendingJob {
    private final static Logger logger = LoggerFactory.getLogger(DecisionSendingJob.class);

    private final static String REJECT_EMAIL_CONTENT = "Dear {studentName},\n\nThank you for applying to the {programmeName} programme from SHRI Academy. We appreciate the time you have spent in preparing for your application.\n\nYour application has been given careful consideration and on this occasion we are unable to offer you a place.\n\n{advice}\n\nBest Regards,\n\nSHRI Academy";

    private final static String ADVICE_PREFIX = "Here are some feedbacks from SHRI Academy:\n";

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private MailClient mailClient;

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    private void init() {
        scheduledExecutorService.scheduleAtFixedRate(new DecisionSending(), 0, 15, TimeUnit.SECONDS);
    }

    private class DecisionSending implements Runnable, CommonConstant {
        @Override
        public void run() {
            List<UserApplication> decidedApps = applicationMapper.selectDecidedApps();
            logger.info("[DecisionSendingJob] Size of decided app list: " + decidedApps.size());
            for (UserApplication app : decidedApps) {
                String userName = app.getUserName();
                String email = app.getUserEmail();
                String programmeName = app.getProgrammeName();
                int appId = app.getId();
                String subject = "Outcome of applying for SHRI " + programmeName + " programme-" + appId;

                switch (app.getStatus()) {
                    case APPLICATION_STATUS_REJECTION:
                        String text = REJECT_EMAIL_CONTENT;
                        text = text.replace("{studentName}", userName).replace("{programmeName}", programmeName);
                        String advice = app.getAdvice();
                        if (advice != null) {
                            text = text.replace("{advice}", ADVICE_PREFIX + advice + "\n\n");
                        }
                        // transaction
                        mailClient.send(email, subject, text);
                        logger.info("[DecisionSendingJob] Rejection notification has sent. AppId: " + appId);
                        applicationMapper.updateHasSent(appId);
                    case APPLICATION_STATUS_OFFER:
                }
            }
        }
    }
}
