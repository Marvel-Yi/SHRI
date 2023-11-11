package com.capstone.shri.dao;

import com.capstone.shri.entity.Document;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DocumentMapper {
    int insertDocument(Document document);

    int hasDocument(int userId, int appId, int documentType);

    int deleteDocument(int userId, int appId, int documentType);

    Document selectDocumentByName(String name);

    List<Document> selectAllDocuments();
}
