package com.capstone.shri.controller;

import com.capstone.shri.dao.UserMapper;
import com.capstone.shri.entity.Document;
import com.capstone.shri.entity.User;
import com.capstone.shri.service.DocumentService;
import com.capstone.shri.util.CommonUtil;
import com.capstone.shri.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/application/sign")
    public String studentSign(int appId, String signature) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommonUtil.getJsonRes(-1, "please login", null);
        }

        return documentService.submitStuSignature(appId, signature);
    }

    @PostMapping("/document/upload")
    public String uploadApplicationFile(@RequestParam(name = "file") MultipartFile multipartFile,
                                        @RequestParam(name = "documentType") int documentType,
                                        @RequestParam(name = "appId") int appId,
                                        @RequestParam(name = "userId") int userId) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return CommonUtil.getJsonRes(-1, "file empty", null);
        }

        User user = userMapper.selectById(userId);

        return documentService.uploadApplicationFile(multipartFile, documentType, appId, user);
    }

    @GetMapping("/document/download")
    public String downloadApplicationFile(HttpServletResponse response, @RequestParam(name = "fileName") String fileName) {
        Document doc = documentService.getDocumentByName(fileName);
        if (doc == null) {
            return CommonUtil.getJSONString(-1, "File does not exist.");
        }

        try (InputStream inputStream = documentService.getDocInputStream(doc);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
             ServletOutputStream outputStream = response.getOutputStream();) {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            byte[] bytes = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        } catch (IOException ioException) {
            logger.error(ioException.toString());
        }

        return CommonUtil.getJSONString(0, "ok");
    }

    @GetMapping("/document/preview")
    public String previewApplicationPDF(HttpServletResponse response, @RequestParam(name = "fileName") String fileName) {
        Document doc = documentService.getDocumentByName(fileName);
        if (doc == null) {
            return CommonUtil.getJSONString(-1, "File does not exist.");
        }

        ServletOutputStream resOutputStream = null;

        try (InputStream inputStream = documentService.getDocInputStream(doc);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
             ServletOutputStream outputStream = response.getOutputStream();) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            byte[] bytes = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            resOutputStream = outputStream;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        } catch (IOException ioException) {
            logger.error(ioException.toString());
        }

        return CommonUtil.getJsonRes(0, "ok", resOutputStream);
    }
}
