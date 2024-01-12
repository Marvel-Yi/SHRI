package com.capstone.shri.service;

import com.capstone.shri.dao.ApplicationMapper;
import com.capstone.shri.dao.DocumentMapper;
import com.capstone.shri.entity.Document;
import com.capstone.shri.entity.User;
import com.capstone.shri.util.CommonConstant;
import com.capstone.shri.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

@Service
public class DocumentService implements CommonConstant {
    private final static Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private DocumentMapper documentMapper;

    @Value("${file.upload-path}")
    private String fileUploadPath;

    public String submitStuSignature(int appId, String signature) {
        applicationMapper.updateSignature(appId, signature.getBytes());
        return CommonUtil.getJsonRes(0, "ok", null);
    }

    public Document getDocumentByName(String documentName) {
        return documentMapper.selectDocumentByName(documentName);
    }

    public InputStream getDocInputStream(Document doc) {
        File file = new File(doc.getPath());
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("Creating InputStream failed: ", e);
        }
        return fileInputStream;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public String uploadApplicationFile(MultipartFile file, int documentType, int appId, User user) {
        String originName = file.getOriginalFilename();
        if (StringUtils.isBlank(originName)) {
            return CommonUtil.getJSONString(-1, "file name is empty");
        }
        if (file.getSize() > DOCUMENT_MAX_SIZE) {
            return CommonUtil.getJSONString(-1, "The maximum file size is 3 MB.");
        }

        String suffixName = originName.contains(".") ? originName.substring(originName.lastIndexOf(".")) : "";
        if (!".pdf".equals(suffixName)) {
            return CommonUtil.getJSONString(-1, "Please upload pdf file.");
        }
        String newFileName = documentType + "-" + user.getUserName() + "-" + appId + "-" + suffixName;
        documentMapper.deleteDocument(user.getId(), appId, documentType);
        File newFile = new File(fileUploadPath, newFileName);
        try {
            file.transferTo(newFile);
            Document document = new Document();
            document.setPath(newFile.getPath());
            document.setName(newFileName);
            document.setSuffix(suffixName);
            document.setCreateTime(new Date());
            document.setDocumentType(documentType);
            document.setUserId(user.getId());
            document.setAppId(appId);
            documentMapper.insertDocument(document);
        } catch (IOException e) {
            logger.error("File upload failed, file name: " + newFileName);
            return CommonUtil.getJSONString(-1, "file upload failed");
        }

        if (isUserAppDocumentComplete(user.getId(), appId)) {
            applicationMapper.updateStatus(appId, APPLICATION_STATUS_UNDER_REVIEW, null, new Date());
        }
        return CommonUtil.getJsonRes(0, "ok", newFileName);
    }

    private boolean isUserAppDocumentComplete(int userId, int appId) {
        int eduCertificate = documentMapper.hasDocument(userId, appId, EDUCATION_CERTIFICATE);
        int lanCertificate = documentMapper.hasDocument(userId, appId, LANGUAGE_CERTIFICATE);
        int empCertificate = documentMapper.hasDocument(userId, appId, EMPLOYMENT_CERTIFICATE);
        return eduCertificate == 1 && lanCertificate == 1 && empCertificate == 1;
    }
}
