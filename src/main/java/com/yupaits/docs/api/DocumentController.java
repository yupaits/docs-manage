package com.yupaits.docs.api;

import com.yupaits.docs.common.response.Response;
import com.yupaits.docs.common.response.ResponseBuilder;
import com.yupaits.docs.mapper.DocumentHistoryMapper;
import com.yupaits.docs.mapper.DocumentMapper;
import com.yupaits.docs.model.Document;
import com.yupaits.docs.model.DocumentHistory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

/**
 * Created by yupaits on 2017/8/5.
 */
@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentHistoryMapper documentHistoryMapper;

    @GetMapping("/documents/{documentId}")
    public Response<Document> getDocumentById(@PathVariable Integer documentId) {
        return ResponseBuilder.build(HttpStatus.OK, documentMapper.selectByPrimaryKey(documentId));
    }

    @GetMapping("/directories/{directoryId}/documents")
    public Response<List<Document>> getDirectoryDocuments(@PathVariable Integer directoryId) {
        Document document = new Document();
        document.setDirectoryId(directoryId);
        return ResponseBuilder.build(HttpStatus.OK, documentMapper.selectBySelective(document));
    }

    @PostMapping("/documents")
    public Response createDocument(@RequestBody Document document) {
        documentMapper.insertSelective(document);
        return ResponseBuilder.buildWithNoData(HttpStatus.OK);
    }

    @DeleteMapping("/documents/{documentId}")
    public Response deleteDocument(@PathVariable Integer documentId) {
        documentMapper.deleteByPrimaryKey(documentId);
        return ResponseBuilder.buildWithNoData(HttpStatus.OK);
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
        return ResponseBuilder.buildWithNoData(HttpStatus.OK);
    }
}
