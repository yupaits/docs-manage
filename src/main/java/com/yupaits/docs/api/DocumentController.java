package com.yupaits.docs.api;

import com.yupaits.docs.common.Response;
import com.yupaits.docs.common.ResponseBuilder;
import com.yupaits.docs.model.Document;
import com.yupaits.docs.model.DocumentHistory;
import com.yupaits.docs.mapper.DocumentHistoryMapper;
import com.yupaits.docs.mapper.DocumentMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

/**
 * Created by yupaits on 2017/8/5.
 */
@RestController
public class DocumentController {

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentHistoryMapper documentHistoryMapper;

    @GetMapping("/documents/{documentId}")
    public Response<Document> getDocumentById(@PathVariable Integer documentId) {
        return ResponseBuilder.success(documentMapper.selectByPrimaryKey(documentId));
    }

    @GetMapping("/directories/{directoryId}/documents")
    public Response<List<Document>> getDirectoryDocuments(@PathVariable Integer directoryId) {
        Document document = new Document();
        document.setDirectoryId(directoryId);
        return ResponseBuilder.success(documentMapper.selectBySelective(document));
    }

    @PostMapping("/documents")
    public Response createDocument(@RequestBody Document document) {
        documentMapper.insertSelective(document);
        return ResponseBuilder.success();
    }

    @DeleteMapping("/documents/{documentId}")
    public Response deleteDocument(@PathVariable Integer documentId) {
        documentMapper.deleteByPrimaryKey(documentId);
        return ResponseBuilder.success();
    }

    @PutMapping("/documents/{documentId}")
    public Response updateDocument(@RequestBody Document document) {
        Document documentInDb = documentMapper.selectByPrimaryKey(document.getId());
        DocumentHistory documentHistory = new DocumentHistory();
        if (StringUtils.isNotBlank(documentInDb.getContent())) {
            documentHistory.setDocumentId(documentInDb.getId());
            documentHistory.setContent(documentInDb.getContent());
            documentHistory.setSavedTime(new Date(System.currentTimeMillis()));
            documentHistoryMapper.insertSelective(documentHistory);
        }
        documentMapper.updateByPrimaryKeySelective(document);
        return ResponseBuilder.success();
    }
}
