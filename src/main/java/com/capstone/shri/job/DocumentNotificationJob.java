package com.capstone.shri.job;

import com.capstone.shri.dao.ApplicationMapper;
import com.capstone.shri.dao.DocumentMapper;
import com.capstone.shri.entity.Document;
import com.capstone.shri.entity.UserApplication;
import com.capstone.shri.util.CommonConstant;
import com.capstone.shri.util.MailClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class DocumentNotificationJob implements CommonConstant {
    private static final Logger logger = LoggerFactory.getLogger(DocumentNotificationJob.class);

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private MailClient mailClient;

    @PostConstruct
    private void init() {
//        scheduledExecutorService.scheduleAtFixedRate(new DocumentNotification(), 0, 1, TimeUnit.DAYS);
    }

    private class DocumentNotification implements Runnable {
        @Override
        public void run() {
            List<UserApplication> applications = applicationMapper.selectApplications(0, 0, 10000);
            Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
            Map<Integer, UserApplication> appInfoMap = new HashMap<>();
            for (UserApplication app : applications) {
                int appId = app.getId();
                int userId = app.getUserId();
                Map<Integer, Integer> usrDocTypeMap = map.get(userId);
                if (usrDocTypeMap == null) {
                    usrDocTypeMap = new HashMap<>();
                    map.put(userId, usrDocTypeMap);
                }
                usrDocTypeMap.put(appId, 0);
                appInfoMap.put(appId, app);
            }

            List<Document> documents = documentMapper.selectAllDocuments();
            for (Document doc : documents) {
                int userId = doc.getUserId();
                int appId = doc.getAppId();
                int docType = doc.getDocumentType();
                int types = map.get(userId).get(appId);
                types |= 1 << docType;
                map.get(userId).put(appId, types);
            }

            for (Map<Integer, Integer> userDocTypeMap : map.values()) {
                for (Map.Entry<Integer, Integer> en2 : userDocTypeMap.entrySet()) {
                    int appId = en2.getKey();
                    int types = en2.getValue();
                    if (types == 7) {
                        continue;
                    }

                    UserApplication app = appInfoMap.get(appId);
                    String userName = app.getUserName();
                    String email = app.getUserEmail();
                    String programmeName = app.getProgrammeName();
                    String subject = EMAIL_SUBJECT_UPLOAD_DOCUMENT;
                    String text = "Dear " + userName + "! I hope this email finds you well. I am writing to remind you about the remaining documents needed for your "
                            + programmeName + " application. Please submit the required documents on our website as soon as possible to complete your application process.";

                    mailClient.send(email, subject, text);
                    logger.info("Reminder user " + userName + " to upload documents for " + programmeName + " application.");
                }
            }
        }
    }
}





