package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Result;
import com.yupaits.docs.common.response.ResultCode;
import com.yupaits.docs.entity.Document;
import com.yupaits.docs.entity.DocumentHistory;
import com.yupaits.docs.repository.DocumentHistoryRepository;
import com.yupaits.docs.repository.DocumentRepository;
import com.yupaits.docs.util.validate.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

/**
 * 文档REST接口
 * Created by yupaits on 2017/8/5.
 */
@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @GetMapping("/documents/{documentId}")
    public Result getDocumentById(@PathVariable Integer documentId) {
        if (ValidateUtils.idInvalid(documentId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        return Result.ok(documentRepository.findOne(documentId));
    }

    @GetMapping("/directories/{directoryId}/documents")
    public Result getDirectoryDocuments(@PathVariable Integer directoryId) {
        if (ValidateUtils.idInvalid(directoryId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        return Result.ok(documentRepository.findByDirectoryId(directoryId));
    }

    @PostMapping("/documents")
    public Result createDocument(@RequestBody Document document) {
        if (document == null || ValidateUtils.idInvalid(document.getDirectoryId()) || StringUtils.isBlank(document.getContent())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        documentRepository.save(document);
        return Result.ok();
    }

    @DeleteMapping("/documents/{documentId}")
    public Result deleteDocument(@PathVariable Integer documentId) {
        if (ValidateUtils.idInvalid(documentId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Document documentInDb = documentRepository.findOne(documentId);
        if (documentInDb == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        saveDocumentHistory(documentInDb);
        documentRepository.delete(documentId);
        return Result.ok();
    }

    @PutMapping("/documents/{documentId}")
    public Result updateDocument(@RequestBody Document document) {
        if (document == null || ValidateUtils.idInvalid(document.getId()) || ValidateUtils.idInvalid(document.getDirectoryId())
                || StringUtils.isBlank(document.getContent())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Document documentInDb = documentRepository.findOne(document.getId());
        if (documentInDb == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        saveDocumentHistory(documentInDb);
        documentRepository.save(document);
        return Result.ok();
    }

    /**
     * 保存文档历史记录
     * @param document 文档
     */
    private void saveDocumentHistory(Document document) {
        DocumentHistory documentHistory = new DocumentHistory();
        if (StringUtils.isNotBlank(document.getContent())) {
            documentHistory.setDocumentId(document.getId());
            documentHistory.setContent(document.getContent());
            documentHistory.setSavedTime(new Date(System.currentTimeMillis()));
            documentHistoryRepository.save(documentHistory);
        }
    }
}
